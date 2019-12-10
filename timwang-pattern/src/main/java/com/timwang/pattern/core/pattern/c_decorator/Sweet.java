package com.timwang.pattern.core.pattern.c_decorator;

/**
 * @author wangjun
 * @date 2019/6/2
 */
public abstract class Sweet {
    private String description = "Sweet";

    public String getDescription() {
        return description;
    }

    public abstract double cost();
}
