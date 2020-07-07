package com.timwang.concurrent.core.thread;
import	java.util.concurrent.Executors;

import java.util.concurrent.ExecutorService;

/**
 * @author wangjun
 * @date 2020-07-07
 */
public class ThreadFactoryClass {
    /* POOL_NUM */
    private static int POOL_NUM = 10;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.execute(() -> System.out.println("this is runnable call from "));
    }
}
