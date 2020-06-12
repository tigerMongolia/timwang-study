package com.timwang.other.annotation;

import java.lang.annotation.*;

/**
 * 开启API权限认证
 * @author wangjun
 * @date 2019-12-13
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableAuth {
}
