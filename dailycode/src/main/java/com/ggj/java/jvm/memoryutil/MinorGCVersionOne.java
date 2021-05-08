package com.ggj.java.jvm.memoryutil;


/**
 * 运行时环境：-XX:+PrintGCDetails -Xms20M -Xmx20M -Xmn10M -XX:SurvivorRatio=2 -XX:+UseSerialGC -XX:+PrintFlagsFinal
 * jdk 1.8 UseParallelGC 新生代并行收集器是默认开启的 需要关闭下，不然会出现 Full GC (Ergonomics)，导致我们测试不准
 * jstat -gc pid 1000
 * @author gaoguangjin
 */
public class MinorGCVersionOne {
    public static final int _1MB = 1024 * 1024;

    /**
     * -Xms20M -Xmx20M -Xmn10M（新生代和老年代各为10M）
     * SurvivorRatio=2 新生代里面堆分配比例 1:1:2
     * 则edn=5m survivor from =2.5m survivor to=2.5m
     * 新生代可以用内存为edn+survivor from=7.5m
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        one();
    }

    /**
     * setp1 begin
     * setp1 end
     * setp2 begin
     * setp2 end
     * setp3 begin
     * [GC (Allocation Failure) [DefNew: 4750K->2331K(7680K), 0.0064056 secs] 4750K->4379K(17920K), 0.0064477 secs] [Times: user=0.00 sys=0.01, real=0.00 secs]
     * setp3 end
     * setp4 begin
     * setp4 end
     * setp5 begin
     * [GC (Allocation Failure) [DefNew: 5503K->2048K(7680K), 0.0037545 secs] 7551K->7449K(17920K), 0.0037756 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
     * setp5 end
     * setp6 begin
     * [GC (Allocation Failure) [DefNew (promotion failed) : 6244K->4196K(7680K), 0.0014613 secs][Tenured: 7449K->7449K(10240K), 0.0014157 secs] 11645K->11545K(17920K), [Metaspace: 2648K->2648K(1056768K)], 0.0029014 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
     * [Full GC (Allocation Failure) [Tenured: 7449K->7436K(10240K), 0.0011866 secs] 11545K->11532K(17920K), [Metaspace: 2648K->2648K(1056768K)], 0.0011993 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
     * Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
     * at com.ggj.jvm.MinorGCVersionOne.one(MinorGCVersionOne.java:86)
     * at com.ggj.jvm.MinorGCVersionOne.main(MinorGCVersionOne.java:21)
     * Heap
     * def new generation   total 7680K, used 4401K [0x00000007bec00000, 0x00000007bf600000, 0x00000007bf600000)
     * eden space 5120K,  85% used [0x00000007bec00000, 0x00000007bf04c590, 0x00000007bf100000)
     * from space 2560K,   0% used [0x00000007bf380000, 0x00000007bf380000, 0x00000007bf600000)
     * to   space 2560K,   0% used [0x00000007bf100000, 0x00000007bf100000, 0x00000007bf380000)
     * tenured generation   total 10240K, used 7436K [0x00000007bf600000, 0x00000007c0000000, 0x00000007c0000000)
     * the space 10240K,  72% used [0x00000007bf600000, 0x00000007bfd431b8, 0x00000007bfd43200, 0x00000007c0000000)
     * Metaspace       used 2679K, capacity 4486K, committed 4864K, reserved 1056768K
     * class space    used 289K, capacity 386K, committed 512K, reserved 1048576K
     *
     * @throws Exception
     */
    public static void one() throws Exception {
        byte[] a, b, c, d, e, f;
        System.in.read();
        //初始化运行时候jvm 会占用1m左右
        System.out.println("setp1 begin");
        a = new byte[2 * _1MB];
        System.out.println("setp1 end");
        //edn use 2m
        // S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU   YGC     YGCT    FGC    FGCT     GCT
        //2560.0 2560.0  0.0    0.0    5120.0   2600.4   10240.0      0.0     4480.0 779.2  384.0   76.4       0    0.000   0      0.000   -          -    0.000

        sleep();
        System.out.println("setp2 begin");
        b = new byte[2 * _1MB];
        System.out.println("setp2 end");
        sleep();
        //edn use 4m
        // S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU   YGC     YGCT    FGC    FGCT     GCT
        //2560.0 2560.0  0.0    0.0    5120.0   4648.4   10240.0      0.0     4480.0 779.2  384.0   76.4       0    0.000   0      0.000   -          -    0.000

        System.out.println("setp3 begin");
        c = new byte[2 * _1MB];
        System.out.println("setp3 end");
        sleep();
        // S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU   YGC     YGCT    FGC    FGCT     GCT
        //2560.0 2560.0  0.0   2331.0  5120.0   2149.3   10240.0     2048.0   4864.0 2646.5 512.0  286.0       1    0.003   0      0.000   -          -    0.003
        // edn最大为5m,目前已经使用4m  edn区域不够分配 2m,触发minor ygc,S1第一次使用
        // minor gc 后edn存活的4m对象无法全部放入S1，只能放入2m，JVM就启动了内存分配的担保机制,end剩余的2m提前转移到老年代里面
        //[GC (Allocation Failure 申请edn空间失败) [DefNew: 4648K->2331K(7680K), 0.0030856 secs] 4648K->4379K(17920K), 0.0031139 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
        // edn  2m ;  S1U 2m ; old 2m


        System.out.println("setp4 begin");
        d = new byte[1 * _1MB];
        System.out.println("setp4 end");
        sleep();

        // S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU   YGC     YGCT    FGC    FGCT     GCT
        //2560.0 2560.0  0.0   2331.0  5120.0   3173.3   10240.0     2048.0   4864.0 2646.5 512.0  286.0       1    0.003   0      0.000   -          -    0.003
        // edn  3m (step3的edn 2m +此次申请1m );  S1U 2m ; old 2m


        System.out.println("setp5 begin");
        e = new byte[4 * _1MB];
        System.out.println("setp5 end");
        sleep();
        // S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU   YGC     YGCT    FGC    FGCT     GCT
        //2560.0 2560.0 2048.3  0.0    5120.0   4196.8   10240.0     5401.3   4864.0 2646.9 512.0  286.0       2    0.007   0      0.000   -          -    0.007
        //[GC (Allocation Failure) [DefNew: 5504K->2048K(7680K), 0.0034966 secs] 7552K->7449K(17920K), 0.0035136 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]

        // 和setp 3类似  edn区域不够分配,触发minor ygc ,edn+s1存活的拷贝到s2，但是 s2区域不够存储，触发minorgc
        //  edn  4m ;  S0U 2m ; old 5m(2+3)


        System.out.println("setp6 begin");
        f = new byte[3 * _1MB];
        System.out.println("setp6 end");
        sleep();

        // [GC (Allocation Failure) [DefNew (promotion failed) : 6245K->4196K(7680K), 0.0013853 secs][Tenured: 7449K->7449K(10240K), 0.0013333 secs] 11646K->11545K(17920K), [Metaspace: 2646K->2646K(1056768K)], 0.0027458 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
        // 和setp 3类似，首先是通过担保机制 直接进去老年代，但是老年代需要有9m，老年代内存不够，触发full gc
        // [Full GC (Allocation Failure) [Tenured: 7449K->7436K(10240K), 0.0011657 secs] 11545K->11532K(17920K), [Metaspace: 2646K->2646K(1056768K)], 0.0011813 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
        System.in.read();
    }

