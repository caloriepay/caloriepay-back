package com.pknu.caloriepay.calender.vo;

import com.pknu.caloriepay.domain.Tier;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ResponseCalender {

    private Long id;
    private Long memberId;
    private LocalDate date;
    private Tier tier;
}
