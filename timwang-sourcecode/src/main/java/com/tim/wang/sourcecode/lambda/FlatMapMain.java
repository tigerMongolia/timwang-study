package com.tim.wang.sourcecode.lambda;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author wangjun
 * @date 2020-07-25
 */
public class FlatMapMain {
    public static void main(String[] args) {
        Stream<List<Integer>> stream = Stream.of(Lists.newArrayList(1,2,3,4), Lists.newArrayList(5,6,8));
        stream.flatMap(Collection::stream).forEach(System.out::println);
    }
}
