package com.ggj.java.lettcode.node;

/**
 * @author gaoguangjin
 */
public class SingleNode {
    private static Node head = new Node("head");

    public static void main(String[] args) {

        Node node1 = new Node("1");
        head.next = node1;
        Node node2 = new Node("2");
        node1.next = node2;


        Node node3 = new Node("2");
        add(node3);


    }

    private static void add(Node newNode) {
        if (head.next == null) {
            head.next = newNode;
        }else{
            newNode.next=head.next;
            head.next=newNode;
            //错误的
            /**
             *
             * head.next=newNode;
             *  newNode.next=head.next;
             *
             *  这样其实就是  newNode.next=newNode 死循环了
             */
        }
    }

    static class Node {
        private Node next;
        private String data;

        public Node(String data) {
            this.data = data;
        }
    }

}
