package com.timwang.pattern.core.pattern.c_decorator;

/**
 * @author wangjun
 * @date 2019/6/2
 */
public class CandleDecorator extends BaseDecorator {
    private Sweet sweet;

    public CandleDecorator(Sweet sweet) {
        this.sweet = sweet;
    }

    @Override
    public String getDescription() {
        return sweet.getDescription() + "，蜡烛";
    }

    @Override
    public double cost() {
        return sweet.cost() + 10;
    }
}
