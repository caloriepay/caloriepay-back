package com.pknu.caloriepay.domain.score.dto.out;

import com.pknu.caloriepay.domain.score.domain.DailyCalorieChange;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDailyCalorieChangeDto {

    private Long id;

    private Long userId;

    private double dailyRecommendedCalorie;

    private double remainCalorie;

    private double totalCalorie;


    public static ResponseDailyCalorieChangeDto fromEntity(DailyCalorieChange calorieChange){
        return ResponseDailyCalorieChangeDto.builder()
                .id(calorieChange.getId())
                .userId(calorieChange.getUserId())
                .dailyRecommendedCalorie(calorieChange.getDailyRecommendedCalorie())
                .remainCalorie(calorieChange.getRemainCalorie())
                .totalCalorie(calorieChange.getTotalCalorie())
                .build();
    }

}
