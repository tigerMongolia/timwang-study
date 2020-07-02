package com.timwang.concurrent.core.lock;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * @author wangjun
 * @date 2020-07-02
 */
public class LockWaitNotify {
    private static java.util.concurrent.locks.ReentrantLock reentrantLock = new java.util.concurrent.locks.ReentrantLock();
    private static Condition conditionA = reentrantLock.newCondition();
    private static Condition conditionB = reentrantLock.newCondition();

    public static void main(String[] args) throws Exception {
        Thread waitThreadA = new Thread(new WaitA(), "threadA");
        waitThreadA.start();
        Thread waitThreadB = new Thread(new WaitB(), "threadB");
        waitThreadB.start();
        TimeUnit.SECONDS.sleep(2);
        reentrantLock.lock();
        try {
            conditionA.signal();
        } finally {
            reentrantLock.unlock();
        }
    }

    static class WaitA implements Runnable {
        @Override
        public void run() {
           reentrantLock.lock();
           try {
               System.out.println(Thread.currentThread() + "begin await = " + System.currentTimeMillis());
               conditionA.await();
               System.out.println(Thread.currentThread() + "end await = " + System.currentTimeMillis());
           } catch (Exception e) {
                e.printStackTrace();
           } finally {
               reentrantLock.unlock();
           }
        }
    }

    static class WaitB implements Runnable {
        @Override
        public void run() {
            reentrantLock.lock();
            try {
                System.out.println(Thread.currentThread() + "，begin await = " + System.currentTimeMillis());
                conditionB.await();
                System.out.println(Thread.currentThread() + "，end await = " + System.currentTimeMillis());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }
        }
    }
}
