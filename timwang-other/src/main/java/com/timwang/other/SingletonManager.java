package com.timwang.other;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangjun
 * @date 2020-03-14
 */
public class SingletonManager {
    private static Map<String, Object> objMap = new HashMap<>();

    public static void registerService(String key, Object instance) {
        if (!objMap.containsKey(key)) {
            objMap.put(key, instance);
        }
    }

    public static Object get(String key) {
        return objMap.get(key);
    }
}
