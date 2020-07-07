package com.timwang.concurrent.core.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author wangjun
 * @date 2020-07-07
 */
public class CallableClass {
    static class CallableSub implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            Thread.sleep(3000l);
            System.out.println("this is call from callable, name = " + Thread.currentThread().getName());
            return 1;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CallableSub client = new CallableSub();
        FutureTask futureTask = new FutureTask<>(client);
        Thread thread = new Thread(futureTask, "threadName2");
        thread.start();
        Object o = futureTask.get();
        System.out.println("result o = " + o);
    }
}
