package com.timwang.other.annotation;

import java.lang.annotation.*;

/**
 * @author wangjun
 * @date 2019-12-13
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BankTransferMoney {
    double maxMoney() default 10000;
}
