package com.tim.wang.sourcecode.netty.nio;

import java.nio.Buffer;
import java.nio.IntBuffer;

/**
 * @author wangjun
 * @date 2020-07-12
 */
public class BufferMain {
    public static void main(String[] args) {
        int[] arr = new int[] {
            1,2,3,4,5,6,7,8,9,10
        };
        IntBuffer buffer = IntBuffer.allocate(10);
        for (int i : arr) {
            buffer.put(i);
        }
        displayBuffer(buffer);
        // 将缓存字节数组的指针设置为数组的开始序列即数组下标0。这样就可以从buffer开头，对该buffer进行遍历（读取）了
        displayBuffer(buffer.flip());
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }
        displayBuffer(buffer.rewind());
    }

    private static void displayBuffer(Buffer buffer) {
        System.out.println("capacity: " + buffer.capacity());
        System.out.println("position: " + buffer.position());
        System.out.println("limit: " + buffer.limit());
        System.out.println("-------------------------");

    }
}
