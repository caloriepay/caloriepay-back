package com.pknu.caloriepay.domain.user.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class Profile {
    private Gender gender;
    private int age;
    private double height;
    private double weight;
    private Goal goal;
    private double targetWeight;
    private ActivityLevel activityLevel;

}
