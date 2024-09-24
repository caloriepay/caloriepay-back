package com.pknu.caloriepay.domain.calender.dto;

import com.pknu.caloriepay.domain.calender.domain.MealRecordDetail;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseMealRecordDetailDto {

    private Long id;

    private Long foodId;

    private Long MealRecordId;

    public static ResponseMealRecordDetailDto from(MealRecordDetail entity){
        return ResponseMealRecordDetailDto
                .builder()
                .id(entity.getId())
                .foodId(entity.getFoodId())
                .MealRecordId(entity.getMealRecord().getId())
                .build();
    }
}
