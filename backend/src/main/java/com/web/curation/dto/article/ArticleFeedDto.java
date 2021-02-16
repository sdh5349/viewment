package com.web.curation.dto.article;

import com.web.curation.domain.article.Article;
import com.web.curation.domain.hashtag.Hashtag;
import com.web.curation.dto.hashtag.HashtagDto;
import com.web.curation.dto.image.ImageDto;
import com.web.curation.dto.pin.PinDto;
import com.web.curation.dto.user.SimpleUserInfoDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * com.web.curation.dto.article
 * ArticleFeedDto.java
 *
 * @author 이주희
 * @date 2021-02-15 오전 7:35
 * @변경이력
 **/

@Getter
@Setter
public class ArticleFeedDto {
    private Long articleId;
    private SimpleUserInfoDto user;
    private PinDto pin;
    private List<ImageDto> images;
    private String date;
    private String wdate;
    private List<HashtagDto> hashtags;
    private double distance;
    private int likes;
    private boolean liked;
    private boolean recommended;

    public ArticleFeedDto(Article article) {
        this.articleId = article.getArticleId();
        this.user = new SimpleUserInfoDto(article.getUser());
        this.pin = new PinDto(article.getPin(), article.getPin().getTrendArticleId());
        images = article.getArticleImages().stream().
                map(articleImage -> {
                    return new ImageDto(articleImage.getImage());
                })
                .collect(Collectors.toList());
        hashtags = article.getHashtags().stream()
                .map(hashtag -> {
                    return new HashtagDto(hashtag);
                })
                .collect(Collectors.toList());
        this.date = article.getDate();
        this.wdate = article.getWdate().toString();
    }
}
