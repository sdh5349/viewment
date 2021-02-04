package com.web.curation.service.recommend;

import com.web.curation.dto.article.ArticleInfoDto;
import com.web.curation.dto.article.ArticleSimpleDto;
import com.web.curation.dto.user.SimpleUserInfoDto;

import java.util.List;

public interface RecommendService {

    List<ArticleSimpleDto> recommendArticle(String userId);
    List<SimpleUserInfoDto> recommendUser(String userId);
}
