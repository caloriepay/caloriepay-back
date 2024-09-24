package com.pknu.caloriepay.domain.calender.application;

import com.pknu.caloriepay.domain.calender.dao.MealRecordDetailRepository;
import com.pknu.caloriepay.domain.calender.dto.ResponseMealRecordDetailDto;
import com.pknu.caloriepay.domain.calender.dto.ResponseMealRecordDetailWithFoodDto;
import com.pknu.caloriepay.domain.calender.dto.ResponseMealRecordDto;
import com.pknu.caloriepay.domain.food.dao.FoodRepository;
import com.pknu.caloriepay.domain.food.domain.Food;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealRecordDetailService {

    private final MealRecordDetailRepository mealRecordDetailRepository;
    private final FoodRepository foodRepository;


    public void createMealDetail(){}
    public List<ResponseMealRecordDetailWithFoodDto> findMealDetailByRecordId(Long mealRecordId){

        List<ResponseMealRecordDetailDto> mealDto = mealRecordDetailRepository.findAllByMealRecord_Id(mealRecordId).stream()
                .map(ResponseMealRecordDetailDto::from)
                .collect(Collectors.toList());

        return mealDto.stream()
                .map(meal -> {
            Food food =foodRepository.findById(meal.getFoodId()).orElseThrow();
            return ResponseMealRecordDetailWithFoodDto.of(meal,food);
        }).collect(Collectors.toList());
    }
}
