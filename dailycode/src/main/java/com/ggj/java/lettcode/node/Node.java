package com.ggj.java.lettcode.node;

/**
 * @author:gaoguangjin
 * @date:2018/7/13
 */
public class Node<T> {
    public T data;   		//数据域，可以存放对象
    public Node left;                   //指向左孩子
    public Node right;			//指向右孩子
    public void displsyndoe(){
        System.out.println(data +" ");
    }

    public Node(T data) {		//构造函数
        this.data = data;
    }
}
