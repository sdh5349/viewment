package com.web.curation.dto.article;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedArticleDto {

    private String userId;
    private boolean includeMine;
    private boolean includeFollowings;
    private double lat;
    private double lng;

}
