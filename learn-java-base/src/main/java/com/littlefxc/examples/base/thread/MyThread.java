package com.littlefxc.examples.base.thread;

/**
 * @author fengxuechao
 * @date 2019/2/20
 **/
public class MyThread extends Thread {

    @Override
    public void run() {
        super.run();
        System.out.println("继承Thread");
    }

    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        myThread.start();
        System.out.println("运行结束");
    }
}
