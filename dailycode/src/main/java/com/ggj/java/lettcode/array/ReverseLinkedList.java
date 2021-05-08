package com.ggj.java.lettcode.array;

/**
 * https://leetcode-cn.com/problems/fan-zhuan-lian-biao-lcof/
 * <p>
 * 定义一个函数，输入一个链表的头节点，反转该链表并输出反转后链表的头节点。
 * <p>
 *  
 * <p>
 * 示例:
 * <p>
 * 输入: 1->2->3->4->5->NULL
 * 输出: 5->4->3->2->1->NULL
 *
 * @author gaoguangjin
 */
public class ReverseLinkedList {
    public static void main(String[] args) {
        new ReverseLinkedList().normal();
    }

    private void normal() {
        ListNode head = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);
        head.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;

        //ListNode reverseList = reverseList1(head);
        //ListNode reverseList2 = reverseList2(head);
        //ListNode reverseList3 = test(head);
        //ListNode reverseList3 = reverseList3(head);
        // ListNode reverseList4 = reverseList4(head);
        //ListNode reverseList5 = reverseList5(head);
        //  ListNode reverseList6 = reverseList6(head);
        //ListNode reverseList7 = reverseList7(head);
//        ListNode reverseList8 = reverlist8(head);
        //ListNode reverseList9 = reverlist9(head);


        ListNode currentNode=new ListNode(0);
        while(head!=null){
            ListNode temp=head;
            head=head.next;
            temp.next=currentNode;
            currentNode=temp;
        }
        System.out.println("");
    }


    public ListNode reverlist8(ListNode head) {
        ListNode result = new ListNode(0);
        while (head.next != null) {
            //ListNode tmp = head;//tmp=1 ,tmp=2
            //这种写法会导致head.next直接为null了。
            //tmp.next = result;//1.next=null   2.next=1
            //result = tmp;// result=1          result= 2
            //head = head.next;// head=2.  head=3

//            ListNode tmp = head;//tmp=1 ,tmp=2
//            head = head.next;// head=2.  head=3
//            tmp.next = result;//1.next=null   2.next=1
//            result = tmp;// result=1          result= 2
//
        }
        return result;
    }

    public ListNode reverlist9(ListNode head) {
        ListNode result = new ListNode(0);
        while (head!= null) {
             ListNode temp=head.next;
            head.next=result;
            result=head;
            head=temp;
        }
        return result;
    }


    public ListNode reverseList5(ListNode head) {
        ListNode pre = null;
        while (head != null) {
            ListNode temp = head;
            //引用变了
            head = head.next;
            //temp和head已经不一样了
            temp.next = pre;
            pre = temp;

           /* ListNode temp=head.next;
            head.next=pre;
            pre=head;
            head=temp;*/


        }
        return pre;
    }


    public ListNode reverseList7(ListNode head) {
        ListNode pre = null;
        while (head != null) {
            //定义临时变量
            ListNode temp = head.next;
            //ListNode temp=head;
            //null<-1
            head.next = pre;
            // pre= null<-1
            pre = head;
            // head=2
            head = temp;
            //head=temp.next;
        }
        return pre;
    }

    /**
     * 错误用法
     *
     * @param head
     * @return
     */
    public ListNode reverseList6(ListNode head) {
        ListNode pre = null;
        while (head != null) {
            //错误用法
            ListNode tmp = head;
           /*
           //这个时候tmp的值已经跟着变了
            head.next=pre;
            pre=head;
            //tmp.next 第一次就变成了null
            head=tmp.next;

            */
            head = head.next;

            tmp.next = pre;
            pre = tmp;

        }
        return pre;
    }








    /**
     * 双指针迭代
     * 我们可以申请两个指针，第一个指针叫 pre，最初是指向 null 的。
     * 第二个指针 cur 指向 head，然后不断遍历 cur。
     * 每次迭代到 cur，都将 cur 的 next 指向 pre，然后 pre 和 cur 前进一位。
     * 都迭代完了(cur 变成 null 了)，pre 就是最后一个节点了。
     */
    private ListNode reverseList3(ListNode head) {
        //1->2->3->4->5
        //null<-1
        //null<1<2
        ListNode pre = null;
        ListNode cur = head;
        ListNode temp = null;
        while (cur != null) {
            //记录当前节点的下一个节点
            temp = cur.next;
            //当前节点指向pre
            cur.next = pre;
            //pre和cur节点都前进一位
            pre = cur;
            cur = temp;
        }
        return pre;
    }

    public ListNode test(ListNode head) {
        ListNode pre = null;
        ListNode next = null;
        while (head != null) {
            next = head.next;
            head.next = pre;
            pre = head;
            head = next;
        }
        return pre;
    }

    /**
     * 递归
     *
     * @param head
     * @return
     */
    private ListNode reverseList4(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        //如果链表是 1->2->3->4->5，那么此时的cur就是5
        ListNode cur = reverseList4(head.next);
        //而head是4，head的下一个是5，下下一个是空
        //所以head.next.next 就是5->4
        head.next.next = head;
        head.next = null;
        return cur;
    }


    /**
     * 循环当前node链表
     * 思路是找到最后一个为null的，然后放到新的链表里面 同时去掉这个节点
     *
     * @param head
     * @return
     */
    public ListNode reverseList1(ListNode head) {
        if (head == null) {
            return null;
        }
        //新的head
        ListNode newHead = null;
        //临时的temp 是为了循环给newHead进行赋值next的
        ListNode tempHead = null;
        //当前的node
        ListNode currentNode = head;
        while (true) {
            //循环到最后一个的
            if (currentNode.next == null) {
                if (newHead == null) {
                    newHead = currentNode;
                } else {
                    tempHead.next = currentNode;
                }
                break;
            }
            //找到当前链表为空的元素
            if (currentNode.next.next == null) {
                //找到第一个为空的
                if (newHead == null) {
                    newHead = currentNode.next;
                    tempHead = newHead;
                } else {
                    tempHead.next = currentNode.next;
                    tempHead = tempHead.next;
                }
                //去除当前找到的节点
                currentNode.next = null;
                //继续下个循环
                currentNode = head;
            } else {
                //继续循环
                currentNode = currentNode.next;
            }

        }
        return newHead;
    }


    public ListNode reverseList2(ListNode head) {
        if (head == null) {
            return null;
        }
        //新的head
        ListNode newHead = null;
        //当前的node
        ListNode currentNode = head;
        while (true) {
            //循环到最后一个的
            if (currentNode.next == null) {
                if (newHead == null) {
                    newHead = currentNode;
                } else {
                    //赋值
                    setNewNodeNext(newHead, currentNode, false);
                }
                break;
            }
            //找到当前链表为空的元素
            if (currentNode.next.next == null) {
                //找到第一个为空的
                if (newHead == null) {
                    newHead = currentNode.next;
                } else {
                    //赋值
                    setNewNodeNext(newHead, currentNode, true);
                }
                //去除当前找到的节点
                currentNode.next = null;
                //继续下个循环
                currentNode = head;
            } else {
                //继续循环
                currentNode = currentNode.next;
            }

        }
        return newHead;
    }

    private void setNewNodeNext(ListNode head, ListNode currentNode, boolean next) {
        ListNode temp = head;
        while (true) {
            if (temp.next == null) {
                if (next) {
                    temp.next = currentNode.next;
                } else {
                    temp.next = currentNode;
                }
                break;
            }
            temp = temp.next;
        }
    }

    public class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

}
