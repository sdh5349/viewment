package com.web.curation.recommend;

import java.util.ArrayList;
import java.util.List;


public class Header {
    public int support;
    public int index;
    public List<Node> link;

    public Header(){
        this.support = 1;
        this.index = 0;
    }
    public Header(int support, int index){
        this.support = support;
        this.index = index;
        link = new ArrayList<>();
    }
}
