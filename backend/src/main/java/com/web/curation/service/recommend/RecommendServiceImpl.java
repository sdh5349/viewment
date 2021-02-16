package com.web.curation.service.recommend;

import com.web.curation.domain.User;
import com.web.curation.domain.article.Article;
import com.web.curation.dto.article.ArticleFeedDto;
import com.web.curation.exceptions.UserNotFoundException;
import com.web.curation.recommend.recommender.ArticleRecommender;
import com.web.curation.repository.article.ArticleRepository;
import com.web.curation.repository.follow.FollowRepository;
import com.web.curation.repository.like.LikeRepository;
import com.web.curation.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Collections;
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
    private final FollowRepository followRepository;

    @Override
    public Page<ArticleFeedDto> recommendArticle(String userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(()->{
            throw new UserNotFoundException();
        });

        Set<Long> recommend = articleRecommender.recommend(user);

        List<Article> articles = articleRepository.findByArticleIdIn(recommend);
        List<ArticleFeedDto> result = articles.stream()
                .filter(article -> !article.getUser().getId().equals(userId))
                .map(article -> {
                    ArticleFeedDto dto = new ArticleFeedDto(article);

                    int likes = likeRepository.countByArticle(article).intValue();
                    boolean liked = likeRepository.existsByUserAndArticle(user, article);
                    dto.setLikes(likes);
                    dto.setLiked(liked);
                    dto.setRecommended(true);
                    return dto;
                })
                .collect(Collectors.toList());


        List<User> followings = followRepository.findByFrom(user).stream()
                .map(follow -> {
                    return follow.getTo();
                })
                .collect(Collectors.toList());

        List<ArticleFeedDto> followingsArticles = articleRepository.findByUserIn(followings).stream()
                .map(article -> {
                    ArticleFeedDto dto = new ArticleFeedDto(article);
                    int likes = likeRepository.countByArticle(article).intValue();
                    boolean liked = likeRepository.existsByUserAndArticle(user, article);
                    dto.setLikes(likes);
                    dto.setLiked(liked);
                    dto.setRecommended(false);
                    return dto;
                })
                .collect(Collectors.toList());

        int s = pageable.getPageNumber();
        int e = pageable.getPageSize();

        int total = result.size() + followingsArticles.size();

        result.addAll(followingsArticles);

        Collections.sort(result, (a, b)->Timestamp.valueOf(b.getWdate()).compareTo(Timestamp.valueOf(a.getWdate())));

        int fromIdx = s*e<result.size() ? (s*e) : result.size();
        int toIdx = (s*e + e)<result.size() ? (s*e+e) : result.size();

        result = result.subList(fromIdx, toIdx);

        return new PageImpl<ArticleFeedDto> (result, pageable, total) ;
    }
}
