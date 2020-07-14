package com.tim.wang.sourcecode.netty.nio.buffer;

import java.nio.IntBuffer;

/**
 * @author wangjun
 * @date 2020-07-14
 */
public class SubBufferMain {
    public static void main(String[] args) {
        int[] arr = new int[] {
                1,2,3,4,5,6,7,8,9,10
        };
        IntBuffer buffer = IntBuffer.allocate(10);
        for (int i : arr) {
            buffer.put(i);
        }
        //创建子缓冲区，子缓冲区的数据与原缓冲区数据是共享的，修改子缓冲区数据，原缓冲区的部分数据也会发生变化
        buffer.position(3);
        buffer.limit(7);
        IntBuffer sliceBuffer = buffer.slice();
        displayBufferData(sliceBuffer);
        displayBufferData(buffer);
        //创建只读缓冲区,与原缓冲区一样数据共享，但只能读
        IntBuffer readonlyBuffer = buffer.asReadOnlyBuffer();
    }

    private static void displayBufferData(IntBuffer buffer) {
        while (buffer.hasRemaining()) {
            System.out.print(buffer.get());
        }
        System.out.println();
    }
}
