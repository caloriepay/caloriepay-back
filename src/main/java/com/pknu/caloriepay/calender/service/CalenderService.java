package com.pknu.caloriepay.calender.service;

import com.pknu.caloriepay.calender.dto.CalenderDto;
import com.pknu.caloriepay.calender.repository.CalenderRepository;
import com.pknu.caloriepay.calender.vo.ResponseCalender;
import com.pknu.caloriepay.calender.vo.ResponseCalenderDetail;
import com.pknu.caloriepay.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalenderService
{
    private final MemberRepository memberRepository;
    private final CalenderRepository calenderRepository;

    public List<ResponseCalender> findCalendersByMemberId(LocalDate start, LocalDate end,Long memberId) {
//추후 UserNotFoundException 으로 고쳐야할듯?
        memberRepository.findById(memberId).orElseThrow(RuntimeException::new);

        List<CalenderDto> calenderDto = calenderRepository.findAllByMemberIdAndDate(memberId,start,end).stream()
                .map(CalenderDto::of)
                .toList();

        return calenderDto.stream()
                .map(ResponseCalender::of)
                .toList();
    }

    public ResponseCalenderDetail findCalenderDetailByMemberId(LocalDate date, Long memberId) {

        memberRepository.findById(memberId).orElseThrow(RuntimeException::new);


        return null;
    }
}
