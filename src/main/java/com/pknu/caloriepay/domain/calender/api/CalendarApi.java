package com.pknu.caloriepay.domain.calender.api;

import com.pknu.caloriepay.domain.auth.dto.info.CurrentMemberInfo;
import com.pknu.caloriepay.domain.calender.application.CalendarDetailSearchService;
import com.pknu.caloriepay.domain.calender.application.CalendarSearchService;
import com.pknu.caloriepay.domain.calender.dto.ResponseCalendarDto;
import com.pknu.caloriepay.domain.calender.dto.ResponseCalenderDetailDto;
import com.pknu.caloriepay.domain.calender.exception.StartIsAfterEndDateException;
import com.pknu.caloriepay.domain.exercise.dto.ResponseExerciseRecordDto;
import com.pknu.caloriepay.global.dto.BaseRes;
import com.pknu.caloriepay.global.enums.ResCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calendar")
@Validated
public class CalendarApi {

    private final CalendarDetailSearchService calendarDetailSearchService;
    private final CalendarSearchService calendarSearchService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        binder.registerCustomEditor(LocalDate.class, new CustomDateEditor(new java.text.SimpleDateFormat("yyyy-MM-dd"), true) {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                text = text.replaceAll("\"", ""); // 따옴표 제거
                setValue(LocalDate.parse(text, dateFormatter));
            }
        });
    }

    @GetMapping("")
    public ResponseEntity<BaseRes<List<ResponseCalendarDto>>> getCalendarByMemberId(
            @AuthenticationPrincipal CurrentMemberInfo memberInfo,
            @RequestParam ("start") LocalDate start,
            @RequestParam("end") LocalDate end)
    {
        if (start.isAfter(end)){
            throw new StartIsAfterEndDateException(ResCode.START_IS_AFTER_END_DATE);
        }
        List<ResponseCalendarDto> calendarDtoList = calendarSearchService.getCalendarByUserIdAndDate(memberInfo.memberId(), start,end);

        return ResponseEntity.ok(BaseRes.success(calendarDtoList));
    }

    @GetMapping("/detail")
    public ResponseEntity<BaseRes<ResponseCalenderDetailDto>> getCalenderDetailByMemberId(
            @AuthenticationPrincipal CurrentMemberInfo memberInfo,
            @RequestParam("date")LocalDate date){

        List<ResponseExerciseRecordDto> exerciseRecordList = calendarDetailSearchService.getExerciseRecordList(memberInfo.memberId(), date);

        return ResponseEntity.ok(BaseRes.success(new ResponseCalenderDetailDto(exerciseRecordList)));
    }
}
