package com.web.curation.service.article;

import com.web.curation.domain.Pin;
import com.web.curation.domain.User;
import com.web.curation.domain.article.Article;
import com.web.curation.domain.article.ArticleImage;
import com.web.curation.exceptions.ElementNotFoundException;
import com.web.curation.exceptions.UserNotFoundException;
import com.web.curation.domain.hashtag.Hashtag;
import com.web.curation.dto.article.ArticleDto;
import com.web.curation.dto.article.ArticleInfoDto;
import com.web.curation.repository.user.UserRepository;
import com.web.curation.repository.article.ArticleRepository;
import com.web.curation.repository.hashtag.HashtagRepository;
import com.web.curation.repository.image.ImageRepository;
import com.web.curation.repository.pin.PinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * com.web.curation.service.article
 * ArticleService.java
 *
 * @author 이주희
 * @date 2021-01-26 오전 11:02
 * @변경이력 김종성: PinRepository 수정으로 관련된 부분 일부 수정(추후 리팩토링 필수!!)
 **/

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final UserRepository userRepository;
    private final PinRepository pinRepository;
    private final ImageRepository imageRepository;
    private final HashtagRepository hashtagRepository;

    @Transactional
    public Article write(ArticleDto articleDto) {
        Article article = new Article();

        User user = userRepository.findById(articleDto.getUserId()).orElseThrow(
                () -> {
                    throw new UserNotFoundException(articleDto.getUserId());
                }
        );
        article.setUser(user);


        Pin pin = null;
        if (articleDto.getPinId() != null && articleDto.getPinId() != 0) {
            pin = pinRepository.findById(articleDto.getPinId()).orElseThrow(
                    () -> {
                        throw new ElementNotFoundException("Pin", articleDto.getPinId().toString());
                    }
            );
        }else {
            Pin newPin = new Pin();
            newPin.setLocation(articleDto.getLat(), articleDto.getLng());
            // TODO kakao api에서 위치의 주소 받아오기
            newPin.setAddress("추후에 수정");
            Long savedPinId = pinRepository.save(newPin).getPinId();
            pin = pinRepository.findById(savedPinId).orElseThrow(
                    () -> {
                        throw new ElementNotFoundException("Pin", articleDto.getPinId().toString());
                    }
            );
        }
        article.setPin(pin);

        for (String contents : articleDto.getHashtags()) {
            List<Hashtag> hashtags = hashtagRepository.findByContents(contents);
            if (hashtags.size() != 0) {
                article.addHashtag(hashtags.get(0));
            } else {
                Hashtag hashtag = new Hashtag();
                hashtag.setContents(contents);
                Hashtag savedHashtag = hashtagRepository.save(hashtag);
                article.addHashtag(savedHashtag);
            }
        }

        article.setContents(articleDto.getContents());

        articleRepository.save(article);

        return article;
    }

    public ArticleInfoDto findByArticleId(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("Article", articleId.toString());
                }
        );
        return new ArticleInfoDto(article);
    }

    public List<ArticleInfoDto> findByHashtag(String hashtag) {
        List<Article> articles = articleRepository.findByHashtag(hashtag);
        List<ArticleInfoDto> result = new ArrayList<>();
        for (int i = 0; i < articles.size(); i++) {
            result.add(new ArticleInfoDto(articles.get(i)));
        }
        return result;
    }

    public List<ArticleInfoDto> findByUserId(String userId) {
        List<Article> articles = articleRepository.findByUserId(userId);
        List<ArticleInfoDto> result = new ArrayList<>();
        for (int i = 0; i < articles.size(); i++) {
            result.add(new ArticleInfoDto(articles.get(i)));
        }
        return result;
    }

    @Transactional
    public Long modify(ArticleDto articleDto) {

        Article article = articleRepository.findById(articleDto.getArticleId()).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("Article", articleDto.getArticleId().toString());
                }
        );

        article.resetPin();
        Pin pin = null;
        if (articleDto.getPinId() != null)
            pin = pinRepository.findById(articleDto.getPinId()).orElseThrow(
                    () -> {
                        throw new ElementNotFoundException("Pin", articleDto.getPinId().toString());
                    }
            );
        else {
            Pin newPin = new Pin();
            newPin.setLocation(articleDto.getLat(), articleDto.getLng());
            // TODO kakao api에서 위치의 주소 받아오기
            newPin.setAddress("추후에 수정");
            Long savedPinId = pinRepository.save(newPin).getPinId();
            pin = pinRepository.findById(savedPinId).orElseThrow(
                    () -> {
                        throw new ElementNotFoundException("Pin", articleDto.getPinId().toString());
                    }
            );
        }
        article.setPin(pin);

        article.resetHashtag();
        for (String contents : articleDto.getHashtags()) {
            List<Hashtag> hashtags = hashtagRepository.findByContents(contents);
            if (hashtags.size() != 0) {
                article.addHashtag(hashtags.get(0));
            } else {
                Hashtag hashtag = new Hashtag();
                hashtag.setContents(contents);
                Hashtag savedHashtag = hashtagRepository.save(hashtag);
                article.addHashtag(savedHashtag);
            }
        }

        article.setContents(articleDto.getContents());

        return article.getArticleId();
    }

    @Transactional
    public void delete(Long articleId) {
        Article findArticle = articleRepository.findById(articleId).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("article", articleId.toString());
                }
        );
        findArticle.resetHashtag();
        articleRepository.delete(findArticle);
    }

}
