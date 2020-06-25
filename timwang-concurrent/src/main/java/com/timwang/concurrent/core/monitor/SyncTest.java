package com.timwang.concurrent.core.monitor;

/**
 * @author wangjun
 * @date 2020-06-24
 */
public class SyncTest {
    public void synBlock() {
        synchronized(this){
            System.out.println("wj");
        }
    }
}
