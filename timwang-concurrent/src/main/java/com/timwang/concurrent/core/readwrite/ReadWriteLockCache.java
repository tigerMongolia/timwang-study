package com.timwang.concurrent.core.readwrite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 * @author wangjun
 * @date 2020-07-02
 */
public class ReadWriteLockCache {
    private static Map<String, Object> map = new HashMap<>();
    private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    private static ReadLock readLock = reentrantReadWriteLock.readLock();
    private static WriteLock writeLock = reentrantReadWriteLock.writeLock();

    public static Object getByKey(String key) {
        readLock.lock();
        try {
            return map.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
            return null;
        }
    }

    public static Map<String, Object> getMap() {
        readLock.lock();
        try {
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
        return null;
    }

    public static void put(String key, Object value) {
        writeLock.lock();
        try {
            map.put(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }

    public static void remove(String key) {
        writeLock.lock();
        try {
            map.remove(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }

    public static void main(String[] args)  throws Exception{
        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Thread putThread = new PutThread();
            threadList.add(putThread);
        }
        for (Thread thread : threadList) {
            thread.start();
        }
        put("ji", "ji");
        TimeUnit.SECONDS.sleep(5);
        System.out.println(getMap());
    }

    private static class PutThread extends Thread {
        @Override
        public void run() {
            put(Thread.currentThread().getName(), Thread.currentThread().getName());
        }
    }

}
