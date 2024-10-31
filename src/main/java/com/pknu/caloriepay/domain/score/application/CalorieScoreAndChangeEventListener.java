package com.pknu.caloriepay.domain.score.application;

import com.pknu.caloriepay.domain.score.dao.CalorieScoreRepository;
import com.pknu.caloriepay.domain.score.dao.DailyCalorieChangeRepository;
import com.pknu.caloriepay.domain.score.domain.CalorieScore;
import com.pknu.caloriepay.domain.score.domain.DailyCalorieChange;
import com.pknu.caloriepay.global.enums.ResCode;
import com.pknu.caloriepay.global.error.CustomException;
import com.pknu.caloriepay.global.event.ExerciseEventDto;
import com.pknu.caloriepay.global.event.MealEventDto;
import com.pknu.caloriepay.global.event.UserProfileEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDate;

import static com.pknu.caloriepay.global.config.RedisCacheConfig.USER_CALORIE_CHANGE_CACHE;
import static com.pknu.caloriepay.global.config.RedisCacheConfig.USER_CALORIE_SCORE_CACHE;

@Component
@Slf4j
@RequiredArgsConstructor
public class CalorieScoreAndChangeEventListener {

    private static final int baseScore = 300;

    private final CalorieScoreRepository calorieScoreRepository;
    private final DailyCalorieChangeRepository dailyCalorieChangeRepository;
//  멤버 프로필 변경이 있을대

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void createCalorieScore(UserProfileEventDto userProfileEventDto) {
        calorieScoreRepository.findByUserIdAndDate(userProfileEventDto.getUserId(), LocalDate.now())
                .ifPresentOrElse(
                        // 이미 존재할 경우 아무 작업도 하지 않음
                        existingScore -> {},
                        // 존재하지 않을 경우 새로 생성하여 저장
                        () -> {
                            CalorieScore calorieScore = CalorieScore.builder()
                                    .userId(userProfileEventDto.getUserId())
                                    .date(LocalDate.now())
                                    .score(baseScore) // baseScore는 미리 정의된 값
                                    .build();
                            calorieScoreRepository.save(calorieScore);
                        }
                );
    }
//    멤버 프로필 변경이 있을때
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void createDailyCalorie(UserProfileEventDto userProfileEventDto){
        DailyCalorieChange dailyCalorieChange = dailyCalorieChangeRepository.findByUserId(userProfileEventDto.getUserId())
                .orElseGet(() -> DailyCalorieChange.builder()
                        .userId(userProfileEventDto.getUserId())
                        .totalCalorie(0)
                        .build());

        dailyCalorieChange.resetRecommendedCalorie(userProfileEventDto.getProfile());
        dailyCalorieChange.resetCalorie();

        dailyCalorieChangeRepository.save(dailyCalorieChange);
    }

//    운동 기록시 일별 칼로리 계산
    @CacheEvict(value = USER_CALORIE_SCORE_CACHE, key = "#exerciseEventDto.userId", cacheManager = "caloriePayCacheManager")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void caloriePlus(ExerciseEventDto exerciseEventDto){

        DailyCalorieChange calorieChange= dailyCalorieChangeRepository.findByUserId(exerciseEventDto.getUserId()).orElseThrow(() -> new CustomException(ResCode.DAILY_CHANGE_NOT_FOUND));
        calorieChange.plusCalorie(exerciseEventDto.getCalorie());
        dailyCalorieChangeRepository.save(calorieChange);
    }

    @CacheEvict(value = USER_CALORIE_CHANGE_CACHE, key = "#mealEventDto.userId", cacheManager = "caloriePayCacheManager")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void calorieMinus(MealEventDto mealEventDto){
        DailyCalorieChange calorieChange= dailyCalorieChangeRepository.findByUserId(mealEventDto.getUserId()).orElseThrow(() -> new CustomException(ResCode.DAILY_CHANGE_NOT_FOUND));
        calorieChange.minusCalorie(mealEventDto.getCalorie());
        dailyCalorieChangeRepository.save(calorieChange);
    }


}
