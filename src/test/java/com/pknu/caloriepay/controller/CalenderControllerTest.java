package com.pknu.caloriepay.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pknu.caloriepay.calender.controller.CalenderController;
import com.pknu.caloriepay.calender.service.CalenderService;
import com.pknu.caloriepay.calender.vo.ResponseCalender;
import com.pknu.caloriepay.calender.vo.ResponseCalenderDetail;
import com.pknu.caloriepay.domain.Tier;
import com.pknu.caloriepay.global.error.ErrorCode;
import com.pknu.caloriepay.global.error.ErrorResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalenderController.class)
public class CalenderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CalenderService calenderService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("캘린더를 성공적으로 가져온 경우")
    public void successGetCalender() throws Exception {

        List<ResponseCalender> responseCalender = Arrays.asList(
                new ResponseCalender(1L, 1L, LocalDate.now(), Tier.S),
                new ResponseCalender(2L, 1L, LocalDate.now().plusDays(1), Tier.S)
        );

        given(calenderService.findCalendersByMemberId(any(),any(),any())).willReturn(responseCalender);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/calender")
                                .param("start",LocalDate.now().toString())
                                .param("end",LocalDate.now().plusDays(1).toString())
                )
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().json(objectMapper.writeValueAsString(responseCalender)))
                .andDo(print());
    }

    @Test
    @DisplayName("캘린더를 가져올때 파라미터의 날짜가 잘못된 경우")
    public void failGetCalenderWithNotAllowedDate() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/calender")
                                .param("start","SDF")
                                .param("end","12342")
                )
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(result -> {
                    ErrorResponseDto responseDto = objectMapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), ErrorResponseDto.class);
                    assertThat(responseDto.getMessage()).isEqualTo(ErrorCode.NOT_ALLOWED_DATE_FORMAT.getMessage());
                })
                .andDo(print());
//        calenderservice.getCalender 0번실행
        verify(calenderService, times(0)).findCalendersByMemberId(any(),any(),any());
    }

    @Test
    @DisplayName("캘린더를 가져올때 파라미터의 시작날짜가 종료날짜보다 느린경우")
    public void failGetCalenderWhenEndEarlyStart() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/calender")
                                .param("start",LocalDate.now().plusDays(1).toString())
                                .param("end",LocalDate.now().toString())
                )
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(result -> {
                    ErrorResponseDto responseDto = objectMapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), ErrorResponseDto.class);
                    assertThat(responseDto.getMessage()).isEqualTo(ErrorCode.START_IS_AFTER_END.getMessage());
                })
                .andDo(print());
        verify(calenderService, times(0)).findCalendersByMemberId(any(),any(),any());
    }

    @Test
    @DisplayName("캘린더를 성공적으로 가져온 경우 - userId 를통해")
    public void successGetCalenderWithUserId() throws Exception {

        List<ResponseCalender> responseCalender = Arrays.asList(
                new ResponseCalender(1L, 1L, LocalDate.now(), Tier.S),
                new ResponseCalender(2L, 1L, LocalDate.now().plusDays(1), Tier.S)
        );

        given(calenderService.findCalendersByMemberId(any(),any(),any())).willReturn(responseCalender);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/calender/1")
                                .param("start",LocalDate.now().toString())
                                .param("end",LocalDate.now().plusDays(1).toString())
                )
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().json(objectMapper.writeValueAsString(responseCalender)))
                .andDo(print());
    }

    @Test
    @DisplayName("캘린더를 가져올때 파라미터의 날짜가 잘못된 경우 - userId 통해")
    public void failGetCalenderWithNotAllowedDateAndUserId() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/calender/1")
                                .param("start","SDF")
                                .param("end","12342")
                )
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(result -> {
                    ErrorResponseDto responseDto = objectMapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), ErrorResponseDto.class);
                    assertThat(responseDto.getMessage()).isEqualTo(ErrorCode.NOT_ALLOWED_DATE_FORMAT.getMessage());
                })
                .andDo(print());
