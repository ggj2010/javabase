package com.ggj.java.lettcode.node;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gaoguangjin
 */
@Slf4j
public class Temp {
    static Node root;

    @Data
    static class Node {
        private int data;
        private Node left;
        private Node right;

        public Node(int data) {
            this.data = data;
        }
    }


    public static void main(String[] args) {
//        addNode(new Node(1));
//        addNode(new Node(10));
//        addNode(new Node(3));
//        addNode(new Node(2));
//        addNode(new Node(9));
//        addNode(new Node(8));

        addNodeLoop(root, new Node(1));
        addNodeLoop(root, new Node(10));
        addNodeLoop(root, new Node(3));
        addNodeLoop(root, new Node(2));
        addNodeLoop(root, new Node(9));
        addNodeLoop(root, new Node(8));


        System.out.println(root);

        middleDisplay(root);
    }

    //中序遍历 左 ，根，右
    private static void middleDisplay(Node node) {

        if (node.left != null) {
            middleDisplay(node.left);
        }

        if (node != null) {
            log.info("" + node.data);
        }

        if (node.right != null) {
            middleDisplay(node.right);
        }

    }

    private static void addNode(Node node) {
        if (root == null) {
            root = node;
            return;
        }

        Node currentNode = root;
        while (true) {
            //大于放左边
            if (currentNode.data > node.data) {
                if (currentNode.left == null) {
                    currentNode.left = node;
                    break;
                } else {
                    currentNode = currentNode.left;
                }
            } else {
                if (currentNode.right == null) {
                    currentNode.right = node;
                    break;
                } else {
                    currentNode = currentNode.right;
                }
            }
        }
    }


    private static Node addNodeLoop(Node node, Node addNode) {
        if (root == null) {
            root = addNode;
            return addNode;
        }
        if (node == null) {
            return addNode;
        }
        if (node.data > addNode.data) {
            node.left = addNodeLoop(node.left, addNode);
        } else {
            node.right = addNodeLoop(node.right, addNode);
        }
        return node;
    }
}
