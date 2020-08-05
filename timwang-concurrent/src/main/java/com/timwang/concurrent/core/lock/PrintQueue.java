package com.timwang.concurrent.core.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wangjun
 * @date 2020-06-24
 */
public class PrintQueue {
    private final Lock queueLock = new ReentrantLock(false);

    public void printJob(Object document) {
        queueLock.lock();
        try {
            Long duration = (long) (Math.random() * 1000);
            System.out.printf("----线程 #%s: 打印第一段信息中: 打印时间为 %d 毫秒\n",
                    Thread.currentThread().getName(), (duration));
            Thread.sleep(duration);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("----线程 #"+Thread.currentThread().getName()+": 解锁");
            queueLock.unlock();
        }

        try {
            Thread.sleep(60);
        } catch (Exception e) {

        }

        queueLock.lock();
        try {
            Long duration = (long) (Math.random() * 1000);
            System.out.printf("====线程 #%s: 打印第二段信息中: 打印时间为 %d 毫秒\n",
                    Thread.currentThread().getName(), (duration));
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("----线程 #"+Thread.currentThread().getName()+":第二段 解锁");
            queueLock.unlock();
        }
    }
}
