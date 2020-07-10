package com.timwang.concurrent.core.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wangjun
 * @date 2020-07-10
 */
public class OOMTaskClass {
    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        int i = 0;
        while (true) {
            exec.submit(new Task(i++));
        }
    }

    static class Task implements Runnable {
        private int i;

        public Task(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            System.out.println("run = " + i);
        }
    }
}
