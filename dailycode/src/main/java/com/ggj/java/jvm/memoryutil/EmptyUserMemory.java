package com.ggj.java.jvm.memoryutil;

/**
 * 运行时环境：-XX:+PrintGCDetails -Xms20M -Xmx20M -Xmn10M -XX:SurvivorRatio=2
 * @author gaoguangjin
 */
public class EmptyUserMemory {


    /**
     * Heap
     *  PSYoungGen      total 7680K, used 757K [0x00000007bf600000, 0x00000007c0000000, 0x00000007c0000000)
     *   eden space 5120K, 14% used [0x00000007bf600000,0x00000007bf6bd6b0,0x00000007bfb00000)
     *   from space 2560K, 0% used [0x00000007bfd80000,0x00000007bfd80000,0x00000007c0000000)
     *   to   space 2560K, 0% used [0x00000007bfb00000,0x00000007bfb00000,0x00000007bfd80000)
     *  ParOldGen       total 10240K, used 0K [0x00000007bec00000, 0x00000007bf600000, 0x00000007bf600000)
     *   object space 10240K, 0% used [0x00000007bec00000,0x00000007bec00000,0x00000007bf600000)
     *  Metaspace       used 2651K, capacity 4486K, committed 4864K, reserved 1056768K
     *   class space    used 286K, capacity 386K, committed 512K, reserved 1048576K
     * @param args
     */
    public static void main(String[] args) {
        //初始化运行堆eden区会占用757K ，用来存放string对象的

        //-Xms20M -Xmx20M -Xmn10M（新生代和老年代各为10M）
        // SurvivorRatio=2 新生代里面堆分配比例 1:1:2
        // 则edn=5m survivor from =2.5m survivor to=2.5m

        //从上门的gc 记录可以看出，新生代可以用内存为7680K=edn+survivor from=7.5
    }
}
