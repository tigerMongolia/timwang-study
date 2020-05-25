package com.timwang.concurrent.core.atomic;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author wangjun
 * @date 2020-04-22
 */
public class D_ABADemo {

    //演示ABA问题
    @Test
    public void demo2() {
        CountDownLatch cdl = new CountDownLatch(1);
        AtomicInteger atomicInteger = new AtomicInteger(0);

        new Thread(() -> {
            System.out.println(atomicInteger.compareAndSet(0, 1));
            System.out.println(atomicInteger.compareAndSet(1, 0));
            cdl.countDown();
        }).start();

        new Thread(() -> {
            try {
                cdl.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(atomicInteger.compareAndSet(0, 1));
            System.out.println(atomicInteger.get());
        }).start();

    }

}
