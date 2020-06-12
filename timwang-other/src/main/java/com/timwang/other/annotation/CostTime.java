package com.timwang.other.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义'统计方法耗时'并打印日志的注解.
 *
 * @author tim.wang
 * @date 2019-12-13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface CostTime {

    /**
     * 执行超过某毫秒数时数则打印'warn'级别的日志，默认 0ms，即默认都打印.
     *
     * @return 毫秒数
     */
    long value() default 0;

}
