package com.timwang.concurrent.core.readwrite;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author wangjun
 * @date 2020-06-26
 */
public class ReadLockJumpQueue {

    private static final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    private static final ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock
            .readLock();
    private static final ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock
            .writeLock();

    private static void read() {
        readLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "得到读锁，正在读取");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
            System.out.println(Thread.currentThread().getName() + "释放读锁");
        }
    }

    private static void write() {
        writeLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "得到写锁，正在写入");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
            System.out.println(Thread.currentThread().getName() + "释放写锁");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new Thread(ReadLockJumpQueue::read, "Thread-2").start();
        new Thread(ReadLockJumpQueue::read, "Thread-4").start();
        new Thread(ReadLockJumpQueue::write, "Thread-3").start();
        new Thread(ReadLockJumpQueue::read, "Thread-5").start();
    }
}
