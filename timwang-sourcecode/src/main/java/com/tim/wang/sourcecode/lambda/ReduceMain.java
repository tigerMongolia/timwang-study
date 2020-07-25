package com.tim.wang.sourcecode.lambda;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author wangjun
 * @date 2020-07-25
 */
public class ReduceMain {
    public static void main(String[] args) {
        findMaxLengthLambda();
        findSumLengthLambda();
    }

    private static void findSumLengthLambda() {
        Stream<String> stream = Stream.of("I", "Love", "You", "Too");
        Integer reduce = stream.reduce(0, (sum, str) -> sum + str.length(), Integer::sum);
        System.out.println(reduce);
    }

    private static void findMaxLengthLambda() {
        Stream<String> stream = Stream.of("I", "Love", "You", "Too");
        Optional<String> reduce = stream.reduce((s1, s2) -> s1.length() >= s2.length() ? s1 : s2);
        System.out.println(reduce.isPresent() ? reduce.get() : null);
    }
}
