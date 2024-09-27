package com.pknu.caloriepay.domain.user.dao;

import com.pknu.caloriepay.domain.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);// 이메일 중복체크
}
