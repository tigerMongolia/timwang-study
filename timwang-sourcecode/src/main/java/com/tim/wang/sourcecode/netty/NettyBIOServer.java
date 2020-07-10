package com.tim.wang.sourcecode.netty;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import	java.util.concurrent.ScheduledExecutorService;
import	java.util.concurrent.Executors;
import	java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author wangjun
 * @date 2020-07-10
 */
public class NettyBIOServer {

    public static void main(String[] args) {
        ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("basicThreadFactory-").build());



    }



}
