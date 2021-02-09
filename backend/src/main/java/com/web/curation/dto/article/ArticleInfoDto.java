package com.web.curation.dto.article;

import com.web.curation.domain.article.Article;
import com.web.curation.dto.hashtag.HashtagDto;
import com.web.curation.dto.image.ImageDto;
import com.web.curation.dto.pin.PinDto;
import com.web.curation.dto.reply.ReplyDto;
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
    private List<ReplyDto> replies = new ArrayList<>();
    private String contents;
    private String date;
    private String wdate;

    private boolean liked;

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
        for (int i = 0; i < article.getReplies().size(); i++) {
            this.replies.add(new ReplyDto(article.getReplies().get(i)));
        }
        this.contents = article.getContents();
        this.date = article.getDate();
        this.wdate = article.getWdate().toString();
    }
}
