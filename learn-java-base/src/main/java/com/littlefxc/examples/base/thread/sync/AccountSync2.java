package com.littlefxc.examples.base.thread.sync;

/**
 * @author fengxuechao
 * @date 2019/2/21
 **/
public class AccountSync2 implements Runnable {

    static volatile int money = 0;

    @Override
    public void run() {
        synchronized (this) {
            for (int i = 0; i < 100000; i++) {
                money++;
            }
        }
    }

    public static void main(String[] args) {
        AccountSync2 account1 = new AccountSync2();
//        AccountSync2 account2 = new AccountSync2();
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
}
