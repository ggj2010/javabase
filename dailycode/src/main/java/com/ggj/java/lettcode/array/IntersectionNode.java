package com.ggj.java.lettcode.array;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 链表相交
 * <p>
 * https://leetcode-cn.com/problems/intersection-of-two-linked-lists-lcci/
 *
 * @author gaoguangjin
 */
public class IntersectionNode {


    public static void main(String[] args) {
        //
        ListNode head = new ListNode(1);

        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);
        head.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;


        ListNode head2 = new ListNode(10);
        // head2.next = node2;

        // getIntersectionNode(head, head2);

        //你变成我，我变成你，我们便相遇了。
        getIntersectionNodeTwo(head, head2);

    }


    //设链表A的长度为a，链表B的长度为b，A到相交结点的距离为c,B到相交节点的距离为d，
    // 显然可以得到两者相交链表的长度：a - c = b - d， 变换一下式子得到:a + d = b + c
    //我们用一个指针从链表A出发，到末尾后就从B出发，用另一个指针从B出发，到末尾后从A出发，
    // 由于上面的公式，当前一个指针走了a+d步数时，后一个指针走了b+c,两步数相等，即走到了相交节点

    public static ListNode getIntersectionNodeTwo(ListNode headA, ListNode headB) {
        //这种解法错误，因为headB值已经变了 需要用到临时变量
       /* while (headA!=headB){
            headA=(headA==null?headB:headA.next);
            headB=(headB==null?headA:headB.next);
        }
        return headA;*/
        ListNode a = headA;
        ListNode b = headB;

        while (a != b) {
            a = (a == null ? headB : a.next);
            b = (b == null ? headA : b.next);
        }
        return headA;
    }

    //值相同
    public static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        Set<ListNode> set = new HashSet<>();
        while (headA != null) {
            set.add(headA);
            headA = headA.next;
        }
        while (headB != null) {
            if (set.contains(headB)) {
                return headB;
            }
            headB = headB.next;
        }
        return null;
    }

    public static class ListNode {

        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ListNode listNode = (ListNode) o;
            return val == listNode.val &&
                    Objects.equals(next, listNode.next);
        }
    }
}
