package com.timwang.other.annotation;

import java.lang.annotation.*;

/**
 * @author wangjun
 * @date 2019-12-13
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MyTestAnnotation {
    String name() default "mao";
    int age() default 18;
}
