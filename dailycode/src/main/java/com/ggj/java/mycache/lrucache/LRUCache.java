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
public class LRUCache<T> {

    private Node<T> head;
    private Node<T> end;
    private volatile int currentCacheSize;
    private volatile int maxCacheSize;

    public LRUCache(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
    }

    @Override
    public String toString() {
        Node currentNod = head;
        StringBuilder stringBuilder=new StringBuilder();
        while (currentNod != null) {
            stringBuilder.append(currentNod.key+"->");
            currentNod = currentNod.next;
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        LRUCache<User> lruCache = new LRUCache(3);
        lruCache.put("1", new User(1, "111"));
        lruCache.put("2", new User(2, "222"));

        lruCache.put("3", new User(3, "333"));  // 3->2->1
        log.info("put 3 {}\n",lruCache);

        lruCache.put("4", new User(4, "44"));  // 4->3->2
        log.info("put 4 {} \n",lruCache);

        lruCache.put("5", new User(5, "55"));  // 5->4->3
        log.info("put 5 {}\n",lruCache);

        lruCache.get("3");                                    // 3->5->4
        log.info("get 3 {}\n",lruCache);

        lruCache.put("5", new User(5, "new55"));// 5->3->4
        log.info("put 5 {}\n",lruCache);

        lruCache.put("4", new User(4, "new4"));// 4->5->3
        log.info("put 4 {}\n",lruCache);
    }

    private synchronized T get(String key) {
        if (head == null) {
            return null;
        }
        Node currentNod = head;
        while (currentNod != null) {
            if (currentNod.key.equals(key)) {
                log.info("get exists cache {}", key);
                remove(currentNod);
                setHead(currentNod);
                setEnd(currentNod);
                return (T) currentNod.data;
            }
            currentNod = currentNod.next;
        }
        return null;
    }

    private void remove(Node currentNod) {
        //去除当前节点
        Node preNode = currentNod.pre;
        Node nexNode = currentNod.next;
        if (preNode != null) {
            preNode.next = nexNode;
            if(nexNode!=null) {
                nexNode.pre = preNode;
            }
        }
        log.info("remove exists cache {}", currentNod.key);
    }

    private synchronized T put(String key, T data) {
        T oldResult = null;
        Node currentNod = head;
        while (currentNod != null) {
            if (currentNod.key.equals(key)) {
                log.info("put exists cache key={}", currentNod.key);
                oldResult = (T) currentNod.data;
                currentNod.data = data;
                //去除当前节点
                remove(currentNod);
                setHead(currentNod);
                setEnd(currentNod);
                return oldResult;
            }
            currentNod = currentNod.next;
        }

        // expires node
        if (currentCacheSize >= maxCacheSize) {
            Node needRemoveNext = head;
            //最后一个节点去掉
            while (!needRemoveNext.key.equals(end.key)) {
                needRemoveNext = needRemoveNext.next;
            }
            log.info("caache is full remove endkey {}", end.key);
            needRemoveNext.pre.next = null;
            currentCacheSize--;
        }
        Node<T> node = new Node<T>(key, data);
        setHead(node);
        setEnd(node);
        currentCacheSize++;
        return null;
    }

    private void setEnd(Node<T> node) {
        if (end == null) {
            end = node;
        } else {
            Node currentNode = head;
            //最后一个节点去掉
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            end = currentNode;
        }
    }

    private void setHead(Node<T> node) {
        log.info("reset header{}", node.key);
        if (head == null) {
            head = node;
        } else {
            Node<T> next = head;
            head = node;
            head.next = next;
            next.pre = head;
        }
    }

    @Data
    static class Node<T> {
        Node next;
        Node pre;
        T data;
        String key;

        public Node(String key, T data) {
            this.data = data;
            this.key = key;
        }

        @Override
        public String toString() {
            return "Node{" +
                    ", key='" + key + '\'' +
                    '}';
        }
    }


    @Data
    static class User {
        private int id;
        private String name;

        public User(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
