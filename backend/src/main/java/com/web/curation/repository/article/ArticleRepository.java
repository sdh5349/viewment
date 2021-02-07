package com.web.curation.repository.article;

import com.web.curation.domain.User;
import com.web.curation.domain.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * com.web.curation.repository.article
 * ArticleRepository.java
 * @date    2021-01-26 오전 9:32
 * @author  이주희
 *
 * @변경이력
 * 21-02-01 JPA repo를 상속 받는 것으로 변경
 **/

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByUserId(String userId);

    @Query("SELECT a FROM Article a JOIN a.hashtags h WHERE UPPER(h.contents) = UPPER(:contents)")
    List<Article> findByHashtag(@Param("contents") String contents);

    List<Article> findByArticleIdIn(Collection<Long> articleIds);

    List<Article> findByWdateAfter(Timestamp timestamp);

    Long countByUser(User user);

}
