package com.tim.wang.sourcecode.netty.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

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

        while (true) {
            // 通过调用 select 方法, 阻塞地等待 channel I/O 可操作
            if (selector.select(TIMEOUT) == 0) {
                System.out.println(".");
                continue;
            }

            // 获取一个SelectionKey后，就要将它删除，表示我们已经对这个IO事件进行处理
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                // 当获取一个 SelectionKey 后, 就要将它删除, 表示我们已经对这个 IO 事件进行了处理.
                keyIterator.remove();

                if (key.isAcceptable()) {
                    /*
                     * 当 OP_ACCEPT 事件到来时, 我们就有从 ServerSocketChannel 中获取一个 SocketChannel, 代表客户端的连接
                     * 注意, 在 OP_ACCEPT 事件中, 从 key.channel() 返回的 Channel 是 ServerSocketChannel.
                     * 而在 OP_WRITE 和 OP_READ 中, 从 key.channel() 返回的是 SocketChannel.
                     */
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel clientChannel = channel.accept();
                    clientChannel.configureBlocking(false);
                    /*
                     * 在 OP_ACCEPT 到来时, 再将这个 Channel 的 OP_READ 注册到 Selector 中.
                     * 注意, 这里我们如果没有设置 OP_READ 的话, 即 interest set 仍然是 OP_CONNECT 的话,
                     * 那么 select 方法会一直直接返回.
                     */
                    clientChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(BUF_SIZE));
                }
                if (key.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer buf = (ByteBuffer) key.attachment();
                    int bytesRead = socketChannel.read(buf);
                    if (bytesRead == -1) {
                        socketChannel.close();
                    }
                    if (bytesRead > 0) {
                        key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        System.out.println("Get data length: " + bytesRead);
                    }
                }


            }
        }

    }

}
