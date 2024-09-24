package com.pknu.caloriepay.domain.calender.application;

import com.pknu.caloriepay.domain.calender.dao.CalenderRepository;
import com.pknu.caloriepay.domain.calender.dto.CalenderDto;
import com.pknu.caloriepay.domain.calender.vo.ResponseCalender;
import com.pknu.caloriepay.domain.calender.vo.ResponseCalenderDetail;
import com.pknu.caloriepay.domain.calender.dao.MealRecordRepository;
import com.pknu.caloriepay.domain.exercise.dao.ExerciseRepository;
import com.pknu.caloriepay.domain.food.dao.FoodRepository;
import com.pknu.caloriepay.domain.spend.dao.SpendRepository;
import com.pknu.caloriepay.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CalenderService
{
    private final MemberRepository memberRepository;
    private final CalenderRepository calenderRepository;
    private final MealRecordRepository earnRepository;
    private final ExerciseRepository exerciseRepository;
    private final SpendRepository spendRepository;
    private final FoodRepository foodRepository;

    public List<ResponseCalender> findCalendersByMemberId(LocalDate start, LocalDate end,Long memberId) {
//추후 UserNotFoundException 으로 고쳐야할듯?
        memberRepository.findById(memberId).orElseThrow(RuntimeException::new);

        List<CalenderDto> calenderDto = calenderRepository.findAllByMemberIdAndDate(memberId,start,end).stream()
                .map(CalenderDto::from)
                .toList();

        return calenderDto.stream()
                .map(ResponseCalender::from)
                .toList();
    }

//    public ResponseCalenderDetail findCalenderDetailByMemberId(LocalDate date, Long memberId) {

//        memberRepository.findById(memberId).orElseThrow(RuntimeException::new);
//
//        // spend data 가져오기
//        List<ResponseSpend> responseSpendDtoList = getSpendData(memberId, date);
//
//        // earn data 가져오기
//        Pair<String,List<EarnData>> earnData = getTitleAndEarnData(memberId, date).orElse(Pair.of("", Collections.emptyList()));
//
//        return ResponseCalenderDetail.of(earnData.getFirst(), earnData.getSecond(), responseSpendDtoList);

    }

//    // spend data 처리
//    private List<ResponseSpend> getSpendData(Long memberId, LocalDate date) {
//        List<SpendDto> spendList = spendRepository.findAllByMemberIdAndDate(memberId, date)
//                .stream()
//                .map(SpendDto::from)
//                .toList();
//
//        HashMap<LocalDateTime, List<MealRecordDataDto>> spendMap = spendList.stream()
//                .collect(Collectors.groupingBy(
//                        SpendDto::getCreatedAt,
//                        HashMap::new,
//                        Collectors.mapping(spendDto -> {
//                            Food food = foodRepository.findById(spendDto.getFoodId()).orElseThrow();
//                            return MealRecordDataDto.of(food, spendDto);
//                        }, Collectors.toList())
//                ));
//
//        return spendMap.entrySet().stream()
//                .map(map -> ResponseSpend.builder()
//                        .dateTime(map.getKey())
//                        .data(map.getValue())
//                        .build())
//                .toList();
//    }
//
//    // earn data 처리
//    private Optional<Pair<String,List<EarnData>>> getTitleAndEarnData(Long memberId, LocalDate date) {
//
//        List<Earn> earns = earnRepository.findAllByMemberIdAndAndDate(memberId, date);
//        if (earns.isEmpty()) {return Optional.empty();}
//        log.info(earns.toString());
//
//        String title = Objects.requireNonNull(earns).get(0).getTitle(); // Assuming there is at least one earn entry
//
//        return Optional.of(Pair.of(title, earns.stream()
//                .map(earnData -> EarnData.of(earnData, exerciseRepository.findById(earnData.getExerciseId()).orElseThrow().getType()))
//                .toList()));
//    }
}
