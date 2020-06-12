package com.timwang.other.annotation;

/**
 * @author wangjun
 * @date 2019-12-13
 */
public class CostTimeTest {
    /** A类的全局实例. */
    private static CostTimeClass a;




    static {
        CostTimeProxy aproxy = new CostTimeProxy();
        a = (CostTimeClass) aproxy.getProxy(CostTimeClass.class);

    }

    /**
     * main 方法.
     *
     * @param args 数组参数
     */
    public static void main(String[] args) {
        a.doSomeThing();
        a.doSomeThing2();
    }
}
