package com.pknu.caloriepay.calender.repository;

import com.pknu.caloriepay.domain.Calender;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import static com.pknu.caloriepay.domain.QCalender.calender;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CalenderRepositoryImpl implements CalenderCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Calender> findAllByMemberIdAndDate(Long memberId, LocalDate start, LocalDate end) {

        return jpaQueryFactory.selectFrom(calender)
                .where(calender.memberId.eq(memberId)
                        .and(calender.date.between(start,end)))
                .fetch();
    }
}
