package com.tim.wang.sourcecode.spi;

/**
 * @author wangjun
 * @date 2020-07-12
 */
public class RobotImpl implements Robot {
    @Override
    public void sayHello() {
        System.out.println("say hello in impl");
    }
}
