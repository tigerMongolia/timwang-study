package com.timwang.study.redisson.util;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * @author wangjun
 * @date 2020-06-09
 */
public class RedisLockUtil {
    public static void main(String[] args) {
        RedissonClient instance = RedissonClientUtil.getInstance();
        RLock lock = instance.getLock("lock");
    }
}
