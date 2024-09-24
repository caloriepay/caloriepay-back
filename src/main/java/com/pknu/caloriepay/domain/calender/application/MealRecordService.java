package com.pknu.caloriepay.domain.calender.application;

import com.pknu.caloriepay.domain.calender.dao.MealRecordRepository;
import com.pknu.caloriepay.domain.calender.domain.MealRecord;
import com.pknu.caloriepay.domain.calender.dto.ResponseMealRecordDataDto;
import com.pknu.caloriepay.domain.calender.dto.ResponseMealRecordDetailDto;
import com.pknu.caloriepay.domain.calender.dto.ResponseMealRecordDto;
import com.pknu.caloriepay.domain.food.dao.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealRecordService {

    private final MealRecordDetailService mealRecordDetailService;
    private final MealRecordRepository mealRecordRepository;
    public List<ResponseMealRecordDto> findMealRecordData(Long memberId, LocalDate date){
        List<MealRecord> mealRecords= mealRecordRepository.findAllByMemberIdAndAndDate(memberId, date);

        return mealRecords.stream()
                .map(mealRecord ->
                        ResponseMealRecordDto.of(mealRecordDetailService.findMealDetailByRecordId(mealRecord.getId()),mealRecord)
                ).collect(Collectors.toList());
    }
}


