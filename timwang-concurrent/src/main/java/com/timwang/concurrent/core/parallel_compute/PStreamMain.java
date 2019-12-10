package com.timwang.concurrent.core.parallel_compute;

/**
 * @author wangjun
 * @date 2019-10-03
 */
public class PStreamMain {
    public static void main(String args[]) {

        new Thread(new Plus()).start();
        new Thread(new Multiply()).start();
        new Thread(new Div()).start();

        System.out.println(System.currentTimeMillis());
        for (int i = 0; i <= 10; i++) {
            for (int j = 0; j <= 2; j++) {
                Msg msg = new Msg();
                msg.i = i;
                msg.j = j;
                msg.info = i + "-" + j;
                msg.orgStr = "((" + i + "+" + j + ")*" + i + ")/2";
                Plus.blockingDeque.add(msg);
            }
        }
        System.out.println(System.currentTimeMillis());
    }
}
