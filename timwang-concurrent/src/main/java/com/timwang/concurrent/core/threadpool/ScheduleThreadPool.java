package com.timwang.concurrent.core.threadpool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wangjun
 * @date 2020-07-10
 */
public class ScheduleThreadPool {
    public static void main(String[] args) {
        scheduledExecutorServiceResult();
        threadFactoryBuild();
        threadPoolTaskBuild();
    }

    private static void threadPoolTaskBuild() {
        // 通过xml方式
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(10);
        threadPoolTaskExecutor.setMaxPoolSize(100);
        threadPoolTaskExecutor.setQueueCapacity(2000);
    }

    private static void threadFactoryBuild() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();

        ExecutorService executor = new ThreadPoolExecutor(
                5, 200, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy()
        );
        executor.submit(() -> System.out.println(Thread.currentThread().getName() + "run"));
        executor.shutdown();
    }

    private static void scheduledExecutorServiceResult() {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build()
        );

        executorService.execute(() -> System.out.println("run"));
    }
}
