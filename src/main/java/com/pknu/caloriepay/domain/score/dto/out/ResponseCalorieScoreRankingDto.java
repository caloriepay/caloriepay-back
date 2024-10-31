package com.pknu.caloriepay.domain.score.dto.out;

import com.pknu.caloriepay.domain.score.domain.CalorieScore;
import com.pknu.caloriepay.domain.user.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCalorieScoreRankingDto{

    private Long id;

    private Long userId;

    private String name;

    private LocalDate date;

    private Integer score;

    private Long rank;

    private Integer perRank;


    public static ResponseCalorieScoreRankingDto of(CalorieScore calorieScore, Member member, Long rank){
        return ResponseCalorieScoreRankingDto.builder()
                .id(calorieScore.getId())
                .userId(member.getId())
                .date(calorieScore.getDate())
                .score(calorieScore.getScore())
                .rank(rank)
                .name(member.getName())
                .build();
    }
    public void updatePerRank(Integer perRank){
        this.perRank=perRank;
    }
}
