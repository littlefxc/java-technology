package com.littlefxc.examples.base.thread.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 通过缓存示例说明读写锁的使用方式
 *
 * @author fengxuechao
 * @date 2019/3/1
 **/
public class CacheRWL {

    private static Map<String, Object> map = new HashMap<>();

    private static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    private static Lock rLock = rwl.readLock();

    private static Lock wLock = rwl.writeLock();

    public static final Object get(String key) {
        rLock.lock();
        try {
            return map.get(key);
        } finally {
            rLock.unlock();
        }
    }

    public static final Object put(String key, Object value) {
        wLock.lock();
        try {
            return map.put(key, value);
        } finally {
            wLock.unlock();
        }
    }

    public static final void clear() {
        wLock.lock();
        try {
            map.clear();
        } finally {
            wLock.unlock();
        }
    }

}
