package com.pknu.caloriepay.domain.score.dto.out;

import com.pknu.caloriepay.domain.score.domain.CalorieScore;
import com.pknu.caloriepay.domain.user.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCalorieScoreDto {
    private Long id;

    private Long userId;

    private String name;

    private LocalDate date;

    private Integer score;

    public static ResponseCalorieScoreDto fromEntity(CalorieScore calorieScore, Member member) {
        return ResponseCalorieScoreDto.builder()
                .id(calorieScore.getId())
                .name(member.getName())
                .score(calorieScore.getScore())
                .userId(calorieScore.getUserId())
                .date(calorieScore.getDate())
                .build();
    }

}
