package com.pknu.caloriepay.domain.user.dto;

import com.pknu.caloriepay.domain.user.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponseDto {
    private Long memberId;

    // Member -> MemberResponseDto 변환 메서드
    public static MemberResponseDto from(Member member) {
        return MemberResponseDto.builder()
                .memberId(member.getId())
                .build();
    }
}
