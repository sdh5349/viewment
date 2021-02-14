package com.web.curation.service.recommend;

import com.web.curation.dto.article.ArticleFeedDto;
import com.web.curation.dto.user.SimpleUserInfoDto;

import java.util.List;

public interface RecommendService {

    List<ArticleFeedDto> recommendArticle(String userId);
    List<SimpleUserInfoDto> recommendUser(String userId);
}
