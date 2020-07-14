package com.tim.wang.sourcecode.netty.nio.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author wangjun
 * @date 2020-07-14
 */
public class ServerSocketChannelMain {
    public static void main(String[] args) throws Exception {
        blockModel();
        notBlockModel();
    }

    private static void notBlockModel() throws Exception {
        //非阻塞模式下, accept()是非阻塞的, 因此如果此时没有连接到来, 那么 accept()方法会返回null
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.socket().bind(new InetSocketAddress(9999));
        serverSocketChannel.configureBlocking(false);

        while(true){
            SocketChannel socketChannel = serverSocketChannel.accept();

            if(socketChannel != null){
                //do something with socketChannel...
            }
        }
    }

    private static void blockModel() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(9999));

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (true) {
            /*
             * 使用ServerSocketChannel.accept()方法来监听客户端的 TCP 连接请求,
             * accept()方法会阻塞, 直到有连接到来, 当有连接时, 这个方法会返回一个 SocketChannel 对象
             */
            SocketChannel socketChannel = serverSocketChannel.accept();
            int read = socketChannel.read(buffer);
            buffer.flip();
            System.out.println(buffer.toString());
        }
    }
}
