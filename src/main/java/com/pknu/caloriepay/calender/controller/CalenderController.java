package com.pknu.caloriepay.calender.controller;

import com.pknu.caloriepay.calender.exception.StartIsAfterEndDateException;
import com.pknu.caloriepay.calender.service.CalenderService;
import com.pknu.caloriepay.calender.vo.ResponseCalender;
import com.pknu.caloriepay.calender.vo.ResponseCalenderDetail;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calender")
@Validated
public class CalenderController {

    private final CalenderService calenderService;

    @GetMapping()
    public ResponseEntity<List<ResponseCalender>> findCalenders(@RequestParam(name = "start")LocalDate start, @RequestParam(name = "end") LocalDate end){

        if(start.isAfter(end)){
            throw new StartIsAfterEndDateException();
        }
        Long memberId = 1L;
        List<ResponseCalender> responseCalender = calenderService.findCalendersByMemberId(start,end,memberId);
        return ResponseEntity.ok(responseCalender);
    }
    @GetMapping("/{memberId}")
    public ResponseEntity<List<ResponseCalender>> findCalendersByMemberId(@RequestParam(name = "start")LocalDate start, @RequestParam(name = "end") LocalDate end, @PathVariable(name = "memberId") Long userId){

        if(start.isAfter(end)){
            throw new StartIsAfterEndDateException();
        }

        List<ResponseCalender> responseCalender = calenderService.findCalendersByMemberId(start,end,userId);

        return ResponseEntity.ok(responseCalender);
    }
    @GetMapping("/detail")
    public ResponseEntity<ResponseCalenderDetail> findCalenderDetail(@RequestParam("date") LocalDate date) {
        Long memberId = 1L;
        ResponseCalenderDetail responseCalenderDetail = calenderService.findCalenderDetailByMemberId(date,memberId);

        return ResponseEntity.ok(responseCalenderDetail);
    }
    @GetMapping("/detail/{memberId}")
    public ResponseEntity<ResponseCalenderDetail> findCalenderDetailByMemberId(@PathVariable Long memberId, @RequestParam("date")LocalDate date){
        ResponseCalenderDetail responseCalenderDetail = calenderService.findCalenderDetailByMemberId(date,memberId);

        return ResponseEntity.ok(responseCalenderDetail);
    }
}
