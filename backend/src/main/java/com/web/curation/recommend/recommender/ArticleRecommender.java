package com.web.curation.recommend.recommender;

import com.web.curation.domain.User;

import java.util.List;
import java.util.Set;


/**
 * com.web.curation.recommend.recommender
 * ArticleRecommender.java
 * @date    2021-02-04 오후 6:57
 * @author  김종성
 *
 * @변경이력
 **/
public interface ArticleRecommender {

    Set<Long> recommend(User user);

}
