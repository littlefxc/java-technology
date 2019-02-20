package com.littlefxc.examples.base.thread;

/**
 * @author fengxuechao
 * @date 2019/2/20
 **/
public class MyRunnable implements Runnable {

    @Override
    public void run() {
        System.out.println("实现Runnable");
    }

    public static void main(String[] args) {
        Runnable runnable = new MyRunnable();
        Thread thread = new Thread(runnable);
        thread.start();
        System.out.println("运行结束");
    }
}
