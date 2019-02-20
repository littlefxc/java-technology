package com.littlefxc.examples.base.thread;

/**
 * 使用interrupt()方法终止线程，以及是否会真的终止线程
 *
 * @author fengxuechao
 * @date 2019/2/20
 */
public class InterruptThread extends Thread {

    public static void main(String[] args) {
        try {
            InterruptThread thread = new InterruptThread();
            thread.start();
            Thread.sleep(2000);
            thread.interrupt();
        } catch (InterruptedException e) {
            System.out.println("main catch");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        for (int i = 0; i < 500000; i++) {
            if (interrupted()) {
                System.out.println("已经是停止状态了!我要退出了!");
                break;
            }
            System.out.println("i=" + (i + 1));
        }
        System.out.println("看到这句话说明线程并未终止------");
    }
}