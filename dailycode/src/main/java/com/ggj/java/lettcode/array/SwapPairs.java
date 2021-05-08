package com.ggj.java.lettcode.array;

/**
 * https://leetcode-cn.com/problems/swap-nodes-in-pairs/
 * <p>
 * 给定一个链表，两两交换其中相邻的节点，并返回交换后的链表。
 * <p>
 * 你不能只是单纯的改变节点内部的值，而是需要实际的进行节点交换。
 * <p>
 *  
 * <p>
 * 示例:
 * <p>
 * 给定 1->2->3->4, 你应该返回 2->1->4->3.
 *
 * @author gaoguangjin
 */
public class SwapPairs {


    public static void main(String[] args) {
        SwapPairs.ListNode head = new SwapPairs.ListNode(1);
        SwapPairs.ListNode node2 = new SwapPairs.ListNode(2);
        SwapPairs.ListNode node3 = new SwapPairs.ListNode(3);
        SwapPairs.ListNode node4 = new SwapPairs.ListNode(4);
        SwapPairs.ListNode node5 = new SwapPairs.ListNode(5);
        head.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;

        SwapPairs swapPairs = new SwapPairs();
        swapPairs.swapPairsOne(head);
        System.out.println("args = [" + args + "]");
    }

    /**
     * https://leetcode-cn.com/problems/swap-nodes-in-pairs/solution/di-gui-he-fei-di-gui-liang-chong-jie-fa-java-by-re/
     *
     * @param head
     * @return
     */
    public ListNode swapPairsOne(ListNode head) {

        while (head == null || head.next == null) {
            return head;
        }
        //定义一个新节点
        ListNode node = new ListNode(-1);
        ListNode res = node;
        while (head != null && head.next != null) {
            //指向需要替换的右边节点
            node.next = head.next;
            //去除右边的节点
            head.next = head.next.next;
            //新节点连接到当前head 完成替换
            node.next.next = head;
            //node 继续指向原来的heade
            node = node.next.next;
//            node = head;
            //继续下个循环
            head = head.next;
        }
        return res.next;
    }

    public ListNode swapPairsTwo(ListNode head) {
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        ListNode prevNode = dummy;
        while (head != null && head.next != null) {
            // Nodes to be swapped
            ListNode firstNode = head;
            ListNode secondNode = head.next;

            // Swapping
            prevNode.next = secondNode;
            //去除右边的节点
            firstNode.next = secondNode.next;
            //右边指向到左边
            secondNode.next = firstNode;

            // Reinitializing the head and prevNode for next swap
            prevNode = firstNode;
            head = firstNode.next; // jump
        }
        return dummy.next;
    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }
}
