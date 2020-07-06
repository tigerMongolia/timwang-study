package com.timwang.other.uid;

import java.util.UUID;

/**
 * @author wangjun
 * @date 2020-07-06
 */
public class UUIDGenerator {

    public static String getUid() {
        return UUID.randomUUID().toString();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 30; i++) {
            System.out.println(getUid());
        }
    }

}
