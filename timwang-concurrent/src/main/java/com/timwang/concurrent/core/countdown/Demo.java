package com.timwang.concurrent.core.countdown;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author wangjun
 * @date 2020-04-22
 */
public class Demo {
    @Test
    public void demo1() {
        for (int i = 0; i < 6; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "线程进入，并执行");
            }, String.format("名字为%s：", i)).start();
        }
        System.out.println("调用主线程的输出方法");
    }

    @Test
    public void deme2() throws Exception{
        CountDownLatch cdl = new CountDownLatch(10000);
        for (int i = 0; i < 10000; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {

                }
                System.out.println(Thread.currentThread().getName() + "线程进入，并执行");
                cdl.countDown();
            }, String.format("名字为%s：", i)).start();
        }
        cdl.await();
        System.out.println("调用主线程的输出方法");
    }
}
