package com.tim.wang.sourcecode.netty.nio.channel;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author wangjun
 * @date 2020-07-14
 */
public class SocketChannelMain {
    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 3000));

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int read = socketChannel.read(buffer);
        System.out.println(read);

        String newData = "New String to write to file..." + System.currentTimeMillis();

        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.clear();
        buf.put(newData.getBytes());

        buf.flip();

        while (buf.hasRemaining()) {
            socketChannel.write(buf);
        }


        //设置 SocketChannel 为异步模式, 这样我们的 connect, read, write 都是异步的了
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("http://example.com", 80));
        //在异步模式中, 或许连接还没有建立, connect 方法就返回了, 因此我们需要检查当前是否是连接到了主机, 因此通过一个 while 循环来判断
        while (!socketChannel.finishConnect()) {
            //wait, or do something else...
        }
    }
}
