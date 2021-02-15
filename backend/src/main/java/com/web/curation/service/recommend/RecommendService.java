package com.web.curation.service.recommend;

import com.web.curation.dto.article.ArticleFeedDto;
import com.web.curation.dto.user.SimpleUserInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecommendService {
    Page<ArticleFeedDto> recommendArticle(String userId, Pageable pageable);
}
