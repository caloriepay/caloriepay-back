package com.pknu.caloriepay.calender.repository;

import com.pknu.caloriepay.domain.Calender;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CalenderCustomRepository {

    public List<Calender> findAllByMemberIdAndDate(Long memberId, LocalDate start, LocalDate end);
}
