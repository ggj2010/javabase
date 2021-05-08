package com.ggj.java.lettcode.node;

import lombok.extern.slf4j.Slf4j;

/**
 * https://www.cnblogs.com/vaiyanzi/p/9509248.html
 * @author gaoguangjin
 */
@Slf4j
public class SortNodeTest {
    Node<Integer> root;
    static Node<Integer> root2;

    public static void main(String[] args) {
        SortNodeTest test = new SortNodeTest();
        test.insert(1);
        test.insert(3);
        test.insert(5);
        test.insert(6);
        test.insert(2);
        test.insert(8);
        test.insert(7);
        test.insert(4);

        System.out.println("args = [" + test.root + "]");

        int i=getNodeHight(test.root);
        System.out.println("root 的度"+i);

        test.insertTwo(root2, 1);
        test.insertTwo(root2, 3);
        test.insertTwo(root2, 5);
        test.insertTwo(root2, 6);
        test.insertTwo(root2, 2);
        test.insertTwo(root2, 8);
        test.insertTwo(root2, 7);
        test.insertTwo(root2, 4);


        middleOrder(test.root);


    }

    /**
     * 获取节点的高度
     * @param root
     * @return
     */
    private static int getNodeHight(Node<Integer> root) {
        if(root==null){
            return 0;
        }
        return Math.max(getNodeHight(root.left), getNodeHight(root.right))+1 ;

    }

    /**
     * 左根右边
     *
     * @param node
     * @return
     */
    private static void middleOrder(Node node) {
       /* if (node.left != null) {
            middleOrder(node.left);
        }
        log.info("" + node.data);
        if (node.right != null) {
            middleOrder(node.right);
        }*/
        /**或者**/
        if (node != null) {
            middleOrder(node.left);
            log.info("" + node.data);
            middleOrder(node.right);
        }
    }

    /**
     * 二叉排序树规则
     * 左边结点值小于根节点
     * 右边节点值大于根节点
     * 其实就是二叉树的中序遍历规则
     *
     * @param data
     */
    private void insert(int data) {
        if (root == null) {
            root = new Node(data);
        } else {
            Node<Integer> currentNode = root;
            while (true) {
                //大于比较右边
                if (data > currentNode.data) {
                    if (currentNode.right == null) {
                        currentNode.right = new Node(data);
                        break;
                    } else {
                        currentNode = currentNode.right;
                    }
                    //小于比较左边
                } else {
                    if (currentNode.left == null) {
                        currentNode.left = new Node(data);
                        break;
                    } else {
                        currentNode = currentNode.left;
                    }
                }

            }
        }
    }

    /**
     * 递归插入
     * 2
     * 1   3
     * 4
     *
     * @param node
     */
    private Node<Integer> insertTwo(Node<Integer> node, int data) {
        if (root2 == null) {
            root2 = new Node<>(data);
            return root2;
        }
        if (node == null) {
            return new Node<>(data);
        }
        //递归右边
        if (data > node.data) {
            node.right = insertTwo(node.right, data);
        } else {
            //递归左边
            node.left = insertTwo(node.left, data);
        }
        return node;
    }
}
