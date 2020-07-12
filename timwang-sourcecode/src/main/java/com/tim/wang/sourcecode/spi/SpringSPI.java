package com.tim.wang.sourcecode.spi;

import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;

/**
 * @author wangjun
 * @date 2020-07-12
 */
public class SpringSPI {
    public static void main(String[] args) {
        List<Robot> robots = SpringFactoriesLoader.loadFactories(Robot.class, SpringSPI.class.getClassLoader());
        List<String> strings = SpringFactoriesLoader.loadFactoryNames(Robot.class, SpringSPI.class.getClassLoader());
        System.out.println(strings);
        System.out.println(robots);
    }
}
