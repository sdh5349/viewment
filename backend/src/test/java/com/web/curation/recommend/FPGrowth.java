package com.web.curation.recommend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FPGrowth {
    public FPTree globalTree;
    public List<Long> items;
    public int minSupport;

    public FPGrowth(int minSupport){
        this.globalTree = new FPTree(minSupport);
        this.items = new ArrayList<>();
        this.minSupport = minSupport;
    }

    public FPTree setGlobalTree(List<List<Long>> dataset){
        List<Integer> pathSupports = new ArrayList<>();
        for(int i = 0; i<dataset.size(); i++){
            pathSupports.add(1);
        }

        setHeaderTable(globalTree, dataset, pathSupports, true);

        globalTree.pruning();
        globalTree.reorder();

        setTreeBody(globalTree, dataset, pathSupports);

        return globalTree;
    }

    private void setHeaderTable(FPTree tree, List<List<Long>> dataset, List<Integer> pathSupports, boolean isGlobal){
        for(int i = 0; i<dataset.size(); i++){
            int pathSup = pathSupports.get(i);
            for(Long item : dataset.get(i)){

                if(tree.headerTable.containsKey(item)){
                    tree.headerTable.get(item).support += pathSup;
                }else{
                    tree.headerTable.put(item, new Header(pathSup, 0));
                }
                if(isGlobal && !items.contains(item)) items.add(item);
            }
        }
    }

    private void setTreeBody(FPTree tree, List<List<Long>> dataset, List<Integer> pathSupports){
        for(int i = 0; i<dataset.size(); i++){
            Set<Long> headerItems = tree.headerTable.keySet();

            List<Long> temp = dataset.get(i).stream()
                    .filter( item -> headerItems.contains(item))
                    .collect(Collectors.toList());

            int path_sup = pathSupports.get(i);
            Node curNode = tree.root;
            for(Long item : temp){
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

    private FPTree createConditionalTree(List<List<Long>> dataset, List<Integer> pathSupports){
        FPTree tree = new FPTree(minSupport);

        setHeaderTable(tree, dataset, pathSupports, false);

        tree.pruning();

        setTreeBody(tree, dataset, pathSupports);

        return tree;
    }

    public void mine(){
        List<Long> prefixPatterns = new ArrayList<>();
        mine(globalTree, prefixPatterns, 0);
    }

    private void mine(FPTree tree, List<Long> prefixPatterns, int depth){
        //가장 지지도가 낮은 아이템부터 마이닝 진행
        List<ItemHeader> itemHeaders = tree.headerTable.entrySet().stream()
                .map(entry -> {
                    return new ItemHeader(entry.getKey(), entry.getValue());
                })
                .collect(Collectors.toList());

        Collections.sort(itemHeaders, (a, b)->Integer.compare(a.header.support, b.header.support));

        for(ItemHeader itemHeader : itemHeaders){
            
            Long item = itemHeader.item;
            
            if(itemHeader.header.support < this.minSupport) continue;

            List<Long> prefix = new ArrayList<>(prefixPatterns);
            prefix.add(item);
            System.out.println(prefix.toString() + " " +itemHeader.header.support);

            List<List<Long>> conditionalPaths = new ArrayList<>();
            //각 경로의 지지도를 저장하는 리스트
            List<Integer> pathSupports = new ArrayList<>();

            for(Node node : tree.headerTable.get(item).link){
                Node curNode = node.parent;
                pathSupports.add(node.support);

                List<Long> tempList = new ArrayList<>();
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

    static class ItemHeader{
        Long item;
        Header header;
        public ItemHeader(Long item, Header header){
            this.item = item;
            this.header = header;
        }
    }
}
