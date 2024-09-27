package com.pknu.caloriepay.domain.user.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class Preferences {
    // 정보 공개 여부
    private boolean visible;
    // 가입 유형
    private JoinType joinType;
    // 회원 정보 등록 여부
    private boolean isProfileRegistered;


    protected Preferences() {}

    public Preferences(boolean visible, JoinType joinType) {
        this.visible = visible;
        this.joinType = joinType;
        this.isProfileRegistered = false;
    }

    public void registerProfile(){
        this.isProfileRegistered = true;
    }
}
