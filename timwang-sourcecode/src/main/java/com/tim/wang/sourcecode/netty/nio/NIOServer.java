package com.tim.wang.sourcecode.netty.nio;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * @author wangjun
 * @date 2020-07-23
 */
public class NIOServer {

    private static final int BUF_SIZE = 256;
    private static final int TIMEOUT = 3000;
    private static final int SOCKET_ADDRESS_PORT = 8088;

    public static void main(String[] args) throws Exception {
        // 打开服务端Socket
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        // 打开Selector
        Selector selector = Selector.open();

        serverSocket.socket().bind(new InetSocketAddress(SOCKET_ADDRESS_PORT));
        serverSocket.configureBlocking(false);

        /*
         * 将channel注册到 selector中
         * 通常我们都是注册OP_ACCEPT事件，然后在OP_ACCEPT到来时，再将这个Channel的OP_READ注册到Selector
         */
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);


    }

}
