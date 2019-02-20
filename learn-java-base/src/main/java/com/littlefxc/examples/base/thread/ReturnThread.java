package com.littlefxc.examples.base.thread;

/**
 * 使用return停止线程
 *
 * @author fengxuechao
 * @date 2019/2/20
 **/
public class ReturnThread extends Thread {

    public static void main(String[] args) throws InterruptedException {
        ReturnThread t = new ReturnThread();
        t.start();
        Thread.sleep(2000);
        t.interrupt();
    }

    @Override
    public void run() {
        while (true) {
            if (this.isInterrupted()) {
                System.out.println("停止了!");
                return;
            }
            System.out.println("timer=" + System.currentTimeMillis());
        }
    }

}
