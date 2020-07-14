package com.tim.wang.sourcecode.netty.nio.buffer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author wangjun
 * @date 2020-07-14
 */
public class DirectBufferMain {
    public static void main(String[] args) throws Exception{
        FileInputStream fis = new FileInputStream("/Users/wangjun/clinks/dirty_merge_file.txt");
        FileChannel inChannel = fis.getChannel();

        FileOutputStream fos = new FileOutputStream("/Users/wangjun/clinks/copy_file.txt");
        FileChannel outChannel = fos.getChannel();

        /**
         * 分配一个直接缓冲区
         * 给定一个直接字节缓冲区，Java虚拟机将尽最大努 力直接对它执行本机I/O操作。
         * 也就是说，它会在每一次调用底层操作系统的本机I/O操作之前(或之后)，尝试避免将缓冲区的内容拷贝到一个中间缓冲区中 或者从一个中间缓冲区中拷贝数据
         */
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        while (true) {
            //先清空缓冲区
            buffer.clear();
            int read = inChannel.read(buffer);
            if (read == -1) {
                break;
            }
            buffer.flip();
            // 从头开始写
            outChannel.write(buffer);
        }

        inChannel.close();
        outChannel.close();
        fos.close();
        fis.close();
    }
}
