package com.tim.wang.sourcecode.netty.nio.buffer;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * @author wangjun
 * @date 2020-07-14
 */
public class MappedBufferMain {
    public static void main(String[] args) throws Exception {
        RandomAccessFile accessFile = new RandomAccessFile("/Users/wangjun/clinks/dirty_merge_file.txt","rw");
        FileChannel fileChannel = accessFile.getChannel();

        /*
         * 可以锁定文件的一部分,
         * 要获取文件的一部分上的锁，您要调用一个打开的 FileChannel 上的 lock() 方法。
         * 注意，如果要获取一个排它锁，您必须以写方式打开文件。
         */
        FileLock lock=fileChannel.lock();
        lock.release();

        /*
         * 内存映射文件 I/O 是通过使文件中的数据神奇般地出现为内存数组的内容来完成的。
         * 这其初听起来似乎不过就是将整个文件读到内存中，但是事实上并不是这样。
         * 一般来说，只有文件中实际读取或者写入的部分才会送入（或者 映射 ）到内存中
         */
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 1024);
        byte[] ss = new byte[1024];
        mappedByteBuffer.get(ss);
        System.out.println(new String(ss));
        fileChannel.close();
    }
}
