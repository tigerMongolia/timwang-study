package com.tim.wang.sourcecode.lambda;

/**
 * @author wangjun
 * @date 2020-07-25
 */
@FunctionalInterface
public interface ConsumerInterface<T> {
    void accept(T t);
}
