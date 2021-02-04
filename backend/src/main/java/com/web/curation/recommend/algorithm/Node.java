package com.web.curation.recommend.algorithm;


import java.util.HashMap;
import java.util.Map;

public class Node<T> {
    protected T item;
    protected Node<T> parent;
    protected int support;
    protected Map<T, Node<T>> children = new HashMap<>();

    protected Node(){}

    protected Node(T item, Node<T> parent, int support) {
        this.item = item;
        this.parent = parent;
        this.support = support;
    }
}
