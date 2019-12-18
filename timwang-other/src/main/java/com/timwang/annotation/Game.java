package com.timwang.annotation;

import java.lang.annotation.*;

/**
 * @author wangjun
 * @date 2019-12-18
 */
@Repeatable(People.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Game {
    String value() default "";
}
