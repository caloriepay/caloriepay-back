package com.pknu.caloriepay.domain.calender.dto;

import com.pknu.caloriepay.domain.food.domain.Food;
import com.pknu.caloriepay.domain.spend.dto.SpendDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class ResponseMealRecordDataDto {

    private Long id;
    private Long memberId;
    private Integer foodKcal;
    private String foodName;

    public static ResponseMealRecordDataDto of(Food food, SpendDto spend){
        return ResponseMealRecordDataDto.builder()
                .id(spend.getId())
                .memberId(spend.getMemberId())
                .foodKcal(food.getKcal())
                .foodName(food.getName())
                .build();
    }
}
