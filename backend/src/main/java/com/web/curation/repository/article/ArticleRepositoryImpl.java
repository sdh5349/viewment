package com.web.curation.repository.article;

import com.web.curation.domain.article.Article;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class ArticleRepositoryImpl implements ArticleRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(Article article) {

        em.persist(article);
    }

    @Override
    public void update(Article article) {

    }

    @Override
    public void delete(Long articleId) {

        em.remove(em.find(Article.class, articleId));
    }

    @Override
    public Optional<Article> findByArticleId(Long articleId) {

        Article article = em.find(Article.class, articleId);
        return Optional.ofNullable(article);
    }

    @Override
    public List<Article> findByUserId(String userId) {
        return null;
    }

    @Override
    public List<Article> findByHashtag(String hashtag) {
        return em.createQuery("select h.articles " +
                                "from hashtag h " +
                                "where upper(h.contents) == upper(:hashtag)", Article.class)
                .setParameter("hashtag", hashtag)
                .getResultList();
//        return em.createQuery("SELECT a" +
//                                "FROM article a, article_hashtag ah, hashtag h" +
//                                "WHERE a.article_id == ah.article_id" +
//                                "AND   ah.hashtag_id == h.hashtag_id" +
//                                "AND   upper(h.contents) == upper(:hashtag)")
//                .setParameter("hashtag", hashtag)
//                .getResultList();
    }



}
