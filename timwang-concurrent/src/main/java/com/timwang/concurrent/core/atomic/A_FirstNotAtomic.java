package com.timwang.concurrent.core.atomic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wangjun
 * @date 2020-04-22
 */
public class A_FirstNotAtomic {
    private static int m = 0;

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch cdl = new CountDownLatch(2);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int j = 0; j < 5000; j++) {
                    m++;
                }
                cdl.countDown();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int j = 0; j < 5000; j++) {
                m++;
            }
            cdl.countDown();
        });
        t1.start();
        t2.start();

        cdl.await();
        System.out.println("result=" + m);
    }
}
