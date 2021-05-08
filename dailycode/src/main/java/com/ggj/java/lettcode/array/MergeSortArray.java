package com.ggj.java.lettcode.array;

/**
 * https://leetcode-cn.com/problems/he-bing-liang-ge-pai-xu-de-lian-biao-lcof/solution/mian-shi-ti-25-he-bing-liang-ge-pai-xu-de-lian-b-2/
 * 输入两个递增排序的链表，合并这两个链表并使新链表中的节点仍然是递增排序的。
 *
 * 示例1：
 *
 * 输入：1->2->4, 1->3->4
 * 输出：1->1->2->3->4->4
 * 限制：
 *
 * 0 <= 链表长度 <= 1000
 *
 * 思路：
 * 定义虚拟节点，然后和两个节点的指挨个比较，如果
 *
 *
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/he-bing-liang-ge-pai-xu-de-lian-biao-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * @author gaoguangjin
 */
public class MergeSortArray {

    public static void main(String[] args) {

        MergeSortArray.ListNode a = new MergeSortArray.ListNode(1);
        MergeSortArray.ListNode node2 = new MergeSortArray.ListNode(2);
        MergeSortArray.ListNode node3 = new MergeSortArray.ListNode(4);

        MergeSortArray.ListNode b = new MergeSortArray.ListNode(1);
        MergeSortArray.ListNode node5 = new MergeSortArray.ListNode(3);
        MergeSortArray.ListNode node6 = new MergeSortArray.ListNode(4);

        a.next = node2;
        node2.next = node3;

        b.next = node5;
        node5.next = node6;


        ListNode resultNode=new ListNode(0);
        ListNode currentNode=resultNode;

        while(a!=null&&b!=null){
            //小于
            if(a.val<b.val){
                currentNode.next=a;
                a=a.next;
            }else{
                currentNode.next=b;
                b=b.next;
            }
            currentNode=currentNode.next;
        }

        currentNode.next=a==null?b:a;

        System.out.println(resultNode);




    }



    public static class ListNode {
        int val;
        MergeSortArray.ListNode next;

        ListNode(int x) {
            val = x;
        }
    }
}
