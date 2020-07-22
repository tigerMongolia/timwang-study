package com.tim.wang.sourcecode.netty.nio.channel;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * @author wangjun
 * @date 2020-07-22
 */
public class DatagramChannelMain {
    public static void main(String[] args) throws Exception {
        //打开
        DatagramChannel channel = DatagramChannel.open();
        channel.socket().bind(new InetSocketAddress(9999));

        //读取数据
        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.clear();

        channel.receive(buf);

        //发送数据
        String newData = "New String to write to file..."
                + System.currentTimeMillis();

        ByteBuffer buffer = ByteBuffer.allocate(48);
        buffer.clear();
        buffer.put(newData.getBytes());
        buffer.flip();

        int bytesSent = channel.send(buf, new InetSocketAddress("example.com", 80));

        /*
         *连接到指定地址
         *因为 UDP 是非连接的, 因此这个的 connect 并不是向 TCP 一样真正意义上的连接, 而是它会讲 DatagramChannel 锁住, 因此我们仅仅可以从指定的地址中读取或写入数据.
         */

        channel.connect(new InetSocketAddress("example.com", 80));
    }
}
