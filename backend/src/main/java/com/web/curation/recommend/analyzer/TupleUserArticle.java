package com.web.curation.recommend.analyzer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class TupleUserArticle {
    private String userId;
    private Long articleId;
}
