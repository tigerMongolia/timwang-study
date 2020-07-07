package com.timwang.concurrent.core.thread;

/**
 * @author wangjun
 * @date 2020-07-07
 */
public class ThreadClass {
    static class SubThread extends Thread {
        @Override
        public void run() {
            System.out.println("执行了一次run方式：name：" + Thread.currentThread().getName());
        }
    }

    public static void main(String[] args) {
        SubThread subThread = new SubThread();
        subThread.start();
        System.out.println("主线程结束");
    }
}
