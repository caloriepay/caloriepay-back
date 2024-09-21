package com.pknu.caloriepay.calender.handler;

import com.pknu.caloriepay.calender.controller.CalenderController;
import com.pknu.caloriepay.calender.exception.StartIsAfterEndDateException;
import com.pknu.caloriepay.global.error.ErrorCode;
import com.pknu.caloriepay.global.error.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice(assignableTypes = CalenderController.class)
@Slf4j
public class CalenderControllerExceptionHandler {


    @ExceptionHandler(StartIsAfterEndDateException.class)
    public ResponseEntity<ErrorResponseDto> handleStartIsAfterEndDateException(StartIsAfterEndDateException ex){
        ErrorResponseDto errorResponseDto = ex.getErrorResponseDto();

        log.error("Error Location -> {}",ex.getStackTrace()[0]);
        log.error("Error Message -> {}",errorResponseDto.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponseDto);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleNotAllowedDateFormatException(MethodArgumentTypeMismatchException ex){

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.NOT_ALLOWED_DATE_FORMAT.getCode(), ErrorCode.NOT_ALLOWED_DATE_FORMAT.getMessage());

        log.error("Error Location -> {}",ex.getStackTrace()[0]);
        log.error("Error Message -> {}",errorResponseDto.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponseDto);
    }

}
