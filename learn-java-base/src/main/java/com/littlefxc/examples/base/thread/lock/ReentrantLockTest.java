package com.littlefxc.examples.base.thread.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author fengxuechao
 * @date 2019/2/28
 **/
public class ReentrantLockTest implements Runnable {

    Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        ReentrantLockTest lockR = new ReentrantLockTest();
        Thread t1 = new Thread(lockR);
        Thread t2 = new Thread(lockR);
        Thread t3 = new Thread(lockR);
        t1.start();
        t2.start();
        t3.start();

    }

    @Override
    public void run() {
        lock.lock();
//        lock.lock();// 重进入验证
        System.out.println("线程 " + Thread.currentThread().getId() + " 加锁");
        try {
            for (int i = 0; i < 5; i++) {
                System.out.println("线程 " + Thread.currentThread().getId() + " 循环计数 = " + i);
            }
        } finally {
            lock.unlock();// 锁的释放
            System.out.println("线程 " + Thread.currentThread().getId() + " 解锁");
            lock.unlock();
        }
    }
}
