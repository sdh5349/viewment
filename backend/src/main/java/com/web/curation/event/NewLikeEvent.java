package com.web.curation.event;

import com.web.curation.domain.User;
import com.web.curation.domain.article.Article;
import lombok.Getter;

@Getter
public class NewLikeEvent {

    private User from;
    private User to;
    private Article article;

    public NewLikeEvent(User from, User to, Article article) {
        this.from = from;
        this.to = to;
        this.article = article;
    }
}
