package com.ggj.java;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.io.IOException;

/**
 * @author gaoguangjin
 */
@Slf4j
public class UniqMachineId {

    private static int machineId = 1;
    private static int maxMachineId = 1024;
    private final static String UNIQ_MACHINE_KEY = "uniq_machineid";

    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost");
        jedis.auth("123");
        for (int i = 1; i <= maxMachineId; i++) {
            if (!jedis.exists(UNIQ_MACHINE_KEY + i)) {
                jedis.set(UNIQ_MACHINE_KEY + i, i + "");
                machineId = i;
                log.info("add :{}", machineId);
                break;
            }
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                jedis.del(UNIQ_MACHINE_KEY + machineId);
                log.info("del :{}", machineId);
            }
        });

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
