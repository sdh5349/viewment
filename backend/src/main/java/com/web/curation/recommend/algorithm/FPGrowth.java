package com.web.curation.recommend.algorithm;

import java.util.*;
import java.util.stream.Collectors;

/**
 * com.web.curation.recommend.algorithm
 * FPGrowth.java
 * @date    2021-02-04 오후 6:56
 * @author  김종성
 *
 * @변경이력
 **/

public class FPGrowth<T> {
    private FPTree<T> globalTree;
    private List<Pattern> items;
    private Map<Integer, List<Pattern>> itemsMap;
    private Map<T, List<Rule<T>>> rules;
    private int minSupport;
    private int amount;

    public FPGrowth(int minSupport){
        this.globalTree = new FPTree<>(minSupport);
        this.items = new ArrayList<>();
        this.minSupport = minSupport;
        this.itemsMap = new HashMap<>();
    }

    public FPTree setGlobalTree(List<List<T>> dataset){
        List<Integer> pathSupports = new ArrayList<>();
        for(int i = 0; i<dataset.size(); i++){
            pathSupports.add(1);
        }
        this.amount = dataset.size();
        setHeaderTable(globalTree, dataset, pathSupports, true);

        globalTree.pruning();
        globalTree.reorder();

        setTreeBody(globalTree, dataset, pathSupports);

        return globalTree;
    }

    private void setHeaderTable(FPTree<T> tree, List<List<T>> dataset, List<Integer> pathSupports, boolean isGlobal){
        for(int i = 0; i<dataset.size(); i++){
            int pathSup = pathSupports.get(i);
            for(T item : dataset.get(i)){

                if(tree.headerTable.containsKey(item)){
                    tree.headerTable.get(item).support += pathSup;
                }else{
                    tree.headerTable.put(item, new Header(pathSup, 0));
                }
            }
        }
    }

    private void setTreeBody(FPTree<T> tree, List<List<T>> dataset, List<Integer> pathSupports){
        for(int i = 0; i<dataset.size(); i++){
            Set<T> headerItems = tree.headerTable.keySet();

            List<T> temp = dataset.get(i).stream()
                    .filter( item -> headerItems.contains(item))
                    .collect(Collectors.toList());

            int path_sup = pathSupports.get(i);
            Node<T> curNode = tree.root;
            for(T item : temp){
                if(!curNode.children.containsKey(item)){
                    curNode.children.put(item, new Node(item, curNode, path_sup));
                    tree.headerTable.get(item).link.add(curNode.children.get(item));
                }else{
                    curNode.children.get(item).support += path_sup;
                }
                curNode = curNode.children.get(item);
            }

        }
    }

    private FPTree<T> createConditionalTree(List<List<T>> dataset, List<Integer> pathSupports){
        FPTree<T> tree = new FPTree<>(minSupport);

        setHeaderTable(tree, dataset, pathSupports, false);

        tree.pruning();

        setTreeBody(tree, dataset, pathSupports);

        return tree;
    }

    public void mine(){
        List<T> prefixPatterns = new ArrayList<>();
        mine(globalTree, prefixPatterns, 0);
    }

    private void mine(FPTree<T> tree, List<T> prefixPatterns, int depth){
        //가장 지지도가 낮은 아이템부터 마이닝 진행
        List<ItemHeader> itemHeaders = tree.headerTable.entrySet().stream()
                .map(entry -> {
                    return new ItemHeader(entry.getKey(), entry.getValue());
                })
                .collect(Collectors.toList());

        Collections.sort(itemHeaders, (a, b)->Integer.compare(a.header.support, b.header.support));

        for(ItemHeader itemHeader : itemHeaders){
            
            T item = (T)itemHeader.item;
            
            if(itemHeader.header.support < this.minSupport) continue;

            List<T> prefix = new ArrayList<>(prefixPatterns);
            prefix.add(item);

            Pattern<T> pattern = new Pattern<>(prefix, itemHeader.header.support);
            this.items.add(pattern);

            this.itemsMap.putIfAbsent(prefix.size(), new ArrayList<>());
            this.itemsMap.get(prefix.size()).add(pattern);

            List<List<T>> conditionalPaths = new ArrayList<>();
            //각 경로의 지지도를 저장하는 리스트
            List<Integer> pathSupports = new ArrayList<>();

            for(Node<T> node : tree.headerTable.get(item).link){
                Node<T> curNode = node.parent;
                pathSupports.add(node.support);

                List<T> tempList = new ArrayList<>();
                while(curNode.item != null){
                    tempList.add(0, curNode.item);
                    curNode = curNode.parent;
                }
                conditionalPaths.add(tempList);
            }

            FPTree conditionalTree = createConditionalTree(conditionalPaths,  pathSupports);

            mine(conditionalTree, prefix, depth+1);
        }
    }

    public List<Pattern> getAllResult(){
        return this.items;
    }

    public List<Pattern> getResultByLength(int length){
        return this.itemsMap.get(length);
    }

    public Map<T, List<Rule<T>>> getAllRules(){

        Map<T, Integer> itemSupport = new HashMap<>();
        rules = new HashMap<>();
        if(itemsMap.get(1) == null || itemsMap.get(2) == null){
            return rules;
        }

        for(Pattern p : itemsMap.get(1)){
            T item = (T)p.items.get(0);
            int support = p.support;
            itemSupport.putIfAbsent(item, support);
        }

        for(Pattern p : itemsMap.get(2)){
            T a = (T)p.items.get(0);
            T b = (T)p.items.get(1);
            int support_ab = p.support;

            int support_a = itemSupport.get(a);
            int support_b = itemSupport.get(b);

            double supp_rule = support_ab/(double)this.amount;
            double conf_rule_a = support_ab/(double)support_a;
            double conf_rule_b = support_ab/(double)support_b;

            double lift_rule = support_ab/(double)(support_a*support_b);
            Rule<T> rule_a = new Rule<>(a, b, supp_rule, conf_rule_a, lift_rule);
            Rule<T> rule_b = new Rule<>(b, a, supp_rule, conf_rule_b, lift_rule);

            rules.putIfAbsent(a, new ArrayList<>());
            rules.putIfAbsent(b, new ArrayList<>());
            rules.get(a).add(rule_a);
            rules.get(b).add(rule_b);
        }
        return rules;
    }

    static class ItemHeader<T>{
        T item;
        Header header;
        public ItemHeader(T item, Header header){
            this.item = item;
            this.header = header;
        }
    }
}
