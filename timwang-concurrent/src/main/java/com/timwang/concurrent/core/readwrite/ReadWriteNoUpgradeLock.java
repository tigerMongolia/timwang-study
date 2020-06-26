package com.timwang.concurrent.core.readwrite;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author wangjun
 * @date 2020-06-26
 */
public class ReadWriteNoUpgradeLock {
    final static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    public static void main(String[] args) {
        upgrade();
    }

    public static void upgrade() {
        rwl.readLock().lock();
        System.out.println("获取到了读锁");
        rwl.writeLock().lock();
        // 打印出“获取到了读锁”，但是却不会打印出“成功升级”，因为 ReentrantReadWriteLock 不支持读锁升级到写锁。
        System.out.println("成功升级");
    }
}
