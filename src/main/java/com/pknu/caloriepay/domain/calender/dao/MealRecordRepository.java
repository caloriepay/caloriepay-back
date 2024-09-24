package com.pknu.caloriepay.domain.calender.dao;

import com.pknu.caloriepay.domain.calender.domain.MealRecord;
import com.pknu.caloriepay.domain.earn.domain.Earn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MealRecordRepository extends JpaRepository<MealRecord,Long> {

    List<MealRecord> findAllByMemberIdAndAndDate(Long memberId, LocalDate date);
}
