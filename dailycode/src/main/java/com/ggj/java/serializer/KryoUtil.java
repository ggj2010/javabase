package com.ggj.java.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;

/**
 * @author gaoguangjin
 */
@Slf4j
public class KryoUtil {
    private static ThreadLocal<Kryo> kryoLocal = new ThreadLocal<Kryo>();

    public static <T>byte[] serialize(T object) {
        if (kryoLocal.get() == null) {
            kryoLocal.set(new Kryo());
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = null;
        byte[] resultByte = null;
        try {
            output = new Output(baos);
            kryoLocal.get().writeObject(output, object);
            output.flush();
            resultByte = baos.toByteArray();
        } catch (Exception e) {
            log.error("", e);
        } finally {
            try {
                output.close();
            } catch (Exception e1) {
                log.error("", e1);
            }
        }
        return resultByte;

    }

    public static <T> T deserialize(byte[] request, Class<T> type) {
        if (kryoLocal.get() == null) {
            kryoLocal.set(new Kryo());
        }
        Input input = new Input(request);
        return kryoLocal.get().readObjectOrNull(input, type);
    }

}
