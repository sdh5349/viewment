package com.web.curation.service.recommend;

import com.web.curation.dto.article.ArticleInfoDto;
import com.web.curation.dto.article.ArticleSimpleDto;
import com.web.curation.dto.user.SimpleUserInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecommendService {

    Page<ArticleSimpleDto> recommendArticle(String userId, Pageable pageable);
    List<SimpleUserInfoDto> recommendUser(String userId);
}
