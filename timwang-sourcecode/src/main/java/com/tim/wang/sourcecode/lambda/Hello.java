package com.tim.wang.sourcecode.lambda;

/**
 * @author wangjun
 * @date 2020-07-25
 */
public class Hello {
    Runnable r1 = () -> System.out.println(this);
    Runnable r2 = () -> System.out.println(toString());

    public static void main(String[] args) {
        new Hello().r1.run();
        new Hello().r2.run();
    }

    @Override
    public String toString() {
        return "Hello hello";
    }
}
