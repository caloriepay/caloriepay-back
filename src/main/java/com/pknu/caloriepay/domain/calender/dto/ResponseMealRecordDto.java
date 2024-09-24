package com.pknu.caloriepay.domain.calender.dto;

import com.pknu.caloriepay.domain.calender.domain.MealRecord;
import com.pknu.caloriepay.domain.calender.domain.MealRecordDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@ToString
public class ResponseMealRecordDto {
    private Long id;

    private Long memberId;

    private Long calenderId;

    private LocalDateTime mealTime;

    private List<ResponseMealRecordDetailWithFoodDto> mealList;

    public static ResponseMealRecordDto of(List<ResponseMealRecordDetailWithFoodDto> mealList , MealRecord mealRecord){
        return ResponseMealRecordDto.builder()
                .id(mealRecord.getId())
                .memberId(mealRecord.getMemberId())
                .calenderId(mealRecord.getCalender().getId())
                .mealTime(mealRecord.getMealTime())
                .mealList(mealList)
                .build();
    }
}
