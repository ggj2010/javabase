package com.ggj.java.lock.mylock;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author:gaoguangjin
 * @Description:liyon
 * @Email:335424093@qq.com
 * @Date 2016/4/8 11:10
 */
public class CycleBarrierLock {
    int number;
    //开启10个线程
    CyclicBarrier cyclicBarrier=new CyclicBarrier(10);

    public static void main(String[] args) {
    new CycleBarrierLock().start();
    }

    public  void start(){
        ExecutorService pool = Executors.newFixedThreadPool(50);



    }


}
