package com.pknu.caloriepay.domain.user.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class Preferences {
    // 정보 공개 여부
    private boolean visible;

    // 가입 유형
    private JoinType joinType;
}
