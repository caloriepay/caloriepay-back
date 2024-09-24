package com.pknu.caloriepay.domain.calender.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class MealRecordDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long foodId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_record_id")
    private MealRecord mealRecord;
}
