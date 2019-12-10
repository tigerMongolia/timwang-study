package com.timwang.pattern.core.pattern.a_observer;

import com.google.common.collect.Lists;

import java.util.List;

/**
 *  被观察者的实现
 *  里面有:
 *    1、观察者接口的集合数据结构
 *  　2、实现添加观察者方法(registerObservers)
 *    3、移除观察者方法(removeObservers)
 *    4、通知所有观察者的方法(notifyObservers)
 * @author wangjun
 * @date 2019/6/2
 */
public class UpdateInfoSubject implements Subject{
    private List<Observer> updateInfoList = Lists.newArrayList();

    @Override
    public void attach(Observer observer) {
        updateInfoList.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        int index = updateInfoList.indexOf(observer);
        if(index >= 0){
            updateInfoList.remove(observer);
        }
    }

    @Override
    public void notify(String message) {
        for (Observer observer : updateInfoList) {
            observer.update(message);
        }
    }
}
