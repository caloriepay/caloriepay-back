package com.pknu.caloriepay.domain.tier.application;

import com.pknu.caloriepay.domain.tier.dao.MonthlyTierRepository;
import com.pknu.caloriepay.domain.tier.dto.out.ResponseTier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class MonthlyTierService{

    private final MonthlyTierRepository monthlyTierRepository;
    public ResponseTier getTierByDate(Long userId, LocalDate date) {
        return monthlyTierRepository.findByUserIdAndDate(userId, date)
                .map(ResponseTier::fromEntity)
                .orElse(null);
    }

//    @Async("threadPoolTaskExecutor")
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//    public void calculateTierAll(MonthCalorieSummeryEventDto eventDto) {
//        eventDto.getDailyCalorieChangeDtoList()
//                .forEach((calorieChange -> {
//                    Long userId = calorieChange.getUserId();
////                  토탈칼로리 비교후 티어 정보 기록
//                    monthlyTierRepository.save(MonthlyTier.builder()
//                            .userId(userId)
//                            .tier(Tier.calculateMonthlyTier(calorieChange.getTotalCalorie()))
//                            .date(LocalDate.now().minusDays(1))
//                            .build());
//                }));
//    }

}
