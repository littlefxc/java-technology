package com.littlefxc.examples.base.thread;

/**
 * @author fengxuechao
 */
public class JoinLongTest {

    public static void main(String[] args) {
        try {
            MyThread threadTest = new MyThread();
            threadTest.start();

            threadTest.join(1000);
//            Thread.sleep(1000);
            System.out.println("主线程结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static public class MyThread extends Thread {

        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                System.out.println("子线程结束");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}