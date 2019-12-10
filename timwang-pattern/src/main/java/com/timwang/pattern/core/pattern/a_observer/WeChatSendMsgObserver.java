package com.timwang.pattern.core.pattern.a_observer;

/**
 * 观察者：发送企业微信的观察者
 * @author wangjun
 * @date 2019/6/2
 */
public class WeChatSendMsgObserver implements Observer {
    private Integer userId;
    private String msg;

    public WeChatSendMsgObserver(Integer userId, String msg) {
        this.userId = userId;
        this.msg = msg;
    }

    @Override
    public void update(String message) {
        System.out.println("=======this is a notify user wechat transaction========userId is：" + userId + "，msg is ：" + msg);
    }
}
