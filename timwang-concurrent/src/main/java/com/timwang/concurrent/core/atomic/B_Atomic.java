package com.timwang.concurrent.core.atomic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangjun
 * @date 2020-04-22
 */
public class B_Atomic {
    private static AtomicInteger m = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch cdl = new CountDownLatch(2);

        Thread t1 = new Thread(() -> {
            for (int j = 0; j < 5000; j++) {
                m.getAndIncrement();
            }
            cdl.countDown();
        });
        Thread t2 = new Thread(() -> {
            for (int j = 0; j < 5000; j++) {
                m.getAndIncrement();
            }
            cdl.countDown();
        });
        t1.start();
        t2.start();

        cdl.await();
        System.out.println("result=" + m.get());
    }
}
