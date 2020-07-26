package com.tim.wang.sourcecode;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author wangjun
 * @date 2020-07-26
 */
public class StreamAPIMain {
    public static void main(String[] args) {
        // streamRangeMain();
        streamRangePeekMain();
    }

    public static void streamRangePeekMain() {
        IntStream.range(1, 10)
                .peek(x -> System.out.print("\nA" + x))
                .skip(6)
                .peek(x -> System.out.print("B" + x))
                .forEach(x -> System.out.print("C" + x));
    }

    public static void streamRangeMain() {
        IntStream.range(1, 10)
                .peek(x -> System.out.println("\nA" + x))
                .limit(3)
                .peek(x -> System.out.println("B" + x))
                .forEach(x -> System.out.println("C" + x));
    }
}
