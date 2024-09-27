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
public class MemberCredentials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_credentials_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String encodedPassword;

    // MemberCredentials 생성 메서드
    public static MemberCredentials createCredentials(Member member, String encodedPassword) {
        return MemberCredentials.builder()
                .member(member)  // Member를 참조
                .encodedPassword(encodedPassword)
                .build();
    }


}
