package com.tim.wang.sourcecode.netty.bio;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.net.ServerSocket;
import java.net.Socket;
import	java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;



/**
 * @author wangjun
 * @date 2020-07-10
 */
public class NettyThreadBIOServer {

    public static void main(String[] args) throws Exception {
        ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("basicThreadFactory-").build());

        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("server started on port 6666");
        while (true) {
            Socket accept = serverSocket.accept();
            System.out.println("socket connected on server");
            executor.execute(() -> NettyBIOServer.handlerSocket(accept));
        }
    }
}
