package com.tim.wang.sourcecode.netty;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author wangjun
 * @date 2020-07-11
 */
public class NettyBIOServer {
    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("=== server started at port 6666 ===");
        while (true) {
            final Socket socket = serverSocket.accept();
            System.out.println("=== server accept socket ===");
            handlerSocket(socket);
        }

    }

    public static void handlerSocket(Socket socket) {
        try {
            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[1024];
            while (true) {
                int read = inputStream.read(buffer);
                if (read != -1) {
                    System.out.println(new String(buffer, 0, read));
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
