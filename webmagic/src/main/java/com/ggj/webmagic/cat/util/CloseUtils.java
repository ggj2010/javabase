package com.ggj.webmagic.cat.util;

import java.io.Closeable;
import java.io.IOException;

public class CloseUtils {
    private CloseUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void closeIO(Closeable... closeables) {
        if (closeables != null) {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    try {
                        closeable.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void closeIOQuietly(Closeable... closeables) {
        if (closeables != null) {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    try {
                        closeable.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
    }
}
