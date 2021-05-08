package com.ggj.java.queue;

import lombok.Data;
import org.apache.commons.lang.time.DateFormatUtils;

import java.io.IOException;
import java.util.Date;

/**
 * 时钟轮
 * @author gaoguangjin
 */
public class MyWheel {


    private Node[] wheelNodeArray;
    //环总大小
    private int wheelSecondsSize;
    //当前size
    private static int currentSize = 1;

    public MyWheel(int wheelSecondsSize) {
        wheelNodeArray = new Node[wheelSecondsSize];
        this.wheelSecondsSize = wheelSecondsSize;
    }


    @Data
    private static class Node {
        private Node next;
        private Object data;

        public Node(Object object) {
            this.data = object;
        }

        public Node(Node next, Object object) {
            this.data = object;
            this.next = next;
        }
    }

    public void add(Object object) {
        Node node = wheelNodeArray[(currentSize - 1) % wheelSecondsSize];
        if (node == null) {
            node = new Node(object);
            wheelNodeArray[(currentSize - 1) % wheelSecondsSize] = node;
        } else {
            while (node.next==null||(node = node.next) == null) {
                node.next = new Node(object);
            }
        }
    }

    public Node get() {
        int size = currentSize % wheelSecondsSize;
        Node node = wheelNodeArray[size];
        wheelNodeArray[size] = null;
        return node;
    }


    public static void main(String[] args) throws InterruptedException {

        MyWheel myWheel=new MyWheel(30);

        new Thread(()->{
            while (true){
                Node node = myWheel.get();
                currentSize++;
                if(node!=null){
                    while (true){
                        if(node==null){
                            break;
                        }
                        dipalyCurrentTime("延时"+node.getData());
                        node=node.next;
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();



        myWheel.add("15-1s");
        myWheel.add("15-2s");
        dipalyCurrentTime("执行前 15");
        Thread.sleep(1000);

        myWheel.add("16-1s");
        myWheel.add("16-2s");

        dipalyCurrentTime("执行前 16");
        Thread.sleep(1000);
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        myWheel.add("17s");
        dipalyCurrentTime("执行前 17");

        Thread.sleep(1000);
        myWheel.add("18s");
        dipalyCurrentTime("执行前 18");
    }

    private static void dipalyCurrentTime(String str) {
        System.out.println(str+"==="+DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
    }

}
