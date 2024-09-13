package com.pknu.caloriepay.calender.vo;

import com.pknu.caloriepay.domain.Spend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Builder
public class ResponseCalenderDetail {

    private ResponseSpend spend;
    private ResponseEarn earn;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ResponseSpend {
        private Timestamp dateTime;
        private List<SpendData> data;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class SpendData {
        private Long id;
        private Long memberId;
        private Integer foodKcal;
        private String foodName;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ResponseEarn {
        private String title;
        private List<EarnData> data;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class EarnData {
        private Long id;
        private Long memberId;
        private Integer exerciseKcal;
        private String exerciseName;
        private Integer exerciseTime;
    }
}
