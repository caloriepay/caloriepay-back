package com.pknu.caloriepay.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NOT_ALLOWED_DATE_FORMAT(613, "날짜형식에 맞지 않습니다"),
    START_IS_AFTER_END(614, "시작 날짜는 종료날짜보다 빨라야합니다.");


    private final Integer code;
    private final String message;


}
