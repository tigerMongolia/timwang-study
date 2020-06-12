package com.timwang.other.annotation;

import java.lang.annotation.*;

/**
 * @author wangjun
 * @date 2019-12-13
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface People {
    Game[] value() ;
}
