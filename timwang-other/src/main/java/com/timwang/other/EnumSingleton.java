package com.timwang.other;

/**
 * @author wangjun
 * @date 2020-03-14
 */
public enum EnumSingleton {
    /**
     *
     */
    INSTANCE;

    private Singleton instance;

    EnumSingleton() {
        instance = new Singleton();
    }
    public Singleton getInstance() {
        return instance;
    }
}
