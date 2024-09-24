package com.pknu.caloriepay.domain.food.dao;

import com.pknu.caloriepay.domain.food.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food,Long> {
}
