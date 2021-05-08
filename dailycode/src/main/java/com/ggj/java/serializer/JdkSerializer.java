
package com.ggj.java.serializer;

import com.ggj.java.serializer.bean.DeSerializerBean;
import com.ggj.java.serializer.bean.SerializerBean;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * java序列化
 *
 * @author:gaoguangjin
 * @date:2018/4/21
 */
@Slf4j
public class JdkSerializer {
    private final static String PATH = "a.txt";
    private final static File file = new File(PATH);

    public static void main(String[] args) {
       /* serialize();
        deserialize();*/

        byteArrayObject();
    }

    private static void byteArrayObject() {
        SerializerBean serializerBean = new SerializerBean();
        serializerBean.setName("高广金");
        serializerBean.setAge(18);

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream= null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(serializerBean);
            System.out.println(new String(byteArrayOutputStream.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void serialize() {
        SerializerBean serializerBean = new SerializerBean();
        serializerBean.setName("高广金");
        serializerBean.setAge(18);

        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            oos.writeObject(serializerBean);
            log.info("序列化前：{}", serializerBean.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void deserialize() {
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            // 如果serialVersionUID 不一致，是无法发序列化的。
            // DeSerializerBean deSerializerBean = (DeSerializerBean) ois.readObject();
            SerializerBean serializerBean = (SerializerBean) ois.readObject();
            log.info("反序列化：{}", serializerBean.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
