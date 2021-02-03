package com.web.curation.service.article;

import com.web.curation.domain.Pin;
import com.web.curation.domain.User;
import com.web.curation.domain.article.Article;
import com.web.curation.domain.connection.Follow;
import com.web.curation.domain.connection.Likes;
import com.web.curation.dto.user.SimpleUserInfoDto;
import com.web.curation.domain.article.ArticleImage;
import com.web.curation.dto.article.ArticleSimpleDto;
import com.web.curation.exceptions.ElementNotFoundException;
import com.web.curation.exceptions.UserNotFoundException;
import com.web.curation.domain.hashtag.Hashtag;
import com.web.curation.dto.article.ArticleDto;
import com.web.curation.dto.article.ArticleInfoDto;
import com.web.curation.repository.follow.FollowRepository;
import com.web.curation.repository.like.LikeRepository;
import com.web.curation.repository.user.UserRepository;
import com.web.curation.repository.article.ArticleRepository;
import com.web.curation.repository.hashtag.HashtagRepository;
import com.web.curation.repository.image.ImageRepository;
import com.web.curation.repository.pin.PinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * com.web.curation.service.article
 * ArticleService.java
 *
 * @author 이주희
 * @date 2021-01-26 오전 11:02
 * @변경이력 김종성: PinRepository 수정으로 관련된 부분 일부 수정(추후 리팩토링 필수!!)
 * @변경이력 김종성: like, unlike 기능 추가
 **/

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final UserRepository userRepository;
    private final PinRepository pinRepository;
    private final ImageRepository imageRepository;
    private final HashtagRepository hashtagRepository;
    private final LikeRepository likeRepository;
    private final FollowRepository followRepository;

    public Article write(ArticleDto articleDto) {
        Article article = new Article();

        User user = userRepository.findById(articleDto.getUserId()).orElseThrow(
                ()->{ throw new UserNotFoundException(); }
        );
        article.setUser(user);

        setData(articleDto, article);

        articleRepository.save(article);

        return article;
    }

    @Transactional(readOnly = true)
    public ArticleInfoDto findByArticleId(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("Article", articleId.toString());
                }
        );

        int likes = likeRepository.countByArticle(article).intValue();

        ArticleInfoDto articleInfoDto = new ArticleInfoDto(article);
        articleInfoDto.setLikes(likes);
        return articleInfoDto;
    }

    @Transactional(readOnly = true)
    public List<ArticleSimpleDto> findByHashtag(String hashtag) {
        List<Article> articles = articleRepository.findByHashtag(hashtag);
        List<ArticleSimpleDto> result = new ArrayList<>();
        for (int i = 0; i < articles.size(); i++) {
            result.add(new ArticleSimpleDto(articles.get(i)));
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<ArticleSimpleDto> findByUserId(String userId) {
        List<Article> articles = articleRepository.findByUserId(userId);
        List<ArticleSimpleDto> result = new ArrayList<>();
        for (int i = 0; i < articles.size(); i++) {
            result.add(new ArticleSimpleDto(articles.get(i)));
        }
        return result;
    }

    public Long modify(ArticleDto articleDto) {

        Article article = articleRepository.findById(articleDto.getArticleId()).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("Article", articleDto.getArticleId().toString());
                }
        );

        article.resetPin();
        article.resetHashtag();
        setData(articleDto, article);

        return article.getArticleId();
    }

    public void delete(Long articleId) {
        Article findArticle = articleRepository.findById(articleId).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("article", articleId.toString());
                }
        );
        findArticle.resetHashtag();
        articleRepository.delete(findArticle);
    }

    public void setData(ArticleDto articleDto, Article article) {
        Pin pin = null;
        if (articleDto.getPinId() != null && articleDto.getPinId() != 0) {
            pin = pinRepository.findById(articleDto.getPinId()).orElseThrow(
                    () -> {
                        throw new ElementNotFoundException("Pin", articleDto.getPinId().toString());
                    }
            );
        } else {
            Pin newPin = new Pin();
            newPin.setLocation(articleDto.getLat(), articleDto.getLng());
            newPin.setAddress(articleDto.getAddressName());
            Long savedPinId = pinRepository.save(newPin).getPinId();
            pin = pinRepository.findById(savedPinId).orElseThrow(
                    () -> {
                        throw new ElementNotFoundException("Pin", articleDto.getPinId().toString());
                    }
            );
        }
        article.setPin(pin);

        article.setContents(articleDto.getContents());

        if (articleDto.getHashtags() != null) {
            for (String contents : articleDto.getHashtags()) {
                List<Hashtag> hashtags = hashtagRepository.findByContents(contents);
                if (hashtags.size() > 0) {
                    article.addHashtag(hashtags.get(0));
                } else {
                    Hashtag hashtag = new Hashtag();
                    hashtag.setContents(contents);
                    Hashtag savedHashtag = hashtagRepository.save(hashtag);
                    article.addHashtag(savedHashtag);
                }
            }
        }
    }

    /***
     *  좋아요 관련 메소드
     */

    @Transactional
    public void like(String userId, Long articleId){
        User user = getUser(userId);
        Article article = getArticle(articleId);

        Likes like = new Likes();
        like.setUser(user);
        like.setArticle(article);

        likeRepository.save(like);

        user.addLike(like);
    }

    @Transactional
    public void unlike(String userId, Long articleId){
        Likes like = likeRepository.findByUserIdAndArticleId(userId, articleId).orElseThrow(
                ()->{throw new ElementNotFoundException("User, Article", "userId "+articleId.toString());}
        );

        User user = getUser(userId);

        user.removeLike(like);

        likeRepository.delete(like);
    }

    public Page<SimpleUserInfoDto> findLikeUsers(String currentUserId, Long articleId, Pageable pageable){
        User currentUser = getUser(currentUserId);
        Article article = getArticle(articleId);
        List<Follow> follows = followRepository.findByFrom(currentUser);

        Page<SimpleUserInfoDto> result = likeRepository.findByArticle(article, pageable).map(
                (like) -> {return new SimpleUserInfoDto(like.getUser(), follows.contains(like.getUser()));}
        );
        return result;
    }

    private User getUser(String userId){
        User user = userRepository.findById(userId).orElseThrow(
                ()->{throw new UserNotFoundException();}
        );
        return user;
    }

    private Article getArticle(Long articleId){
        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> { throw new ElementNotFoundException("article", articleId.toString()); }
        );
        return article;
    }

}
