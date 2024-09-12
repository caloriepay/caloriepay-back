package com.pknu.caloriepay.calender.exception;

import com.pknu.caloriepay.global.error.ErrorCode;
import com.pknu.caloriepay.global.error.ErrorResponseDto;
import lombok.Getter;

@Getter
public class StartIsAfterEndDateException extends RuntimeException{
    private final ErrorResponseDto errorResponseDto;
    public StartIsAfterEndDateException(){
        this.errorResponseDto = new ErrorResponseDto(ErrorCode.START_IS_AFTER_END.getCode(), ErrorCode.START_IS_AFTER_END.getMessage());
    }

}
