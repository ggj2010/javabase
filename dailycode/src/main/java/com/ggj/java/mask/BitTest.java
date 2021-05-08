package com.ggj.java.mask;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author gaoguangjin
 */
public class BitTest {
    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int CAPACITY   = (1 << COUNT_BITS) - 1;

    // runState is stored in the high-order bits
    //ctl变量将 workerCount（工作线程数量）和 runState（运行状态）两个字段压缩在一起
    //ThreadPoolExecutor用3个比特位表示runState， 29个比特位表示workerCount。因此这
    private static final int RUNNING    = -1 << COUNT_BITS;
    private static final int SHUTDOWN   =  0 << COUNT_BITS;
    private static final int STOP       =  1 << COUNT_BITS;
    private static final int TIDYING    =  2 << COUNT_BITS;

    public static void main(String[] args) {

        System.out.println(COUNT_BITS);


         final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));


        System.out.println(new AtomicInteger(ctlOf(RUNNING, 0)));
        System.out.println(new AtomicInteger(ctlOf(RUNNING, 2)));
        System.out.println(new AtomicInteger(ctlOf(STOP, 3)));
        System.out.println(new AtomicInteger(ctlOf(STOP, 4)));



    }


    private static int ctlOf(int rs, int wc) { return rs | wc; }

}
