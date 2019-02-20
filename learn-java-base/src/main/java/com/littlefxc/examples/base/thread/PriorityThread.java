package com.littlefxc.examples.base.thread;

/**
 * 线程优先级具有继承特性测试:
 * 主线程设置优先度后然后在线程1中启动线程2
 *
 * @author fengxuechao
 * @date 2019/2/20
 **/
public class PriorityThread {

    public static void main(String[] args) {
        System.out.println("main thread   begin priority=" + Thread.currentThread().getPriority());
        Thread.currentThread().setPriority(6);
        System.out.println("main thread     end priority=" + Thread.currentThread().getPriority());
        PriorityThread1 thread1 = new PriorityThread1();
        thread1.start();
    }
}

class PriorityThread1 extends Thread {
    @Override
    public void run() {
        System.out.println("PriorityThread1 run priority=" + this.getPriority());
        PriorityThread2 thread2 = new PriorityThread2();
        thread2.start();
    }
}

class PriorityThread2 extends Thread {
    @Override
    public void run() {
        System.out.println("PriorityThread2 run priority=" + this.getPriority());
    }
}

