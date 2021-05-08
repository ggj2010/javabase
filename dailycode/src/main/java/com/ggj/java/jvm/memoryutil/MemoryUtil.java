package com.ggj.java.jvm.memoryutil;

import org.apache.lucene.util.RamUsageEstimator;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;


/**
 * https://blog.csdn.net/vision583934061/article/details/106000561
 * https://www.cnblogs.com/feng-gamer/p/10543004.html
 * https://blog.csdn.net/yunqiinsight/article/details/80431831
 * https://blog.csdn.net/qqHJQS/article/details/100944914
 *
 *
 * -XX:-UseCompressedOops
 * @author gaoguangjin
 */
public class MemoryUtil {




    public static void main(String[] args) {
        //计算指定对象本身在堆空间的大小，单位字节
        System.out.println(RamUsageEstimator.shallowSizeOf(new NodeA()));
        //计算指定对象及其引用树上的所有对象的综合大小，单位字
        System.out.println(RamUsageEstimator.sizeOf(new NodeA()));
        //计算指定对象及其引用树上的所有对象的综合大小，返回可读的结果
        System.out.println(RamUsageEstimator.humanSizeOf(new NodeA()));

        NodeA obj=new NodeA();
        //查看对象内部信息
        print(ClassLayout.parseInstance(obj).toPrintable());
        //查看对象外部信息
        print(GraphLayout.parseInstance(obj).toPrintable());
        //获取对象总大小
       // print("obj totalsize : " + GraphLayout.parseInstance(obj).totalSize()+" bytes");
    }

    static void print(String message) {
        System.out.println(message);
        System.out.println("-------------------------");
    }

    static class NodeA{
        //4
        int age;
        //4
        NodeB nodeB=new NodeB();

        //markword 12
        // 实 4+4
        //padding 4
        //taotal 24

    }

    /**
     * total 12+16 28
     * padding +4
     * taotal =32
     */
    static class NodeB{
        //4
        private Integer a;
        //4
        private Integer b;
        //4
        private Integer c;
        //4
        private Integer d;
    }
}
