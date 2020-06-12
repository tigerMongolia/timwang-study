package com.timwang.study.redis.test.util;

import com.timwang.redis.study.redisson.util.RedissonClientUtil;
import org.junit.Test;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * @author wangjun
 * @date 2020-06-08
 */
public class RedissonUtilTest {
    @Test
    public void test_create_redis() throws Exception{
        RedissonClient instance = RedissonClientUtil.getInstance();
        assert instance != null;
        Config config = instance.getConfig();
        System.out.println(config.toJSON());
    }
}
