package com.timwang.other.annotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangjun
 * @date 2019-12-13
 */
public class CostTimeClass {
    private static final Logger log = LoggerFactory.getLogger(CostTimeClass.class);

    /**
     * 始终打印方法执行耗时的方法.
     */
    @CostTime
    public void doSomeThing() {
        log.info("执行A类中doSomeThing()方法！");
    }

    /**
     * 当方法执行耗时大于等于'50ms'时打印出方法执行耗时.
     */
    @CostTime(50)
    public void doSomeThing2() {
        log.info("执行A类中doSomeThing2()方法！");
    }

}
