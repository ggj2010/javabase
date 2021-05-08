package com.ggj.java.lettcode.node;

import java.util.ArrayList;
import java.util.List;

/**
 * https://leetcode-cn.com/problems/flatten-binary-tree-to-linked-list/
 * 114. 二叉树展开为链表
 * <p>
 * 给你二叉树的根结点 root ，请你将它展开为一个单链表：
 * <p>
 * 展开后的单链表应该同样使用 TreeNode ，其中 right 子指针指向链表中下一个结点，而左子指针始终为 null 。
 * 展开后的单链表应该与二叉树 先序遍历 顺序相同
 *
 * @author gaoguangjin
 */

public class TreeFlattenToNode {

    public static void main(String[] args) {

        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(5);
        root.left.left = new TreeNode(3);
        root.left.right = new TreeNode(4);
        root.right.right = new TreeNode(6);
//        1
//      2   5
//    3   4   6

        List<TreeNode> treeNodeList=new ArrayList<>();
        flatten(root,treeNodeList);

        for (int i = 0; i <treeNodeList.size()-1 ; i++) {
            treeNodeList.get(i).right= treeNodeList.get(i+1);
            treeNodeList.get(i).left=null;
        }
        System.out.println(treeNodeList);


    }

    public static void flatten(TreeNode root, List<TreeNode> treeNodeList) {
        if (root != null) {
            treeNodeList.add(root);
            flatten(root.left, treeNodeList);
            flatten(root.right, treeNodeList);
        } else {
           return;
        }
    }

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
}
