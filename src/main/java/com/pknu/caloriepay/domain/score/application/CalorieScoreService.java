package com.pknu.caloriepay.domain.score.application;

import com.pknu.caloriepay.domain.score.dao.CalorieScoreRepository;
import com.pknu.caloriepay.domain.score.domain.CalorieScore;
import com.pknu.caloriepay.domain.score.dto.out.ResponseCalorieScoreDto;
import com.pknu.caloriepay.domain.user.dao.MemberRepository;
import com.pknu.caloriepay.domain.user.domain.Member;
import com.pknu.caloriepay.global.enums.ResCode;
import com.pknu.caloriepay.global.error.CustomException;
import com.pknu.caloriepay.global.event.DailyCalorieSummaryEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.IntStream;

import static com.pknu.caloriepay.global.config.RedisCacheConfig.USER_CALORIE_SCORE_CACHE;

@Service
@RequiredArgsConstructor
public class CalorieScoreService {

    private final CalorieScoreRepository calorieScoreRepository;
    private final MemberRepository memberRepository;

    @Cacheable(value = USER_CALORIE_SCORE_CACHE, key = "#userId",cacheManager = "caloriePayCacheManager")
    public ResponseCalorieScoreDto getCalorieScoreByUserIdAndDate(Long userId){
        Member member = memberRepository.findById(userId).orElseThrow(() ->new CustomException(ResCode.USER_NOT_FOUND));

        return calorieScoreRepository.findTopByUserIdOrderByDateDesc(userId)
                .map((calorieScore) ->ResponseCalorieScoreDto.fromEntity(calorieScore,member))
                .orElse(null);
    }
    public List<ResponseCalorieScoreDto> getCalorieScoreChangeFor5Month(Long userId,Integer offset) {
        Member member = memberRepository.findById(userId).orElseThrow(() ->new CustomException(ResCode.USER_NOT_FOUND));
        LocalDate currentDate = LocalDate.now();

        return IntStream.range(0, offset)
                .mapToObj(i -> {
                    LocalDate targetDate = currentDate.minusMonths(i);
                    return calorieScoreRepository.findLatestScoreByUserIdAndYearAndMonth(userId, targetDate.getYear(), targetDate.getMonthValue())
                            .map(score -> ResponseCalorieScoreDto.fromEntity(score,member))
                            .orElse(null);
                })
                .toList();
    }

    public ResponseCalorieScoreDto getHighCalorieScoreOfMonth(Long userId, LocalDate date){
        Member member = memberRepository.findById(userId).orElseThrow(() ->new CustomException(ResCode.USER_NOT_FOUND));

        LocalDate startOfMonth = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());

        return ResponseCalorieScoreDto.fromEntity(calorieScoreRepository.findHighScoreOfMonthByUserId(userId,startOfMonth,endOfMonth).orElseThrow( () ->
                new CustomException(ResCode.SCORE_NOT_FOUND)),member);
    }

    @Transactional
    public void refreshCalorieScoreByUserId(Long userId) {
        calorieScoreRepository.findByUserIdAndDate(userId, LocalDate.now())
                .ifPresentOrElse(
                        calorieScore -> {
                        },
                        () -> {
                            CalorieScore latestScore = calorieScoreRepository.findTopByUserIdOrderByDateDesc(userId)
                                    .orElseThrow(() -> new CustomException(ResCode.SCORE_NOT_FOUND));
                            calorieScoreRepository.save(
                                    CalorieScore.builder()
                                            .userId(latestScore.getUserId())
                                            .score(latestScore.getScore())
                                            .date(LocalDate.now())
                                            .build()
                            );
                        }
                );
    }

    @Async("threadPoolTaskExecutor")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void calculateScore(DailyCalorieSummaryEventDto eventDto) {
        eventDto.getDailyCalorieChangeDtoList().forEach(dto -> {
            CalorieScore existingScore = calorieScoreRepository.findByUserIdAndDate(dto.getUserId(), LocalDate.now().minusDays(1))
                    .orElseThrow(() -> new CustomException(ResCode.SCORE_NOT_FOUND));

            CalorieScore newScore = CalorieScore.builder()
                    .userId(dto.getUserId())
                    .score(existingScore.getScore()) // 이전 점수 또는 계산된 값
                    .date(LocalDate.now())
                    .build();

            calorieScoreRepository.save(newScore); // 새로 생성된 CalorieScore 저장
        });
    }
}
