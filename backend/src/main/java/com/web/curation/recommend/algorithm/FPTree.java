package com.web.curation.recommend.algorithm;

import java.util.*;
import java.util.Map.Entry;

public class FPTree<T> {

    protected Map<T, Header> headerTable;
    protected Node<T> root;
    protected int minSupport;

    protected FPTree(int minSupport){
        this.minSupport = minSupport;
        root = new Node();
        headerTable = new HashMap<>();
    }

    //빈발하지 못한 패턴들을 헤더 테이블에서 제거
    protected void pruning(){

        Iterator<Entry<T, Header>> iter = headerTable.entrySet().iterator();

        while (iter.hasNext()) {
            Entry<T, Header> entry = iter.next();
            if(entry.getValue().support < minSupport) iter.remove();
        }
    }

    //헤더 테이블을 지지도 내림차순으로 재정렬
    public void reorder(){
        List<Header> headers = new ArrayList<>(headerTable.values());
        Collections.sort(headers, (a, b)->Integer.compare(b.support, a.support));
        Set<T> items = headerTable.keySet();
        int i = 0;
        for(T item : items){
            headerTable.get(item).index = i++;
        }
    }
}
