package com.web.curation.service.article;

import com.web.curation.domain.Pin;
import com.web.curation.domain.User;
import com.web.curation.domain.article.Article;
import com.web.curation.domain.connection.Likes;
import com.web.curation.domain.hashtag.Hashtag;
import com.web.curation.domain.memory.MemoryPin;
import com.web.curation.dto.article.ArticleDto;
import com.web.curation.dto.article.ArticleInfoDto;
import com.web.curation.dto.article.ArticleSimpleDto;
import com.web.curation.dto.user.SimpleUserInfoDto;
import com.web.curation.event.NewArticleEvent;
import com.web.curation.exceptions.ElementNotFoundException;
import com.web.curation.exceptions.UserNotFoundException;
import com.web.curation.repository.article.ArticleRepository;
import com.web.curation.repository.follow.FollowRepository;
import com.web.curation.repository.hashtag.HashtagRepository;
import com.web.curation.repository.like.LikeRepository;
import com.web.curation.repository.memory.MemoryRepository;
import com.web.curation.repository.pin.PinRepository;
import com.web.curation.repository.user.UserRepository;
import com.web.curation.util.DistanceUtil;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * com.web.curation.service.article
 * ArticleService.java
 *
 * @author 이주희
 * @date 2021-01-26 오전 11:02
 * @변경이력 김종성: PinRepository 수정으로 관련된 부분 일부 수정(추후 리팩토링 필수!!)
 * @변경이력 김종성: like, unlike 기능 추가
 * @변경이력 이주희: 기본 피드 게시글 조회 기능 추가
 * 이주희 21-02-09 기억하기 주변 핀 찾기 기능 추가
 **/

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final UserRepository userRepository;
    private final PinRepository pinRepository;
    private final MemoryRepository memoryRepository;
    private final HashtagRepository hashtagRepository;
    private final LikeRepository likeRepository;
    private final FollowRepository followRepository;

    private final ApplicationEventPublisher eventPublisher;

    public Article write(ArticleDto articleDto) {
        Article article = new Article();

        User user = getUser(articleDto.getUserId());
        article.setUser(user);

        Pin pin = null;
        if (articleDto.getPinId() != null && articleDto.getPinId() != 0) {
            pin = getPin(articleDto.getPinId());
        } else {
            Pin newPin = new Pin();
            newPin.setLocation(articleDto.getLat(), articleDto.getLng());
            newPin.setAddress(articleDto.getAddressName());
            Long savedPinId = pinRepository.save(newPin).getPinId();
            pin = getPin(savedPinId);
            addMemoryPin(pin);
        }
        article.setPin(pin);

        setData(articleDto, article);

        Article savedArticle = articleRepository.save(article);

        eventPublisher.publishEvent(new NewArticleEvent(savedArticle));

        return savedArticle;
    }

    @Transactional(readOnly = true)
    public ArticleInfoDto findByArticleId(String currentUserId, Long articleId) {
        Article article = getArticle(articleId);
        User currentUser = getUser(currentUserId);

        int likes = likeRepository.countByArticle(article).intValue();
        boolean liked = likeRepository.existsByUserAndArticle(currentUser, article);

        ArticleInfoDto articleInfoDto = new ArticleInfoDto(article);
        articleInfoDto.setLikes(likes);
        articleInfoDto.setLiked(liked);
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

    @Transactional(readOnly = true)
    public List<ArticleSimpleDto> getArticlesForFeed(String userId, double lat, double lng) {
        Set<Article> articles = new HashSet<>();

        getUser(userId).getMemories().stream()
                .forEach(memory -> {
                    memory.getNearbyPins().stream()
                            .forEach(memoryPin -> {
                                articles.addAll(memoryPin.getPin().getArticles());
                            });
                });

        List<ArticleSimpleDto> result = articles.stream()
                .map(article -> {
                    ArticleSimpleDto dto = new ArticleSimpleDto(article);
                    Point point = article.getPin().getLocation();
                    dto.setDistance(DistanceUtil.calcDistance(lat, lng, point.getY(), point.getX()));
                    return dto;
                })
                .collect(Collectors.toList());
        return result;
    }

    @Transactional(readOnly = true)
    public List<ArticleSimpleDto> getArticlesByPins(Long[] pinIds, String start, String end) {
        List<Article> articles = new ArrayList<>();
        for (int i = 0; i < pinIds.length; i++) {
            Pin pin = getPin(pinIds[i]);
            articles.addAll(articleRepository.findByPinAndDateBetween(pin, start, end));
        }

        List<ArticleSimpleDto> result = articles.stream()
                .map(article -> {
                    return new ArticleSimpleDto(article);
                })
                .collect(Collectors.toList());
        return result;
    }

    public List<ArticleSimpleDto> getArticlesForTrend(String userId, double lat, double lng) {
        Set<Article> articles = new HashSet<>();
        List<Article> nearbyArticles = new ArrayList<>();

        User user = getUser(userId);
        followRepository.findByFrom(user).stream()
                .forEach(follow -> {
                    articles.addAll(follow.getTo().getArticles());
                });

        pinRepository.findAll().stream()
                .forEach(pin -> {
                    Point point = pin.getLocation();
                    if (1500 > DistanceUtil.calcDistance(lat, lng, point.getY(), point.getX())) {
                        nearbyArticles.addAll(pin.getArticles());
                    }
                });

        Collections.sort(nearbyArticles, (a1, a2) -> a2.getLikes().size() - a1.getLikes().size());

        if(nearbyArticles.size()>0)
            articles.addAll(nearbyArticles.subList(0, Math.min(nearbyArticles.size(), Math.max(10,articles.size()/3))));

        List<ArticleSimpleDto> result = articles.stream()
                .map(article -> {
                    ArticleSimpleDto dto = new ArticleSimpleDto(article);
                    if (!followRepository.findByUserIdAndTargetUserId(userId, article.getUser().getId()).isEmpty())
                        dto.setFollowing(true);
                    return dto;
                })
                .collect(Collectors.toList());
        return result;
    }

    public Long modify(ArticleDto articleDto) {

        Article article = getArticle(articleDto.getArticleId());

        article.resetHashtag();
        setData(articleDto, article);

        return article.getArticleId();
    }

    public void delete(Long articleId) {
        Article findArticle = getArticle(articleId);
        findArticle.resetHashtag();
        findArticle.resetUser();
        articleRepository.delete(findArticle);
    }

    public void setData(ArticleDto articleDto, Article article) {
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

        article.setDate(articleDto.getDate());
    }


    private void addMemoryPin(Pin pin) {
        memoryRepository.findAll().stream()
                .forEach(memory -> {
                    if(memory.getRadius() >= DistanceUtil.calcDistance(memory.getLocation().getY(), memory.getLocation().getX(), pin.getLocation().getY(), pin.getLocation().getX()))
                        memory.addNearbyPins(MemoryPin.createMemoryPin(memory,pin));
                });
    }


    /***
     *  좋아요 관련 메소드
     */

    @Transactional
    public void like(String userId, Long articleId) {
        User user = getUser(userId);
        Article article = getArticle(articleId);

        Likes like = new Likes();
        like.setUser(user);
        like.setArticle(article);

        likeRepository.save(like);

        user.addLike(like);
        article.addLike(like);
    }

    @Transactional
    public void unlike(String userId, Long articleId) {
        Likes like = likeRepository.findByUserIdAndArticleId(userId, articleId).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("User, Article", "userId " + articleId.toString());
                }
        );

        User user = getUser(userId);
        Article article = getArticle(articleId);

        user.removeLike(like);
        article.removeLike(like);

        likeRepository.delete(like);

    }

    public Page<SimpleUserInfoDto> findLikeUsers(String currentUserId, Long articleId, Pageable pageable) {
        User currentUser = getUser(currentUserId);
        Article article = getArticle(articleId);
        List<User> follows = followRepository.findByFrom(currentUser).stream()
                .map(follow->{return follow.getTo();})
                .collect(Collectors.toList());

        Page<SimpleUserInfoDto> result = likeRepository.findByArticle(article, pageable).map(
                (like) -> {
                    return new SimpleUserInfoDto(like.getUser(), follows.contains(like.getUser()));
                }
        );
        return result;
    }

    private User getUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> {
                    throw new UserNotFoundException();
                }
        );
        return user;
    }

    private Article getArticle(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("article", articleId.toString());
                }
        );
        return article;
    }

    private Pin getPin(Long pinId) {
        Pin pin = pinRepository.findById(pinId).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("Pin", pinId.toString());
                }
        );
        return pin;
    }
}
