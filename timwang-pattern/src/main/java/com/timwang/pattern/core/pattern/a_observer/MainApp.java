package com.timwang.pattern.core.pattern.a_observer;

/**
 * @author wangjun
 * @date 2019/6/2
 */
public class MainApp {
    public static void main(String[] args) {
        UpdateInfoSubject infoSubject = new UpdateInfoSubject();
        // 创建更新日志观察者
        ProjectUpdateLogObserver o1 = new ProjectUpdateLogObserver(16273, 37, 1, "更新测试1");
        // 创建企业微信观察者
        WeChatSendMsgObserver o2 = new WeChatSendMsgObserver(37, "更新信息后的测试消息");
        // 订阅更新subject
        infoSubject.attach(o1);
        infoSubject.attach(o2);
        // 业务逻辑完成发出消息给订阅的更新日志
        infoSubject.notify("");
    }
}
