package com.ggj.java.mycache.lrucache;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 实现一个LRUCache
 * <p>
 * 功能点：支持设置缓存大小，
 * 当缓存不够当时候，先去除end节点
 * 每次获取缓存的时候，当前节点remove到head节点上
 *
 * @author gaoguangjin
 */
@Slf4j
public class LRUCacheTwo {

    private int capacity;
    private int currentSize;
    private Node head = null;
    private Node tail = null;

    //["LRUCache","put","get","put","get","get"]
    public static void main(String[] args) {


    String a="10#10,13#3,17#6,11#10,5#9,10#13#2,19#2#3#5,25#8#9,22#5,5#1,30#11#9,12#7#5#8#9#4,30#9,3#9#10#10#6,14#3,1#3#10,11#8#2,14#1#5#4#11,4#12,24#5,18#13#7,23#8#12#3,27#2,12#5#2,9#13,4#8,18#1,7#6#9,29#8,21#5#6,30#1,12#10#4,15#7,22#11,26#8,17#9,29#5#3,4#11,30#12#4,29#3#9#6#3,4#1#10#3,29#10,28#1,20#11,13#3#3,12#3,8#10,9#3,26#8#7#5#13,17#2,27#11,15#12#9,19#2,15#3,16#1#12,17#9,1#6,19#4#5#5#8,1#11,7#5,2#9,28#1#2,2#7,4#4,22#7,24#9,26#13,28#11,26";
        LRUCacheTwo cache = new LRUCacheTwo(10/* 缓存容量 */);

        String[] dd = a.split("#");
        for (String s : dd) {
            if(s.contains(",")){
                cache.put(Integer.valueOf(s.split(",")[0]),Integer.valueOf(s.split(",")[1]));
            }else{
                cache.get(Integer.valueOf(s));
            }
        }
    }

    public LRUCacheTwo(int capacity) {
        this.capacity = capacity;
    }

    public int get(int key) {
        if (head == null) {
            return -1;
        }
        Node temp = head;
        while (temp != null) {
            //查到更新
            if (temp.key == key) {
                //节点指向变更
                removeCurrentNode(temp);
                setHead(temp);
                setTail();
                return temp.value;
            }
            temp = temp.next;
        }
        return -1;
    }

    public void put(int key, int value) {
        Node newNode = new Node(key, value);
        Node temp = head;
        while (temp != null) {
            //查到更新
            if (temp.key == key) {
                //节点指向变更
                removeCurrentNode(temp);
                //设置头
                setHead(newNode);
                setTail();
                return;
            }
            temp = temp.next;
        }
        if (currentSize == capacity) {
            removeTail();
        }
        //不存在，直接放到头
        setHead(newNode);
        setTail();
        currentSize++;
    }

    private void removeCurrentNode(Node node) {
        Node pre = node.pre;
        Node next = node.next;
        if (pre != null) {
            pre.next = next;
            if (next != null) {
                next.pre = pre;
            }
            //当前节点就是head指向的
        } else {
            head = next;
        }

    }

    public void setHead(Node node) {
        if (head == null) {
            head = node;
            return;
        }
        node.next=null;
        node.pre=null;

        Node temp = head;
        head = node;
        head.next = temp;
        temp.pre = head;
    }

    public void setTail() {
        if (tail == null) {
            tail = head;
            return;
        }
        Node temp = head;
        while (temp != null) {
            //temp是最后一个节点
            if (temp.next == null) {
                tail = temp;
            }
            temp = temp.next;
        }
    }

    //删除最后一个
    public void removeTail() {
        if (tail.pre != null) {
            Node temp = tail.pre;
            temp.next = null;
            tail = temp;
        } else {
            head = null;
            tail = null;
        }
        currentSize--;
    }

    public class Node {
        private Node pre;
        private Node next;
        private int key;
        private int value;

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }


}
