package com.pknu.caloriepay.global.config.batch;

import com.pknu.caloriepay.domain.score.dao.DailyCalorieChangeRepository;
import com.pknu.caloriepay.domain.score.domain.DailyCalorieChange;
import com.pknu.caloriepay.domain.tier.dao.MonthlyTierRepository;
import com.pknu.caloriepay.domain.tier.domain.MonthlyTier;
import com.pknu.caloriepay.domain.tier.domain.Tier;
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
public class MonthSummeryJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DailyCalorieChangeRepository dailyCalorieChangeRepository;
    private final MonthlyTierRepository monthlyTierRepository; // MonthlyTierRepository 추가

    @Bean
    public Job monthCalorieSummaryJob() {
        return new JobBuilder("monthCalorieSummaryJob", jobRepository)
                .start(monthlyTierUpdateStep()) // 첫 번째 스텝
                .next(monthCalorieUpdateStep()) // 두 번째 스텝
                .build();
    }

    @Bean
    public Step monthCalorieUpdateStep() {
        return new StepBuilder("monthCalorieUpdateStep", jobRepository)
                .tasklet(monthCalorieUpdateTasklet(),transactionManager)
                .build();
    }

    @Bean
    public Tasklet monthCalorieUpdateTasklet() {
        return (contribution, chunkContext) -> {
            List<DailyCalorieChange> dailyCalorieChangeDtoList = dailyCalorieChangeRepository.findAll();
            dailyCalorieChangeDtoList.forEach(calorieChange -> {
                calorieChange.resetTotalCalorie(); // 총 칼로리 초기화
                dailyCalorieChangeRepository.save(calorieChange); // 업데이트된 엔티티 저장
            });
            return RepeatStatus.FINISHED; // Tasklet 작업 완료 상태 반환
        };
    }

    @Bean
    public Step monthlyTierUpdateStep() {
        return new StepBuilder("monthlyTierUpdateStep", jobRepository)
                .tasklet(monthlyTierUpdateTasklet(),transactionManager)
                .build();
    }

    @Bean
    public Tasklet monthlyTierUpdateTasklet() {
        return (contribution, chunkContext) -> {
            List<DailyCalorieChange> dailyCalorieChangeDtoList = dailyCalorieChangeRepository.findAll();

            dailyCalorieChangeDtoList.forEach(calorieChange -> {
                Long userId = calorieChange.getUserId();
                // 토탈칼로리 비교 후 티어 정보 기록
                monthlyTierRepository.save(MonthlyTier.builder()
                        .userId(userId)
                        .tier(Tier.calculateMonthlyTier(calorieChange.getTotalCalorie()))
                        .date(LocalDate.now().minusDays(1))
                        .build());
            });

            return RepeatStatus.FINISHED; // Tasklet 작업 완료 상태 반환
        };
    }
}
