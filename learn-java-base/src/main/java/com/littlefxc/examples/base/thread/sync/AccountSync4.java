package com.littlefxc.examples.base.thread.sync;

/**
 * @author fengxuechao
 * @date 2019/2/21
 **/
public class AccountSync4 implements Runnable {

    static volatile int money = 0;

    @Override
    public void run() {
        synchronized (AccountSync4.class) {
            for (int i = 0; i < 100000; i++) {
                money++;
            }
        }
    }

    public static void main(String[] args) {
        AccountSync4 account1 = new AccountSync4();
        AccountSync4 account2 = new AccountSync4();
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
