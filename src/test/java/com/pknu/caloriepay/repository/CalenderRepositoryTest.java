package com.pknu.caloriepay.repository;

import com.pknu.caloriepay.calender.repository.CalenderRepository;
import com.pknu.caloriepay.domain.Calender;
import com.pknu.caloriepay.domain.Tier;
import com.pknu.caloriepay.global.config.QueryDSLConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDSLConfig.class})
public class CalenderRepositoryTest {

    @Autowired
    private CalenderRepository calenderRepository;
    @BeforeEach
    void setup(){
        for(Long i=1L; i<5L ; i++) {
            calenderRepository.save(Calender.builder()
                    .memberId(1L)
                    .tier(Tier.S)
                    .date(LocalDate.now().plusDays(i)).build()
            );
        }

        for(Long i=6L; i<10L ; i++) {
            calenderRepository.save(Calender.builder()
                    .memberId(i)
                    .tier(Tier.S)
                    .date(LocalDate.now()).build()
            );
        }
    }
    @Test
    @DisplayName("[findAllByMemberIdAndDate] 함수 테스트")
    public void findAllByMemberIdAndDateTest(){
//        member1의값 전부 조회
        List<Calender> calenderList1 = calenderRepository.findAllByMemberIdAndDate(1L,LocalDate.now(),LocalDate.now().plusDays(7));
//        member3의 값 전부조회 -> member3 없음
        List<Calender> calenderList2 = calenderRepository.findAllByMemberIdAndDate(3L,LocalDate.now(),LocalDate.now().plusDays(7));
//        member8의 값 전부 조회 -> 1개
        List<Calender> calenderList3 = calenderRepository.findAllByMemberIdAndDate(8L,LocalDate.now(),LocalDate.now().plusDays(7));
//      member1의 값 현재부터 3일뒤까지 조회 ->4개
        List<Calender> calenderList4 = calenderRepository.findAllByMemberIdAndDate(1L,LocalDate.now(),LocalDate.now().plusDays(3));

        assertThat(calenderList1).hasSize(4);
        assertThat(calenderList2).isEmpty();
        assertThat(calenderList3).hasSize(1);
        assertThat(calenderList4).hasSize(3);
    }


}
