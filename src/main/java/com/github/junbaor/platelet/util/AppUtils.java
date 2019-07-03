package com.github.junbaor.platelet.util;

import java.util.LinkedHashMap;

public class AppUtils {

    @SuppressWarnings("unchecked")
    public static <K, V> LinkedHashMap<K, V> map(Object... args) {
        if (args == null || args.length % 2 != 0) {
            throw new IllegalArgumentException();
        }
        LinkedHashMap<K, V> m = new LinkedHashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            m.put((K) args[i], (V) args[i + 1]);
        }
        return m;
    }

}
