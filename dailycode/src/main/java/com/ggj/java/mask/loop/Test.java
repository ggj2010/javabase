package com.ggj.java.mask.loop;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author gaoguangjin
 */
public class Test {
    public static void main(String[] args) {
        Tree tree = buildTree();
        //根据提供的节点，遍历所有子节点
        List<Integer> seletedIdList = Arrays.asList(2, 3);
        List<Integer> resultList = new ArrayList<>();
        resultList.addAll(seletedIdList);
        parse(tree.getChild(), resultList, tree.getId());
        System.out.println(resultList);
    }

    /**
     *
     * @param child
     * @param resultList
     * @param parentId
     */
    private static void parse(List<Tree> child,  List<Integer> resultList, int parentId) {
        if (CollectionUtils.isEmpty(child)) {
            return;
        }
        for (Tree tree : child) {
            int nowId = tree.getId();
            if (resultList.contains(parentId)) {
                resultList.add(nowId);
            }
            parse(tree.getChild(), resultList, nowId);
        }
    }

    /**
     * --1
     * ----2
     * ------21
     * --------211
     * --------212
     * ------22
     * ----3
     * ------31
     * ------32
     *
     * @return
     */
    private static Tree buildTree() {
        Tree parentTree = new Tree(1, 0, "root");
        Tree tree2 = new Tree(2, 1, "2");
        Tree tree21 = new Tree(21, 21, "21");
        Tree tree211 = new Tree(211, 21, "211");
        Tree tree212 = new Tree(212, 21, "212");
        tree21.setChild(Arrays.asList(tree211, tree212));

        Tree tree22 = new Tree(22, 22, "22");
        tree2.setChild(Arrays.asList(tree21, tree22));

        Tree tree3 = new Tree(3, 1, "3");
        Tree tree31 = new Tree(31, 3, "31");
        Tree tree32 = new Tree(32, 3, "32");
        tree3.setChild(Arrays.asList(tree31, tree32));
        parentTree.setChild(Arrays.asList(tree2, tree3));
        return parentTree;
    }
}
