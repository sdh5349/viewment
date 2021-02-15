package com.web.curation.event;

import com.web.curation.domain.article.Article;
import lombok.Getter;

@Getter
public class NewArticleEvent {

    private Article article;

    public NewArticleEvent(Article article) {
        this.article = article;
    }
}
