package com.tim.wang.sourcecode.lambda;

/**
 * @author wangjun
 * @date 2020-07-25
 */
public class LambdaSourceCode {
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Anonymous Class Thread run()");
            }
        }).start();
    }
}
