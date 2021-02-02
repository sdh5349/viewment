package com.web.curation.dto.article;

import com.web.curation.domain.article.Article;
import com.web.curation.dto.hashtag.HashtagDto;
import com.web.curation.dto.image.ImageDto;
import com.web.curation.dto.pin.PinDto;
import com.web.curation.dto.user.SimpleUserInfoDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * com.web.curation.dto.article
 * ArticleInfoDto.java
 * @date    2021-01-27
 * @author  이주희
 *
 * @변경이력
 * UserDto -> SimpleUserInfoDto
 **/

@Getter
@Setter
public class ArticleInfoDto {
    private Long articleId;
    private SimpleUserInfoDto user;
    private PinDto pin;
    private List<ImageDto> images = new ArrayList<>();
    private List<HashtagDto> hashtags = new ArrayList<>();
    private String contents;
    private String wdate;

    private int likes;

    public ArticleInfoDto(Article article){
        this.articleId = article.getArticleId();
        this.user = new SimpleUserInfoDto(article.getUser());
        this.pin = new PinDto(article.getPin());
        for (int i = 0; i < article.getArticleImages().size(); i++) {
            this.images.add(new ImageDto(article.getArticleImages().get(i).getImage()));
        }
        for (int i = 0; i < article.getHashtags().size(); i++) {

            this.hashtags.add(new HashtagDto(article.getHashtags().get(i)));
        }
        this.contents = article.getContents();
    }
}
