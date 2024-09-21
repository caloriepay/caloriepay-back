package com.pknu.caloriepay.calender.repository;

import com.pknu.caloriepay.domain.Calender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalenderRepository extends JpaRepository<Calender, Long>,CalenderCustomRepository {
}
