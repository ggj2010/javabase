package com.ggj.java.lettcode.alibaba;

import com.ggj.java.rpc.demo.netty.usezk.client.MyPoolThreadFactory;
import lombok.Data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author gaoguangjin
 */
public class Test {
    /**
     * 评测题目:
     * 以下文件提供给候选人作为输入
     * <p>
     * 1.txt
     * <p>
     * 2000102,100,98.3
     * <p>
     * 2000103,101,73.3
     * 2000104,102,98.3
     * <p>
     * 2.txt
     * 2000106,101,45.3
     * <p>
     * 3.txt
     * 2000105,100,101.3
     * <p>
     * <p>
     * 题目：
     * <p>
     * 假设本地有一个文件夹，文件夹下面有若干文件（文件数大于50小于100），文件的存储格式是文本格式（后缀名是.txt)，文件的大小每个文件不会超过100k。
     * 文件格式如下：
     * 2000102，100，98.3
     * 2000103，101，73.3
     * 2000104，102，98.3
     * 2000105，100，101.3
     * 2000106，101，45.3
     * ......
     * <p>
     * <p>
     * 文件格式说明：文件每行都由三列构成，第一列是一个id，第二列是分组groupId, 第三列是指标quota。
     * id的数据类型是String, groupId的数据类型String, quota的数据类型float。
     * <p>
     * <p>
     * <p>
     * 功能要求：
     * 1.对文件夹下面所有文件的内容进行合并排序，输出按照分组升序排序之后，每个分组下面的最小指标值。比如上面的数据输出结果为：
     * <p>
     * 100，2000102，98.3
     * 101，2000106，45.3
     * 102，2000104，98.3
     * <p>
     * <p>
     * 非功能要求
     * 1.文件读取要有线程池来执行，线程池的大小固定为10，文件内容需要存储到指定的内容数据结构当中。
     * <p>
     * 2.查找要求有独立线程来执行，直接消费读取线程池产生的内存数据结构。
     * <p>
     * 3.文件读取和排序要求并发作业，文件读取只要产生了数据，就可以把数据交给排序线程进行消费，计算最小值。
     * <p>
     * <p>
     * <p>
     * 代码结构要求
     * 1.重上面的要求语意里面抽象出合适的设计模式。
     * <p>
     * 2.需要考虑多线程的并发控制，同步机制。
     * <p>
     * 3.代码实现只能用JDK1.6或者1.8自带的工具类。
     *
     * @param args
     */


    private final static String FILE_PATH = "/data/test/";
    private final static String PARSE_FILE_POOL_NAME = "parsefile-pool";
    private static Map<String, Content> fileMap = new ConcurrentHashMap<>();
    private static ThreadPoolExecutor praseFilethreadPoolExecutor = new ThreadPoolExecutor(10, 10, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new ThreadFactory() {
        private AtomicInteger atomicInteger = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            ThreadGroup group = Thread.currentThread().getThreadGroup();
            Thread thread = new Thread(group, r, String.format("%s-%d", PARSE_FILE_POOL_NAME, atomicInteger.getAndIncrement()));
            return thread;
        }
    }, new ThreadPoolExecutor.AbortPolicy());


    public void main(String[] args) {
        parseFile();


    }

    private static void parseFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            throw new IllegalArgumentException(FILE_PATH + "not exists");
        }
        if (!file.isDirectory()) {
            throw new IllegalArgumentException(FILE_PATH + "is not  directory");
        }

        String[] filePathArray = file.list();
        for (String path : filePathArray) {
            praseFilethreadPoolExecutor.submit(new Thread() {
                @Override
                public void run() {
                    try (BufferedReader fileInputStream = new BufferedReader(new FileReader(new File(path)))) {
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }


    }


    @Data
    private class Content {
        //对文件夹下面所有文件的内容进行合并排序，输出按照分组升序排序之后，每个分组下面的最小指标值
        private String id;
        //指标
        private String groupId;
        //指标
        private float quota;
    }


}
