package com.tim.wang.sourcecode.lambda;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author wangjun
 * @date 2020-07-25
 */
public class MyStream<T> {
    private List<T> list;

    public MyStream(List<T> list) {
        this.list = list;
    }

    public void forEach(ConsumerInterface<T> consumer) {
        for (T t : list) {
            consumer.accept(t);
        }
    }

    public static void main(String[] args) {
        MyStream<String> str = new MyStream<>(Lists.newArrayList("1","2","3"));
        str.forEach(item -> System.out.println(item));
    }

}
