package com.tim.wang.sourcecode.lambda;

/**
 * @author wangjun
 * @date 2020-07-25
 */
public class LambdaSourceCode2 {
    public static void main(String[] args) {
        new Thread(() -> {
            System.out.println("====");;
        }).start();
    }
}
