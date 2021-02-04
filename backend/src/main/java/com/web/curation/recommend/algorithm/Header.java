package com.web.curation.recommend.algorithm;

import java.util.ArrayList;
import java.util.List;


public class Header {
    protected int support;
    protected int index;
    protected List<Node> link;

    protected Header(){
        this.support = 1;
        this.index = 0;
    }
    protected Header(int support, int index){
        this.support = support;
        this.index = index;
        link = new ArrayList<>();
    }
}
