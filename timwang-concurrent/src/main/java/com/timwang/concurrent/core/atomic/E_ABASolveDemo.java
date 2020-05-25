package com.timwang.concurrent.core.atomic;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author wangjun
 * @date 2020-04-22
 */
public class E_ABASolveDemo {
    //使用AtomicStampedReference类解决ABA问题
    @Test
    public void demo3() {
        AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(3000, 1);
        CountDownLatch cdl = new CountDownLatch(1);

        new Thread(() -> {
            int stamp = atomicStampedReference.getStamp();
            System.out.println("线程1：" + stamp);
            cdl.countDown();

            atomicStampedReference.compareAndSet(atomicStampedReference.getReference(),3001,
                    atomicStampedReference.getStamp(),
                    atomicStampedReference.getStamp() + 1);
            System.out.println("线程1：" + atomicStampedReference.getStamp());

            atomicStampedReference.compareAndSet(3001,
                    3000,
                    atomicStampedReference.getStamp(),
                    atomicStampedReference.getStamp() + 1);
            System.out.println("线程1：" + atomicStampedReference.getStamp());
        }).start();
        atomicStampedReference.getReference();
        atomicStampedReference.getStamp();

        new Thread(() -> {
            try {
                cdl.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int stamp = 1;
            System.out.println("线程2：" + stamp);


            boolean b = atomicStampedReference.compareAndSet(3000,
                    3001,
                    stamp,
                    stamp + 1);
            System.out.println("更新："+ b);
            System.out.println("线程2：" + atomicStampedReference.getStamp());
        }).start();

        //打印也有问题
    }
}
