package com.web.curation.dto.article;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * com.web.curation.dto.article
 * ArticleDto.java
 * @date    2021-01-27
 * @author  이주희
 *
 * @변경이력
 **/

@Getter
@Setter
@RequiredArgsConstructor
public class ArticleDto {

    private Long articleId;
    private String userId;
    private double lat;
    private double lng;
    private Long pinId;
    private String contents;
    private List<String> hashtags;

}
