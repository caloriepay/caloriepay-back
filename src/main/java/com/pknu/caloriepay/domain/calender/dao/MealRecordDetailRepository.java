package com.pknu.caloriepay.domain.calender.dao;

import com.pknu.caloriepay.domain.calender.domain.MealRecordDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealRecordDetailRepository extends JpaRepository<MealRecordDetail, Long> {
    List<MealRecordDetail> findAllByMealRecord_Id(Long id);
}
