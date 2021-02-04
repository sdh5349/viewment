package com.web.curation.recommend.algorithm;

import java.util.List;

public class Pattern<T>{
    protected List<T> items;
    protected int support;
    protected Pattern(List<T> items, int support){
        this.items = items;
        this.support = support;
    }

    @Override
    public String toString() {
        return "Pattern{" +
                "items=" + items +
                ", support=" + support +
                '}';
    }
}
