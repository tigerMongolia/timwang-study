package com.timwang.pattern.core.pattern.a_observer;

/**
 * 抽象主题，提供了attach、detach、notify三个方法
 * @author wangjun
 * @date 2019/6/2
 */
public interface Subject {
    /**
     * 增加订阅者
     * @param observer Observer
     */
    void attach(Observer observer);
    /**
     * 删除订阅者
     * @param observer Observer
     */
    void detach(Observer observer);

    /**
     * 通知订阅者更新消息
     * @param message String
     */
    void notify(String message);
}
