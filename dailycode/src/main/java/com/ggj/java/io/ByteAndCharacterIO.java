
package com.ggj.java.io;

import java.io.*;

/**
 * @author:gaoguangjin
 * @date:2018/4/11
 */
public class ByteAndCharacterIO {

    private static final String PATH = "/Users/gaoguangjin/Desktop/";
    private static final String FILE_ONE = "file1.txt";
    private static final String FILE_TWO = "file2.txt";

    public static void main(String[] args) {
        outPutStreamWithOutputStreamWriter();
    }

    /**
     * 输出字节流和输出字符流 字节流在输出的时候直接操作文件本身。 字符流在输出的时候会使用缓冲区，
     * 最直观的就是如果我们在写文件的时候，如果使用字符流，不flush或者close（），内容
     * 是不会写到文件里面的，而字节流输出时候是实时的。
     */
    private static void outPutStreamWithOutputStreamWriter() {
        OutputStreamWriter osw = null;
        OutputStream os = null;
        try {
            //boolean append 是否追加方式
            osw = new OutputStreamWriter(new FileOutputStream(new File(PATH + FILE_ONE),true));
            osw.write("bbb");
            //
            System.in.read();
            // flush 才会从缓冲器写入文件
            osw.flush();
            os = new FileOutputStream(new File(PATH + FILE_TWO));
            os.write(97);
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (osw != null) {
                try {
                    osw.close();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
