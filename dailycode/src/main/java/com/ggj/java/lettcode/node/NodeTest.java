package com.ggj.java.lettcode.node;

/**
 * 前序 根 左 右
 * 中序 左 根 右
 * 后序 左 右 根
 * <p>
 * 一棵空树，或者是具有下列性质的二叉树：
 * （1）若左子树不空，则左子树上所有结点的值均小于它的根结点的值；
 * （2）若右子树不空，则右子树上所有结点的值均大于它的根结点的值；
 * （3）左、右子树也分别为二叉排序树；
 * （4）没有键值相等的结点。
 *
 * @author gaoguangjin
 */
public class NodeTest {
    Node root;

    public static void main(String[] args) {
        NodeTest test = new NodeTest();
       /* test.insert(1);
        test.insert(3);
        test.insert(5);
        test.insert(6);
        test.insert(2);
        test.insert(7);
        test.insert(4);*/

        test.insert(1);
        test.insert(3);
        test.insert(7);
        test.insert(4);
        test.preOrder(test.root);
        System.out.println(test);
    }

    //二叉排序树，任何节点的值大于左子树每个节点的值，小于右子树每个节点的值
    public void insert(int iData) {    //插入新的节点
        Node newNode = new Node(iData);
        //树为空，把第一个节点置为根节点
        if (root == null) {
            root = newNode;
        } else { //不为空
            //声明指向root的引用
            Node<Integer> current = root;
            Node<Integer> parent;
            while (true) {
                parent = current;
                //待插入的数值小于当前节点的值
                if (iData < current.data) {
                    //把current指向当前节点的左孩子
                    current = current.left;
                    if (current == null) {
                        parent.left = newNode;
                        return;
                    }
                } else {
                    //待插入的数值大于当前节点的值
                    current = current.right;
                    if (current == null) {
                        parent.right = newNode;
                        return;
                    }
                }
            }
        }
    }

    // 前序 根 左 右
    public void preOrder(Node node) {
        if (node != null) {
            node.displsyndoe();
            preOrder(node.left);
            preOrder(node.right);
        }
    }
    //  中序 左 根 右
    public void middleOrder(Node node) {
        if (node != null) {
            middleOrder(node.left);
            node.displsyndoe();
            middleOrder(node.right);
        }
    }
    //  后序 左 右 根
    public void postOrder(Node node) {
        if (node != null) {
            postOrder(node.left);
            postOrder(node.right);
            node.displsyndoe();
        }
    }
}
