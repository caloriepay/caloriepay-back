package com.pknu.caloriepay.domain.score.dto.out;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseScoreAndCalorieDto {


    private ResponseDailyCalorieChangeDto calorieChange;
    private ResponseCalorieScoreDto calorieScore;
}
