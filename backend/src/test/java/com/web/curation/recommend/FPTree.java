package com.web.curation.recommend;

import java.util.*;
import java.util.Map.Entry;

public class FPTree {

    public Map<Long, Header> headerTable;
    public Node root;
    public int minSupport;

    public FPTree(int minSupport){
        this.minSupport = minSupport;
        root = new Node();
        headerTable = new HashMap<>();
    }

    //빈발하지 못한 패턴들을 헤더 테이블에서 제거
    public void pruning(){

        Iterator<Entry<Long, Header>> iter = headerTable.entrySet().iterator();

        while (iter.hasNext()) {
            Entry<Long, Header> entry = iter.next();
            if(entry.getValue().support < minSupport) iter.remove();
        }
    }

    //헤더 테이블을 지지도 내림차순으로 재정렬
    public void reorder(){
        List<Header> headers = new ArrayList<>(headerTable.values());
        Collections.sort(headers, (a, b)->Integer.compare(b.support, a.support));
        Set<Long> items = headerTable.keySet();
        int i = 0;
        for(Long item : items){
            headerTable.get(item).index = i++;
        }
    }
}
