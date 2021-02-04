package com.web.curation.recommend.analyzer;

import com.web.curation.domain.User;
import com.web.curation.domain.connection.Likes;
import com.web.curation.recommend.algorithm.FPGrowth;
import com.web.curation.recommend.algorithm.Rule;
import com.web.curation.repository.like.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FpgAnalyzer implements Analyzer{

    @Override
    public <T> Set<T> analysis(List<T> input, List<List<T>> dataset) {

        //todo 마이닝한 결과를 저장하고 불러오는 방식으로 변경
        FPGrowth fpGrowth = new FPGrowth<T>(dataset.size()/10);
        fpGrowth.setGlobalTree(dataset);
        fpGrowth.mine();

        Map allRules = fpGrowth.getAllRules();
        /////////////////////
        
        Set<T> set = new HashSet<>();
        for(T item : input){
            if(!allRules.containsKey(item)) continue;
            List<Rule<T>> rules = (List<Rule<T>>)allRules.get(item);

            rules.sort((a, b)->Double.compare(b.lift, a.lift));

            for(int i = 0; i<rules.size()/3+1; i++){
                T t = rules.get(i).B;
                if(!input.contains(t))
                    set.add(rules.get(i).B);
            }
        }

        return set;
    }
}
