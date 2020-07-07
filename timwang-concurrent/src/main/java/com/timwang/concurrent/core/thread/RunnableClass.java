package com.timwang.concurrent.core.thread;

/**
 * @author wangjun
 * @date 2020-07-07
 */
public class RunnableClass {
    static class RunnableSub implements Runnable {
        @Override
        public void run() {
            System.out.println("我是来自runnable的方法。name：" + Thread.currentThread().getName());
        }
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new RunnableSub(), "threadname-1");
        thread.start();
        System.out.println("===主线程结束==");
    }
}
