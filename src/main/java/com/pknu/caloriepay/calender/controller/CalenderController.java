package com.pknu.caloriepay.calender.controller;

import com.pknu.caloriepay.calender.exception.StartIsAfterEndDateException;
import com.pknu.caloriepay.calender.service.CalenderService;
import com.pknu.caloriepay.calender.vo.ResponseCalender;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

        List<ResponseCalender> responseCalender = calenderService.findCalenders(start,end);
        return ResponseEntity.ok(responseCalender);
    }


}
