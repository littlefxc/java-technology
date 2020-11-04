package com.littlefxc.examples.base.thread;

public class InterruptedExample2{

    static volatile boolean stop;
    public static void main(String[] args) throws InterruptedException {
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                //任务逻辑
                try {
                    Thread.sleep(10000);//睡眠10秒，处于阻塞状态
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("任务终止");
                    return ;
                }
                //其他任务逻辑...
                System.out.println("线程结束");
            }
        });
        t.start();
        System.out.println("Thread start");
        t.interrupt();
        System.out.println("Thread end");
        stop=true;
    }
}