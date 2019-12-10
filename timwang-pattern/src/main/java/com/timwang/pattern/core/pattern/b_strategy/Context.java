package com.timwang.pattern.core.pattern.b_strategy;

import java.util.List;

/**
 * @author wangjun
 * @date 2019/6/2
/**
 * Context 是一个使用了某种策略的类。 实现了 Strategy 接口的实体策略类
 */
public class Context {

    private AssignStrategy strategy;

    public Context(AssignStrategy strategy) {
        this.strategy = strategy;
    }

    public Integer executeStrategy(List<ProjectUserScore> scoreList){
        return strategy.assign(scoreList);
    }
}