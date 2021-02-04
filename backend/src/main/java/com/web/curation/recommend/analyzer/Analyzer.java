package com.web.curation.recommend.analyzer;

import java.util.Collections;
import java.util.List;
import java.util.Set;


/**
 * com.web.curation.recommend.analyzer
 * Analyzer.java
 * @date    2021-02-04 오후 6:56
 * @author  김종성
 *
 * @변경이력
 **/
public interface Analyzer {
    public <T> Set<T> analysis(List<T> input, List<List<T>> dataset);
}
