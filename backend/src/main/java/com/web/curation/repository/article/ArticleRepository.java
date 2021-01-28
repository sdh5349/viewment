package com.web.curation.repository.article;

import com.web.curation.domain.article.Article;

import java.util.List;
import java.util.Optional;

/**
 * com.web.curation.repository.article
 * ArticleRepository.java
 * @date    2021-01-26 오전 9:32
 * @author  이주희
 *
 * @변경이력
 **/

public interface ArticleRepository {

    void save(Article article);

    void delete(Long articleId);

    Optional<Article> findByArticleId(Long articleId);

    List<Article> findByUserId(String userId);

    List<Article> findByHashtag(String Hashtag);

}
