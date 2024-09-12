package com.pknu.caloriepay.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pknu.caloriepay.calender.controller.CalenderController;
import com.pknu.caloriepay.calender.service.CalenderService;
import com.pknu.caloriepay.calender.vo.ResponseCalender;
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

        given(calenderService.findCalenders(any(),any())).willReturn(responseCalender);

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
                    assertThat(responseDto.getMesssage()).isEqualTo(ErrorCode.NOT_ALLOWED_DATE_FORMAT.getMessage());
                })
                .andDo(print());
//        calenderservice.getCalender 0번실행
        verify(calenderService, times(0)).findCalenders(any(),any());
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
                    assertThat(responseDto.getMesssage()).isEqualTo(ErrorCode.START_IS_AFTER_END.getMessage());
                })
                .andDo(print());
        verify(calenderService, times(0)).findCalenders(any(),any());
    }

    @Test
    @DisplayName("캘린더를 성공적으로 가져온 경우 - userId 를통해")
    public void successGetCalenderWithUserId(){}

    @Test
    @DisplayName("캘린더를 가져올때 파라미터의 날짜가 잘못된 경우 - userId 통해")
    public void failGetCalenderWithNotAllowedDateAndUserId(){}

    @Test
    @DisplayName("캘린더를 가져올때 파라미터의 시작날짜가 종료날짜보다 느린경우 - userId통해")
    public void failGetCalenderWhenEndEarlyStartAndUserId(){

    }

    @Test
    @DisplayName("캘린더 상세보기를 성공적으로 가져온경우")
    public void successGetCalenderDetail(){

    }

    @Test
    @DisplayName("캘린더 상세보기를 가져올때 날짜형식이 잘못된 경우")
    public void failGetCalenderDetail(){

    }


    @Test
    @DisplayName("캘린더 상세보기를 성공적으로 가져온경우 - userId를 통해" )
    public void successGetCalenderDetailWithUserId(){}

    @Test
    @DisplayName("캘린더 상세보기를 가져올때 날짜형식이 잘못된 경우 - userId를 통해")
    public void failGetCalenderDetailWithUserId(){
    }

}
