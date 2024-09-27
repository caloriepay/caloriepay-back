package com.pknu.caloriepay.domain.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;
    private String email;
    private String phoneNumber;
    private String nickname;

    @Embedded
    private Profile profile;

    @Embedded
    private Preferences preferences;

    // Member 엔티티의 생성 메서드
    public static Member createMember(String name, String email, String phoneNumber, String nickname, Profile profile, Preferences preferences) {
        return Member.builder()
                .name(name)
                .email(email)
                .phoneNumber(phoneNumber)
                .nickname(nickname)
                .preferences(preferences)
                .build();
    }



}
