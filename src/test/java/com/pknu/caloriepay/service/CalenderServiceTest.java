package com.pknu.caloriepay.service;


import com.pknu.caloriepay.calender.dto.CalenderDto;
import com.pknu.caloriepay.calender.repository.CalenderRepository;
import com.pknu.caloriepay.calender.service.CalenderService;
import com.pknu.caloriepay.calender.vo.ResponseCalender;
import com.pknu.caloriepay.domain.Calender;
import com.pknu.caloriepay.domain.Member;
import com.pknu.caloriepay.domain.Tier;
import com.pknu.caloriepay.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CalenderServiceTest {

    @InjectMocks
    private CalenderService calenderService;
    @Mock
    private CalenderRepository calenderRepository;
    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("[findCalendersByMemberId]: 캘린더를 성공적으로 가져오는경우")
    public void successFindCalender(){
        Member member = Member.builder()
                .id(1L)
                .name("홍길동")
                .build();

        List<Calender> calenderList = new ArrayList<>();

        Calender calender1 = Calender.builder()
                .id(1L)
                .memberId(100L)
                .date(LocalDate.of(2024, 1, 1))
                .tier(Tier.S)
                .build();

        Calender calender2 = Calender.builder()
                .id(2L)
                .memberId(101L)
                .date(LocalDate.of(2024, 1, 2))
                .tier(Tier.S)
                .build();

        calenderList.add(calender1);
        calenderList.add(calender2);

        //given
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));
        given(calenderRepository.findAllByMemberIdAndDate(1L, LocalDate.now(),LocalDate.now().plusDays(1))).willReturn(calenderList);
        //when
        List<ResponseCalender> responseCalenders = calenderService.findCalendersByMemberId(LocalDate.now(),LocalDate.now().plusDays(1),1L);
        //Then
        verify(memberRepository,times(1)).findById(1L);
        verify(calenderRepository,times(1)).findAllByMemberIdAndDate(1L, LocalDate.now(),LocalDate.now().plusDays(1));
        assertThat(responseCalenders).isEqualTo(
                calenderList.stream()
                        .map(CalenderDto::of)
                        .map(ResponseCalender::of)
                        .toList()
        );
    }

    @Test
    @DisplayName("[findCalendersByMemberId]: 전달받은 memberId가 member내에 존재하지않음")
    public void failFindCalender(){

        //given, when
        given(memberRepository.findById(10L)).willReturn(Optional.empty());
        //when
        assertThrows(RuntimeException.class, () ->
                calenderService.findCalendersByMemberId(LocalDate.now(),LocalDate.now().plusDays(1),10L));
        //Then
        verify(memberRepository,times(1)).findById(10L);
        verify(calenderRepository,times(0)).findAllByMemberIdAndDate(1L, LocalDate.now(),LocalDate.now().plusDays(1));

    }

}
