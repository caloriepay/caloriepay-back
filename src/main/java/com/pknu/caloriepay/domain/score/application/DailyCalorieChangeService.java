package com.pknu.caloriepay.domain.score.application;

import com.pknu.caloriepay.domain.score.dao.DailyCalorieChangeRepository;
import com.pknu.caloriepay.domain.score.domain.DailyCalorieChange;
import com.pknu.caloriepay.global.event.DailyCalorieSummaryEventDto;
import com.pknu.caloriepay.global.event.MonthCalorieSummeryEventDto;
import com.pknu.caloriepay.domain.score.dto.out.ResponseDailyCalorieChangeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.pknu.caloriepay.global.config.RedisCacheConfig.USER_CALORIE_CHANGE_CACHE;

@Service
@RequiredArgsConstructor
public class DailyCalorieChangeService {
//    일단 일일권장칼로리를 무적권 줘야하네

    private final ApplicationEventPublisher applicationEventPublisher;
    private final DailyCalorieChangeRepository dailyCalorieChangeRepository;

    @Cacheable(value = USER_CALORIE_CHANGE_CACHE,key = "#userId",cacheManager = "caloriePayCacheManager")
    public ResponseDailyCalorieChangeDto getCalorieChange(Long userId) {
        return dailyCalorieChangeRepository.findByUserId(userId)
                .map(ResponseDailyCalorieChangeDto::fromEntity)
                .orElse(null); // 값이 없을 경우 null을 반환
    }
//  일별 칼로리 정산
    @Async("threadPoolTaskExecutor")
    @Transactional
    @Scheduled(cron = "0 1 0 * * *")
    public void dailyCalorieSummary() {
        List<DailyCalorieChange> dailyCalorieChangeDtoList = dailyCalorieChangeRepository.findAll();
        DailyCalorieSummaryEventDto resultDto=new DailyCalorieSummaryEventDto(dailyCalorieChangeDtoList
                .stream()
                .map(ResponseDailyCalorieChangeDto::fromEntity)
                .toList());

        dailyCalorieChangeDtoList.forEach(calorieChange -> {
            calorieChange.calculateTotalCalorie();
            calorieChange.resetCalorie();
            dailyCalorieChangeRepository.save(calorieChange);
        });

        applicationEventPublisher.publishEvent(resultDto);
    }

//  월별칼로리정산
    @Async("threadPoolTaskExecutor")
    @Transactional
    @Scheduled(cron = "0 5 0 1 * *" ,zone = "Asia/Seoul")
    public void totalCalorieSummary() {
        List<DailyCalorieChange> dailyCalorieChangeDtoList = dailyCalorieChangeRepository.findAll();
        MonthCalorieSummeryEventDto resultDto = new MonthCalorieSummeryEventDto(dailyCalorieChangeDtoList
                .stream()
                .map(ResponseDailyCalorieChangeDto::fromEntity)
                .toList());

        dailyCalorieChangeDtoList.forEach(calorieChange -> {
            calorieChange.resetTotalCalorie();
            dailyCalorieChangeRepository.save(calorieChange);
        });

        applicationEventPublisher.publishEvent(resultDto);
    }

}
