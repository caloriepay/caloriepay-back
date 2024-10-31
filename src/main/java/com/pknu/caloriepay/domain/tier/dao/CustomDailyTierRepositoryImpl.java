package com.pknu.caloriepay.domain.tier.dao;

import com.pknu.caloriepay.domain.tier.domain.QDailyTier;
import com.pknu.caloriepay.domain.tier.domain.Tier;
import com.pknu.caloriepay.domain.tier.dto.out.ResponseDailyTierOfMonth;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

@Slf4j

@RequiredArgsConstructor
public class CustomDailyTierRepositoryImpl implements CustomDailyTierRepository{

    private final JPQLQueryFactory jpqlQueryFactory;

    @Override
    public List<ResponseDailyTierOfMonth> countByTierGroupByUserId(Long userId, LocalDate start, LocalDate end) {
        QDailyTier qDailyTier = QDailyTier.dailyTier;

        return jpqlQueryFactory.select(qDailyTier.tier,qDailyTier.count())
                .from(qDailyTier)
                .where(qDailyTier.userId.eq(userId)
                        .and(qDailyTier.date.between(start, end)))
                .groupBy(qDailyTier.tier)
                .fetch()
                .stream()
                .map((tuple -> new ResponseDailyTierOfMonth(tuple.get(0, Tier.class),tuple.get(1,Long.class))))
                .toList();

    }
}
