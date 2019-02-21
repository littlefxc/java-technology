package com.littlefxc.examples.base.thread.sync;

/**
 * @author fengxuechao
 * @date 2019/2/21
 **/
public class AccountSync3 implements Runnable {

    static volatile int money = 0;

    private synchronized static void increase() {
        money++;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100000; i++) {
            increase();
        }
    }

    public static void main(String[] args) {
        AccountSync3 account1 = new AccountSync3();
        AccountSync3 account2 = new AccountSync3();
        Thread t1 = new Thread(account1);
        Thread t2 = new Thread(account2);
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
