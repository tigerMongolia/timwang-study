package com.timwang.pattern.core.pattern.c_decorator;

/**
 * @author wangjun
 * @date 2019/6/2
 */
public class Cake extends Sweet {
    @Override
    public String getDescription() {
        return "一个蛋糕";
    }

    @Override
    public double cost() {
        return 66;
    }
}
