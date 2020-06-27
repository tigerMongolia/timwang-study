package com.timwang.concurrent.core.lock;

/**
 * @author wangjun
 * @date 2020-06-27
 */
public class ReentrantLock {
    boolean isLocked = false;
    Thread lockedBy = null;
    int lockedCount = 0;

    public synchronized void lock() throws InterruptedException {
        Thread thread = Thread.currentThread();
        while (isLocked && lockedBy != thread) {
            wait();
        }
        isLocked = true;
        lockedCount++;
        lockedBy = thread;
    }

    public synchronized void unlock() {
        if (Thread.currentThread() == this.lockedBy) {
            lockedCount--;
            if (lockedCount == 0) {
                isLocked = false;
                notify();
            }
        }
    }

    public static void main(String[] args) {
        ReentrantLock one = new ReentrantLock();
        Count count = one.new Count();
        try {
            count.print();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class Count {
        ReentrantLock lock = new ReentrantLock();
        private void print() throws Exception {
            lock.lock();
            doAdd();
            lock.unlock();
        }

        public void doAdd() throws Exception {
            lock.lock();
            System.out.println("do add something...");
            lock.unlock();
        }
    }
}
