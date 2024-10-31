package com.pknu.caloriepay.domain.tier.application;

import com.pknu.caloriepay.domain.tier.dao.DailyTierRepository;
import com.pknu.caloriepay.domain.tier.dto.out.ResponseDailyTierOfMonth;
import com.pknu.caloriepay.domain.tier.dto.out.ResponseTier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DailyTierService{

    private final DailyTierRepository dailyTierRepository;

    @Transactional(readOnly = true)
    public ResponseTier getTierByDate(Long userId, LocalDate date) {
        return dailyTierRepository.findByUserIdAndDate(userId, date)
            .map(ResponseTier::fromEntity)
            .orElse(null);
        }

//    @Async("threadPoolTaskExecutor")
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//    public void calculateTierAll(DailyCalorieSummaryEventDto eventDto) {
////      요일 정산시 어제날짜로 티어가 정산됨.
//        eventDto.getDailyCalorieChangeDtoList().forEach(calorieChange -> {
//            Long userId = calorieChange.getUserId();
//            dailyTierRepository.save(DailyTier.builder()
//                    .userId(userId)
//                    .tier(Tier.calculateDailyTier(calorieChange.getRemainCalorie())) // 티어 계산
//                    .date(LocalDate.now().minusDays(1)) // 어제 날짜
//                    .build()); // DailyTier 저장
//        });
//    }

    public List<ResponseDailyTierOfMonth> getDailyTierOfMonth(Long userId, LocalDate date){
        LocalDate startOfMonth = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());

        return dailyTierRepository.countByTierGroupByUserId(userId,startOfMonth,endOfMonth);
    }


}
