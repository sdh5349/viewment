package com.web.curation.event;

import com.web.curation.domain.article.Article;
import com.web.curation.domain.memory.MemoryPin;
import lombok.Getter;

import java.util.List;

@Getter
public class NewArticleEvent {

    private Article article;
    private List<MemoryPin> memoryPins;

    public NewArticleEvent(Article article, List<MemoryPin> memoryPins) {
        this.article = article;
        this.memoryPins = memoryPins;
    }
}
