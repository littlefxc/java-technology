package com.littlefxc.examples.base.thread.sync;

/**
 * 指定加锁对象的错误示例：两个线程访问的不是同一个对象
 *
 * @author fengxuechao
 * @date 2019/2/20
 **/
public class AccountSync1 implements Runnable {

    static volatile int money = 0;

    public static void main(String[] args) {
        AccountSync1 account1 = new AccountSync1();
//        AccountSync1 account2 = new AccountSync1();
        Thread t1 = new Thread(account1);
        Thread t2 = new Thread(account1);
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(money);
    }

    @Override
    public synchronized void run() {
        for (int i = 0; i < 100000; i++) {
            money++;
        }
    }
}
