package com.timwang.concurrent.core.lock;

/**
 * @author wangjun
 * @date 2020-06-27
 */
public class NotReentrantLock {
    private boolean isLocked = false;

    public synchronized void lock() throws Exception {
        while (isLocked) {
            wait();
        }
        isLocked = true;
    }

    public synchronized void unlock() {
        isLocked = false;
        notify();
    }

    class Count {
        NotReentrantLock lock = new NotReentrantLock();
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

    public static void main(String[] args) {
        NotReentrantLock one = new NotReentrantLock();
        Count count = one.new Count();
        try {
            count.print();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
