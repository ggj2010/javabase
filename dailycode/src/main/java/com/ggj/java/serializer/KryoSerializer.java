
package com.ggj.java.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.ggj.java.serializer.bean.SerializerBean;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author:gaoguangjin
 * @date:2018/4/21
 */
@Slf4j
public class KryoSerializer {
    public static void main(String[] args) throws Exception {
        Kryo kryo = new Kryo();
        SerializerBean serializerBean = new SerializerBean();
        serializerBean.setName("高广金");
        serializerBean.setAge(18);
        Output output = new Output(new FileOutputStream("/tmp/test.bin"));
        kryo.writeObject(output, serializerBean);
        output.flush();
        output.close();
        log.info("序列化前：{}", serializerBean.toString());
        SerializerBean deseriable = kryo.readObject(new Input(new FileInputStream("/tmp/test.bin")),
                SerializerBean.class);
        log.info("反序列化：{}", deseriable.toString());
    }
}
