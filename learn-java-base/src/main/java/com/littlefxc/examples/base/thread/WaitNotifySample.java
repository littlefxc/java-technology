package com.littlefxc.examples.base.thread;

/**
 * @author fengxuechao
 * @date 2019/2/22
 **/
public class WaitNotifySample {

    public static void main(String[] args) {
        Thread t1 = new WaitThread();
        Thread t2 = new NotifyThread();
        t1.start();
        t2.start();
    }

    private static class WaitThread extends Thread {
        @Override
        public void run() {
            synchronized (WaitNotifySample.class) {
                System.out.println("线程1:开始");
                try {
                    System.out.println("线程1:等待中。。。");
                    WaitNotifySample.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程1:结束");
            }
        }
    }

    private static class NotifyThread extends Thread {
        @Override
        public void run() {
            synchronized (WaitNotifySample.class) {
                System.out.println("线程2:开始");
                WaitNotifySample.class.notify();
                System.out.println("线程2:notify() 后必须等待 synchronized 代码执行完后才释放");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程2:结束");
            }
        }
    }
}
