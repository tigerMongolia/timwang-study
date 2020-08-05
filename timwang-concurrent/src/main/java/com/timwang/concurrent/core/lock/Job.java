package com.timwang.concurrent.core.lock;

/**
 * @author wangjun
 * @date 2020-06-24
 */
public class Job implements Runnable {
    private PrintQueue printQueue;

    public Job(PrintQueue printQueue) {
        this.printQueue = printQueue;
    }

    @Override
    public void run() {
        System.out.printf("线程 #%s: 将要进入打印job \n", Thread.currentThread().getName());
        printQueue.printJob(new Object());
        System.out.printf("线程 #%s: 打印job已结束\n", Thread.currentThread().getName());
    }

}
