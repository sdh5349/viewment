package com.web.curation.recommend.recommender;

import com.web.curation.domain.User;
import com.web.curation.domain.article.Article;
import com.web.curation.domain.connection.Likes;
import com.web.curation.exceptions.UserNotFoundException;
import com.web.curation.recommend.analyzer.Analyzer;
import com.web.curation.recommend.analyzer.TupleUserArticle;
import com.web.curation.repository.like.LikeRepository;
import com.web.curation.repository.user.UserRepository;
import com.web.curation.service.article.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ArticleRecommenderImpl implements ArticleRecommender{

    private final LikeRepository likeRepository;
    private final Analyzer analyzer;

    @Override
    public Set<Long> recommend(User user) {

        List<Long> input = likeRepository.findByUser(user).stream().map(like -> {
            return like.getArticle().getArticleId();
        }).collect(Collectors.toList());


        //데이터셋 전처리
        //todo 데이터셋을 다른 장소에 저장
        List<TupleUserArticle> list = likeRepository.findAllToTuple();

        List<List<Long>> dataset = list.stream()
                .collect(Collectors.groupingBy(TupleUserArticle::getUserId))
                .values().stream()
                .map(li -> {
                    return li.stream().map(tuple -> {
                        return tuple.getArticleId();
                    })
                            .collect(Collectors.toList());
                })
                .collect(Collectors.toList());


        Set<Long> result = analyzer.analysis(input, dataset);

        return result;
    }
}
