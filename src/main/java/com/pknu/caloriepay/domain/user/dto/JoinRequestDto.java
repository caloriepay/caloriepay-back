package com.pknu.caloriepay.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JoinRequestDto {
    private String name;
    private String phoneNumber;
    private String nickname;
    private String email;
    private String password;


}
