package com.tim.wang.sourcecode.collections.hashmap;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangjun
 * @date 2020-06-12
 */
public class HashMapUtil {

    public static void main(String[] args) {
        debugHashMapConstructMethod();
        debugHashMapPutMethod();
    }

    private static void debugHashMapConstructMethod() {
        // 只是设置加载因子0.75
        Map map = new HashMap();
    }

    private static void debugHashMapPutMethod() {
        Map map = new HashMap();
        map.put("key", "value");
    }

}
