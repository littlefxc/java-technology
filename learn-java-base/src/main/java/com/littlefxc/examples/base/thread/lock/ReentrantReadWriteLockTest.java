package com.littlefxc.examples.base.thread.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author fengxuechao
 * @date 2019/3/1
 **/
public class ReentrantReadWriteLockTest {

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public void read() {
        try {
            try {
                lock.readLock().lock();
                System.out.println("线程 " + Thread.currentThread().getId() + " 获得读锁 " + System.currentTimeMillis());
                Thread.sleep(10000);
            } finally {
                lock.readLock().unlock();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void write() {
        try {
            try {
                lock.writeLock().lock();
                System.out.println("线程 " + Thread.currentThread().getId() + " 获得写锁 " + System.currentTimeMillis());
                Thread.sleep(10000);
            } finally {
                lock.writeLock().unlock();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ReentrantReadWriteLockTest rwlSample = new ReentrantReadWriteLockTest();
        Thread t1 = new Thread(() -> rwlSample.read());
        Thread t2 = new Thread(() -> rwlSample.write());
        t1.start();
        t2.start();
    }
}