    private static void sleep() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
    // def new generation总内存内存为 edn+一个survivor from内存
    // 【 GC 前该区域已使用容量 -> GC 后该区域已使用容量 (该区域内存总容量) 】  【GC 前Java堆已使用容量 -> GC后Java堆已使用容量 （Java堆总容量）】

/**
 * jstat -gc 89209 1000
 * S0C：第一个幸存区的大小
 * S1C：第二个幸存区的大小
 * S0U：第一个幸存区的使用大小
 * S1U：第二个幸存区的使用大小
 * EC：伊甸园区的大小
 * EU：伊甸园区的使用大小
 * OC：老年代大小
 * OU：老年代使用大小
 * MC：方法区大小
 * MU：方法区使用大小
 * CCSC:压缩类空间大小
 * CCSU:压缩类空间使用大小
 * YGC：年轻代垃圾回收次数
 * YGCT：年轻代垃圾回收消耗时间
 * FGC：老年代垃圾回收次数
 * FGCT：老年代垃圾回收消耗时间
 * GCT：垃圾回收消耗总时间
 *
 * S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU   YGC     YGCT    FGC    FGCT    CGC    CGCT     GCT
 * 2560.0 2560.0  0.0    0.0    5120.0   552.4    10240.0      0.0     4480.0 779.2  384.0   76.4       0    0.000   0      0.000   -          -    0.000
 * 2560.0 2560.0  0.0    0.0    5120.0   2600.4   10240.0      0.0     4480.0 779.2  384.0   76.4       0    0.000   0      0.000   -          -    0.000
 * 2560.0 2560.0  0.0    0.0    5120.0   4648.4   10240.0      0.0     4480.0 779.2  384.0   76.4       0    0.000   0      0.000   -          -    0.000
 * 2560.0 2560.0  0.0   2331.0  5120.0   2149.3   10240.0     2048.0   4864.0 2646.5 512.0  286.0       1    0.003   0      0.000   -          -    0.003
 * 2560.0 2560.0  0.0   2331.0  5120.0   3173.3   10240.0     2048.0   4864.0 2646.5 512.0  286.0       1    0.003   0      0.000   -          -    0.003
 * 2560.0 2560.0 2048.3  0.0    5120.0   4196.8   10240.0     5401.3   4864.0 2646.9 512.0  286.0       2    0.007   0      0.000   -          -    0.007
 *
 *
 * */







