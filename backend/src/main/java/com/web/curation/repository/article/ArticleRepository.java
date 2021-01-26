package com.web.curation.repository.article;

import com.web.curation.domain.article.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {

    void save(Article article);

    void update(Article article);

    void delete(Long articleId);

    Optional<Article> findByArticleId(Long articleId);

    List<Article> findByUserId(String userId);

    List<Article> findByHashtag(String Hashtag);

}
