package com.timwang.annotation;

import java.lang.annotation.*;

/**
 * @author wangjun
 * @date 2019-12-13
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface Todo {
    public enum Priority {
        /**
         * 优先级
         */
        LOW,
        MEDIUM,
        HIGH
    }
    public enum Status {
        /**
         * 状态处理状态
         */
        STARTED,
        NOT_STARTED
    }
    String author() default "TimWang";
    Priority priority() default Priority.LOW;
    Status status() default Status.NOT_STARTED;
}