package com.tim.wang.sourcecode.netty.nio.channel;
import java.nio.ByteBuffer;
import	java.nio.channels.FileChannel;

import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @author wangjun
 * @date 2020-07-13
 */
public class FileChannelMain {

    public static void main(String[] args) throws Exception {
        String str = "this is channel main hello world";
        // 构建一个输出流
        FileOutputStream outputStream = new FileOutputStream("/Users/wangjun/Downloads/test.txt");
        // 获取channel
        FileChannel channel = outputStream.getChannel();
        // 获取Byte Buffer
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        // 写入到通道
        channel.write(buffer);
        // 关闭通道
        channel.close();
    }

}
