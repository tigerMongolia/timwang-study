package com.timwang.pattern.core.pattern.c_decorator;

/**
 * @author wangjun
 * @date 2019/6/2
 */
public class FruitDecorator extends BaseDecorator {
    private Sweet sweet;

    public FruitDecorator(Sweet sweet) {
        this.sweet = sweet;
    }

    @Override
    public String getDescription() {
        return sweet.getDescription() + "，水果";
    }

    @Override
    public double cost() {
        return sweet.cost() + 10;
    }
}
