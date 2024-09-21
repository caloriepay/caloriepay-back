package com.pknu.caloriepay.calender.dto;

import com.pknu.caloriepay.domain.Calender;
import com.pknu.caloriepay.domain.Tier;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class CalenderDto {

    private Long id;

    private Long memberId;

    private LocalDate date;

    private Tier tier;

    public static CalenderDto of (Calender calender){
        return CalenderDto.builder()
                .id(calender.getId())
                .memberId(calender.getMemberId())
                .date(calender.getDate())
                .tier(calender.getTier())
                .build();
    }
}

