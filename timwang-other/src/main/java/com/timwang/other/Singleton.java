package com.timwang.other;

/**
 * @author wangjun
 * @date 2020-03-13
 */
public class Singleton {

    private static Singleton instance = new Singleton();

    public static Singleton getInstance() {
        return instance;
    }

    public static Singleton getInstance2() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public static synchronized  Singleton getInstance3() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public static Singleton getInstance4() {
        return SingletonHandler.S_INSTANCE;
    }

    private static class SingletonHandler {
        private static final Singleton S_INSTANCE = new Singleton();
    }
}
