package com.timwang.study.redisson.util;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Redisson是一个在Redis的基础上实现的Java驻内存数据网格（In-Memory Data Grid）。
 * 它不仅提供了一系列的分布式的Java常用对象，还提供了许多分布式服务。
 * 其中包括(BitSet, Set, Multimap, SortedSet, Map, List）
 * Queue, BlockingQueue, Deque, BlockingDeque, Semaphore, Lock, AtomicLong, CountDownLatch, Publish / Subscribe,
 * Bloom filter, Remote service, Spring cache, Executor service, Live Object service, Scheduler service)
 * Redisson提供了使用Redis的最简单和最便捷的方法。Redisson的宗旨是促进使用者对Redis的关注分离（Separation of Concern），
 * 从而让使用者能够将精力更集中地放在处理业务逻辑上。
 * @author wangjun
 * @date 2020-06-08
 */
public class RedissonClientUtil {
    private static RedissonClient client = null;

    private static Logger logger = LoggerFactory.getLogger(RedissonClientUtil.class);

    public static RedissonClient getInstance() {
        if (client != null) {
            return client;
        }
        try {
            Config config = Config.fromJSON(ClassLoader.getSystemResource("single-node-config.json").openStream());
            client = Redisson.create(config);
            return client;
        } catch (IOException exception) {
            logger.error("get instance of redisson client ex", exception);
            throw new RuntimeException("init ex", exception);
        }
    }
}
