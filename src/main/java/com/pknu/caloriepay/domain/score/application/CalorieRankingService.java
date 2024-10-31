package com.pknu.caloriepay.domain.score.application;


import com.pknu.caloriepay.domain.score.dao.CalorieScoreRepository;
import com.pknu.caloriepay.domain.score.domain.CalorieScore;
import com.pknu.caloriepay.domain.score.dto.out.ResponseCalorieScoreRankingDto;
import com.pknu.caloriepay.domain.user.dao.MemberRepository;
import com.pknu.caloriepay.domain.user.domain.Member;
import com.pknu.caloriepay.global.enums.ResCode;
import com.pknu.caloriepay.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static com.pknu.caloriepay.global.config.cache.RedisCacheConfig.RANK_CACHE;

@Service
@RequiredArgsConstructor
public class CalorieRankingService {

    private final CalorieScoreRepository calorieScoreRepository;
    private final MemberRepository memberRepository;

    @Cacheable(value = RANK_CACHE,key = "'latest'",cacheManager = "caloriePayCacheManager")
    @Transactional(readOnly = true)
    public List<ResponseCalorieScoreRankingDto> getLatestScoreRanking() {
        List<CalorieScore> calorieScores = calorieScoreRepository.findLatestScoresByUserOrderByScoreDesc(PageRequest.of(0, 100));
//      100등까지의 리스트만 뽑아옴
        return IntStream.rangeClosed(1, calorieScores.size())
                .mapToObj(i -> {
                    CalorieScore calorieScore = calorieScores.get(i - 1);
                    Member member = memberRepository.findById(calorieScore.getUserId()).orElseThrow(() ->new CustomException(ResCode.USER_NOT_FOUND));
                    return ResponseCalorieScoreRankingDto.of(calorieScore, member, (long) i);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public ResponseCalorieScoreRankingDto findUserRankingByUserId(Long userId){

        Map<String , Object> calorieScoreMap = calorieScoreRepository.findUserRankingByUserId(userId);
        Member member = memberRepository.findById(userId).orElseThrow(() ->new CustomException(ResCode.USER_NOT_FOUND));
        long countMember = memberRepository.count();

        CalorieScore calorieScore= (CalorieScore) calorieScoreMap.get("calorieScore");
        Long ranking = (long) calorieScoreMap.get("ranking");


        int perRank = (int) ((double) ranking / countMember * 100);
        ResponseCalorieScoreRankingDto responseCalorieScoreRankingDto=ResponseCalorieScoreRankingDto.of(calorieScore,member, ranking);
        responseCalorieScoreRankingDto.updatePerRank(perRank);

        return responseCalorieScoreRankingDto;
    }


}
