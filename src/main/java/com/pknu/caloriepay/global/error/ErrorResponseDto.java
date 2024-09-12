package com.pknu.caloriepay.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponseDto {
    private Integer code;
    private String messsage;
}
