package com.ggj.java.lettcode.array;

/**
 * 给定一个链表，判断链表中是否有环。
 *
 * @author gaoguangjin
 */
public class HasCycleList {
    public static void main(String[] args) {

        HasCycleList.ListNode head = new HasCycleList.ListNode(1);
        HasCycleList.ListNode node2 = new HasCycleList.ListNode(2);
        HasCycleList.ListNode node3 = new HasCycleList.ListNode(3);
        HasCycleList.ListNode node4 = new HasCycleList.ListNode(4);
        HasCycleList.ListNode node5 = new HasCycleList.ListNode(5);
        head.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
        //有环
        node5.next = node2;

        HasCycleList hasCycleList = new HasCycleList();

        hasCycleList.detectCycle(head);
       // System.out.println(hasCycleList.hasCycle(head));
    }

    public static class ListNode {
        int val;
        HasCycleList.ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

    /**
     * 双指针：快慢指针
     * 如果有环 直接迭代肯定不行的
     * 还是把链表比作一条跑道，链表中有环，那么这条跑道就是一条圆环跑道，
     * 在一条圆环跑道中，两个人有速度差，那么迟早两个人会相遇，只要相遇那么就说明有环。
     *
     * @param head
     * @return
     */
    public boolean hasCycle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head.next;
        //如果有环 肯定会相遇的
        while (slow != fast) {
            //直接走完了肯定没环
            if (fast == null || fast.next == null) {
                return false;
            }
            slow = slow.next;
            fast = fast.next.next;
        }
        return true;
    }


    public ListNode detectCycle(ListNode head) {

        if(head==null || head.next==null){
            return null;
        }
        ListNode slowNode=head.next;
        ListNode quickNode=head.next.next;
        while(slowNode!=quickNode){
            if(quickNode==null||quickNode.next==null){
                return null;
            }
            slowNode=slowNode.next;
            quickNode=quickNode.next.next;
        }

        //那么我们可以知道fast指针走过a+b+c+b
        //. slow指针走过a+b
        //那么2*(a+b) = a+b+c+b
        //所以a = c,那么此时让slow回到起点，fast依然停在z，两个同时开始走，一次走一步 那么它们最终会相遇在y点，正是环的起始点
        quickNode=head;
        while(slowNode!=quickNode){
            slowNode=slowNode.next;
            quickNode=quickNode.next;
        }
        return slowNode;
    }
}
