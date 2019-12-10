package com.timwang.concurrent.core.util;

/**
 * @author wangjun
 * @date 2019-10-03
 */
public class Singleton {
    public static Integer STATUS = 1;
    private Singleton() {
        System.out.println("Singleton is create");
    }

    private static Singleton instance = new Singleton();

    public static Singleton getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        System.out.println(Singleton.STATUS);
    }
}
