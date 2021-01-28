package com.web.curation.repository.article;

import com.web.curation.domain.article.ArticleImage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * com.web.curation.repository.article
 * ArticleImageRepository.java
 * @date    2021-01-27
 * @author  이주희
 *
 * @변경이력
 **/

public interface ArticleImageRepository extends JpaRepository<ArticleImage, Long> {
}