//        calenderservice.getCalender 0번실행
        verify(calenderService, times(0)).findCalendersByMemberId(any(),any(),any());

    }

    @Test
    @DisplayName("캘린더를 가져올때 파라미터의 시작날짜가 종료날짜보다 느린경우 - userId통해")
    public void failGetCalenderWhenEndEarlyStartAndUserId() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/calender/1")
                                .param("start",LocalDate.now().plusDays(1).toString())
                                .param("end",LocalDate.now().toString())
                )
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(result -> {
                    ErrorResponseDto responseDto = objectMapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), ErrorResponseDto.class);
                    assertThat(responseDto.getMessage()).isEqualTo(ErrorCode.START_IS_AFTER_END.getMessage());
                })
                .andDo(print());
        verify(calenderService, times(0)).findCalendersByMemberId(any(),any(),any());

    }

    @Test
    @DisplayName("캘린더 상세보기를 성공적으로 가져온경우")
    public void successGetCalenderDetail() throws Exception {

        ResponseCalenderDetail.ResponseSpend spend = ResponseCalenderDetail.ResponseSpend.builder()
                .dateTime(new Timestamp(System.currentTimeMillis()))
                .data(List.of(new ResponseCalenderDetail.SpendData(1L, 1L, 500, "사과")
                ,new ResponseCalenderDetail.SpendData(2L, 1L, 500, "비빔냉면")))
                .build();

        ResponseCalenderDetail.ResponseEarn earn = ResponseCalenderDetail.ResponseEarn.builder()
                .title("채원이랑 헬스장")
                .data(List.of(new ResponseCalenderDetail.EarnData(1L, 1L, 300, "러닝", 30)
                ,new ResponseCalenderDetail.EarnData(2L, 1L, 300, "웨이트", 30)))
                .build();

        ResponseCalenderDetail responseCalenderDetail = ResponseCalenderDetail.builder()
                .spend(spend)
                .earn(earn)
                .build();


        given(calenderService.findCalenderDetailByMemberId(any(),any())).willReturn(responseCalenderDetail);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/calender/detail")
                                .param("date",LocalDate.now().toString())
                )
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().json(objectMapper.writeValueAsString(responseCalenderDetail)))
                .andDo(print());

    }

    @Test
    @DisplayName("캘린더 상세보기를 가져올때 날짜형식이 잘못된 경우")
    public void failGetCalenderDetail() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/calender/detail")
                                .param("date","1234")
                )
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(result -> {
                    ErrorResponseDto responseDto = objectMapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), ErrorResponseDto.class);
                    assertThat(responseDto.getMessage()).isEqualTo(ErrorCode.NOT_ALLOWED_DATE_FORMAT.getMessage());
                })
                .andDo(print());
        verify(calenderService, times(0)).findCalenderDetailByMemberId(any(),any());
    }


    @Test
    @DisplayName("캘린더 상세보기를 성공적으로 가져온경우 - userId를 통해" )
    public void successGetCalenderDetailWithUserId() throws Exception {
        ResponseCalenderDetail.ResponseSpend spend = ResponseCalenderDetail.ResponseSpend.builder()
                .dateTime(new Timestamp(System.currentTimeMillis()))
                .data(List.of(new ResponseCalenderDetail.SpendData(1L, 1L, 500, "사과")
                        ,new ResponseCalenderDetail.SpendData(2L, 1L, 500, "비빔냉면")))
                .build();

        ResponseCalenderDetail.ResponseEarn earn = ResponseCalenderDetail.ResponseEarn.builder()
                .title("채원이랑 헬스장")
                .data(List.of(new ResponseCalenderDetail.EarnData(1L, 1L, 300, "러닝", 30)
                        ,new ResponseCalenderDetail.EarnData(2L, 1L, 300, "웨이트", 30)))
                .build();

        ResponseCalenderDetail responseCalenderDetail = ResponseCalenderDetail.builder()
                .spend(spend)
                .earn(earn)
                .build();


        given(calenderService.findCalenderDetailByMemberId(any(),any())).willReturn(responseCalenderDetail);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/calender/detail/1")
                                .param("date",LocalDate.now().toString())
                )
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().json(objectMapper.writeValueAsString(responseCalenderDetail)))
                .andDo(print());

    }

    @Test
    @DisplayName("캘린더 상세보기를 가져올때 날짜형식이 잘못된 경우 - userId를 통해")
    public void failGetCalenderDetailWithUserId() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/calender/detail")
                                .param("date","1234")
                )
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(result -> {
                    ErrorResponseDto responseDto = objectMapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), ErrorResponseDto.class);
                    assertThat(responseDto.getMessage()).isEqualTo(ErrorCode.NOT_ALLOWED_DATE_FORMAT.getMessage());
                })
                .andDo(print());
        verify(calenderService, times(0)).findCalenderDetailByMemberId(any(),any());
    }

}
