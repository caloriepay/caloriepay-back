package com.pknu.caloriepay.domain.user.application;

import com.pknu.caloriepay.domain.user.dao.MemberCredentialsRepository;
import com.pknu.caloriepay.domain.user.dao.MemberRepository;
import com.pknu.caloriepay.domain.user.domain.JoinType;
import com.pknu.caloriepay.domain.user.domain.Member;
import com.pknu.caloriepay.domain.user.domain.MemberCredentials;
import com.pknu.caloriepay.domain.user.domain.Preferences;
import com.pknu.caloriepay.domain.user.dto.JoinRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberJoinService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberCredentialsRepository memberCredentialsRepository;

    @Transactional
    public Member joinMember(JoinRequestDto joinRequestDto) {
        if (memberRepository.findByEmail(joinRequestDto.getEmail()).isPresent()) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(joinRequestDto.getPassword());

        Preferences preferences = new Preferences(true, JoinType.EMAIL);

        Member member = Member.builder()
                .name(joinRequestDto.getName())
                .email(joinRequestDto.getEmail())
                .phoneNumber(joinRequestDto.getPhoneNumber())
                .nickname(joinRequestDto.getNickname())
                .preferences(preferences)
                .build();

        memberRepository.save(member);

        // MemberCredentials 생성 및 저장 (Member를 참조)
        MemberCredentials credentials = MemberCredentials.createCredentials(member, encodedPassword);
        memberCredentialsRepository.save(credentials);

        return member;
    }
}
