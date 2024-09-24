package com.pknu.caloriepay.domain.calender.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class MealRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private LocalDateTime mealTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calender_id")
    private Calender calender;
}
