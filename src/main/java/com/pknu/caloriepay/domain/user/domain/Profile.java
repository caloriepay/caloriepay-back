package com.pknu.caloriepay.domain.user.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class Profile {
    private String gender;
    private int age;
    private double height;
    private double weight;
    private Target target;
    private ActivityLevel activityLevel;

}
