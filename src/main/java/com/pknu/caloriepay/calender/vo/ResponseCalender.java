package com.pknu.caloriepay.calender.vo;

import com.pknu.caloriepay.calender.dto.CalenderDto;
import com.pknu.caloriepay.domain.Tier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class ResponseCalender {

    private Long id;
    private Long memberId;
    private LocalDate date;
    private Tier tier;

    public static ResponseCalender of(CalenderDto calenderDto){
        return ResponseCalender.builder()
                .id(calenderDto.getId())
                .memberId(calenderDto.getMemberId())
                .date(calenderDto.getDate())
                .tier(calenderDto.getTier())
                .build();
    }
}
