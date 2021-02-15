package com.web.curation.service.recommend;

import com.web.curation.domain.User;
import com.web.curation.domain.article.Article;
import com.web.curation.dto.article.ArticleFeedDto;
import com.web.curation.dto.user.SimpleUserInfoDto;
import com.web.curation.exceptions.UserNotFoundException;
import com.web.curation.recommend.recommender.ArticleRecommender;
import com.web.curation.repository.article.ArticleRepository;
import com.web.curation.repository.like.LikeRepository;
import com.web.curation.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * com.web.curation.service.recommend
 * RecommendServiceImpl.java
 * @date    2021-02-04 오후 8:23
 * @author  김종성
 *
 * @변경이력
 **/

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService{

    private final ArticleRecommender articleRecommender;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final LikeRepository likeRepository;

    @Override
    public List<ArticleFeedDto> recommendArticle(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()->{
            throw new UserNotFoundException();
        });

        Set<Long> recommend = articleRecommender.recommend(user);

        //3일간의 최신 게시글 추천 목록에 추가
        long time = System.currentTimeMillis()-1000L*60L*60L*24L*3L;
        Timestamp t = new Timestamp(time);

        Set<Long> articlesLatest = articleRepository.findByWdateAfter(t).stream()
                .map(article -> {return article.getArticleId();})
                .collect(Collectors.toSet());

        recommend.addAll(articlesLatest);

        List<Article> articles = articleRepository.findByArticleIdIn(recommend);

        List<ArticleFeedDto> result = articles.stream()
                .filter(article -> !article.getUser().getId().equals(userId))
                .map(article -> {
                    ArticleFeedDto dto = new ArticleFeedDto(article);

                    int likes = likeRepository.countByArticle(article).intValue();
                    boolean liked = likeRepository.existsByUserAndArticle(user, article);
                    dto.setLikes(likes);
                    dto.setLiked(liked);

                    return dto;
                })
                .collect(Collectors.toList());

        return result;
    }

    @Override
    public List<SimpleUserInfoDto> recommendUser(String userId) {
        return null;
    }
}
