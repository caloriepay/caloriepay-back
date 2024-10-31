package com.pknu.caloriepay.global.config.batch;

import com.pknu.caloriepay.domain.score.dao.CalorieScoreRepository;
import com.pknu.caloriepay.domain.score.dao.DailyCalorieChangeRepository;
import com.pknu.caloriepay.domain.score.domain.CalorieScore;
import com.pknu.caloriepay.domain.score.domain.DailyCalorieChange;
import com.pknu.caloriepay.domain.tier.dao.DailyTierRepository;
import com.pknu.caloriepay.domain.tier.domain.DailyTier;
import com.pknu.caloriepay.domain.tier.domain.Tier;
import com.pknu.caloriepay.global.enums.ResCode;
import com.pknu.caloriepay.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DailySummeryJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DailyCalorieChangeRepository dailyCalorieChangeRepository;
    private final CalorieScoreRepository calorieScoreRepository;
    private final DailyTierRepository dailyTierRepository;

    @Bean
    public Job dailySummeryJob(){
        return new JobBuilder("dailySummeryJob", jobRepository)
                .start(dailyTierUpdateStep())
                .next(dailyCalorieScoreUpdateStep())
                .next(dailyCalorieUpdateStep())
                .build();
    }

    @Bean
    public Step dailyCalorieUpdateStep(){
        return new StepBuilder("dailyCalorieUpdateStep",jobRepository)
                .tasklet(dailyCalorieUpdateTasklet(),transactionManager)
                .build();
    }

    @Bean
    public Tasklet dailyCalorieUpdateTasklet() {
        return (contribution, chunkContext) -> {
            log.info("-dailyCalorieUpdateTasklet Run.-");
            dailyCalorieChangeRepository.findAll().forEach(calorieChange -> {
                calorieChange.calculateTotalCalorie();
                calorieChange.resetCalorie();
                dailyCalorieChangeRepository.save(calorieChange);
            });

            return RepeatStatus.FINISHED; // Tasklet 작업 완료 상태 반환
        };
    }
    @Bean
    public Step dailyCalorieScoreUpdateStep() {
        return new StepBuilder(" dailyCalorieScoreUpdateStep",jobRepository)
                .tasklet(dailyCalorieScoreUpdateTasklet(),transactionManager)
                .build();
    }
    @Bean
    public Tasklet dailyCalorieScoreUpdateTasklet() {
        return (contribution, chunkContext) -> {
            log.info("-dailyScoreUpdateTasklet Run.-");
            // 모든 DailyCalorieChange 엔티티를 조회합니다.
             dailyCalorieChangeRepository.findAll().forEach(dto -> {
                // 기존 칼로리 점수
                CalorieScore existingScore = calorieScoreRepository
                        .findByUserIdAndDate(dto.getUserId(), LocalDate.now().minusDays(1))
                        .orElseThrow(() -> new CustomException(ResCode.SCORE_NOT_FOUND));

                // 새로운 CalorieScore를 생성
                CalorieScore newScore = CalorieScore.builder()
                        .userId(dto.getUserId())
                        .score(existingScore.getScore()) // 이전 점수 또는 다른 계산 값으로 설정
                        .date(LocalDate.now())
                        .build();

                calorieScoreRepository.save(newScore);
            });



            return RepeatStatus.FINISHED; // Tasklet 작업 완료
        };
    }
    @Bean
    public Step dailyTierUpdateStep(){
        return new StepBuilder("dailyTierUpdateStep",jobRepository)
                .tasklet(dailyTierUpdateTasklet(),transactionManager)
                .build();
    }

    @Bean
    public Tasklet dailyTierUpdateTasklet(){
        return (contribution, chunkContext) -> {
            log.info("-dailyTierUpdateTasklet Run.-");
            dailyCalorieChangeRepository.findAll().forEach(calorieChange -> {
                Long userId = calorieChange.getUserId();
                dailyTierRepository.save(DailyTier.builder()
                        .userId(userId)
                        .tier(Tier.calculateDailyTier(calorieChange.getRemainCalorie())) // 티어 계산
                        .date(LocalDate.now().minusDays(1)) // 어제 날짜
                        .build()); // DailyTier 저장
            });

            return RepeatStatus.FINISHED;
        };
    }

}
