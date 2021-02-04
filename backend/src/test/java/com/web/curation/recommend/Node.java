package com.web.curation.recommend;


import java.util.HashMap;
import java.util.Map;

public class Node {
    public Long item;
    public Node parent;
    public int support;
    public Map<Long, Node> children = new HashMap<>();

    public Node(){}

    public Node(Long item, Node parent, int support) {
        this.item = item;
        this.parent = parent;
        this.support = support;
    }
}
