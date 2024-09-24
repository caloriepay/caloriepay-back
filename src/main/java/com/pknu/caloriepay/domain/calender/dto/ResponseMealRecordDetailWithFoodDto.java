package com.pknu.caloriepay.domain.calender.dto;

import com.pknu.caloriepay.domain.food.domain.Food;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseMealRecordDetailWithFoodDto {

    private Long id;

    private Long foodId;

    private String foodName;

    private Integer foodKcal;

    public static ResponseMealRecordDetailWithFoodDto of(ResponseMealRecordDetailDto dto, Food food){
        return ResponseMealRecordDetailWithFoodDto.builder()
                .id(dto.getId())
                .foodId(food.getId())
                .foodKcal(food.getKcal())
                .foodName(food.getName())
                .build();
    }
}
