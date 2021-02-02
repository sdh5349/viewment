package com.web.curation.repository.like;

import com.web.curation.domain.User;
import com.web.curation.domain.article.Article;
import com.web.curation.domain.connection.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    @Query("select l from Like l where l.user.id = :userId and l.article.articleId = :articleId")
    public Optional<Like> findByUserIdAndArticleId(@Param("userId") String userId, @Param("articleId") Long articleId);

    public Page<Like> findByArticle(Article article, Pageable pageable);
    public Long countByArticle(Article article);
}
