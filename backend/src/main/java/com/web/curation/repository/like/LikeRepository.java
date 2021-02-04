package com.web.curation.repository.like;

import com.web.curation.domain.User;
import com.web.curation.domain.article.Article;
import com.web.curation.domain.connection.Likes;
import com.web.curation.recommend.analyzer.TupleUserArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, Long> {

    @Query("select l from Likes l where l.user.id = :userId and l.article.articleId = :articleId")
    public Optional<Likes> findByUserIdAndArticleId(@Param("userId") String userId, @Param("articleId") Long articleId);

    public Page<Likes> findByArticle(Article article, Pageable pageable);
    public Long countByArticle(Article article);

    public List<Likes> findByUser(User user);

    @Query("select NEW com.web.curation.recommend.analyzer.TupleUserArticle(l.user.id, l.article.articleId) from Likes l")
    public List<TupleUserArticle> findAllToTuple();
}
