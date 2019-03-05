package com.littlefxc.examples.base.thread.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author fengxuechao
 * @date 2019-03-05
 */
public class ConditionSample {

    Lock lock = new ReentrantLock();

    Condition condition = lock.newCondition();

    public void conditionWait() {
        lock.lock();
        try {
            System.out.println("线程 " + Thread.currentThread().getId() + " 释放锁并开始等待");
            long l = System.currentTimeMillis();
            condition.await();
            long time = System.currentTimeMillis() - l;
            System.out.println("线程 " + Thread.currentThread().getId() + " 获得锁并结束等待, 等待时间是 " + time + "ms");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void conditionSignal() {
        lock.lock();
        try {
            System.out.println("线程 " + Thread.currentThread().getId() + " 开始释放锁并通知线程等待队列");
            condition.signal();
            Thread.sleep(2000);
            System.out.println("线程 " + Thread.currentThread().getId() + " 释放锁并通知线程等待队列");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ConditionSample sample = new ConditionSample();
        Thread t1 = new Thread(() -> sample.conditionWait());
        Thread t2 = new Thread(() -> sample.conditionSignal());
        t1.start();
        t2.start();
    }

}
