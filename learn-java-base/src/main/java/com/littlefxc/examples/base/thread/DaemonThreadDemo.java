package com.littlefxc.examples.base.thread;

/**
 * @author fengxuechao
 * @date 2020/11/5
 */
public class DaemonThreadDemo extends Thread {

    public DaemonThreadDemo(String name) {
        super(name);
    }

    public void run() {
        try {
            for (int i = 0; i < 5; i++) {
                Thread.sleep(3);
                System.out.println(this.getName() + "(isDaemon=" + this.isDaemon() + ")" + ", loop " + i);
            }
        } catch (InterruptedException e) {
        }
    }

    public static void main(String[] args) {

        System.out.println(Thread.currentThread().getName()
                + "(isDaemon=" + Thread.currentThread().isDaemon() + ")");

        Thread t1 = new DaemonThreadDemo("t1");    // 新建t1
        Thread t2 = new MyDaemon("t2");            // 新建t2
        t2.setDaemon(true);                               // 设置t2为守护线程
        t1.start();                                       // 启动t1
        t2.start();                                       // 启动t2
    }


}

class MyDaemon extends Thread {
    public MyDaemon(String name) {
        super(name);
    }

    public void run() {
        try {
            // 主线程结束后，守护线程会立即结束，不会打印 10000 次
            for (int i = 0; i < 10000; i++) {
                Thread.sleep(1);
                System.out.println(this.getName() + "(isDaemon=" + this.isDaemon() + ")" + ", loop " + i);
            }
        } catch (InterruptedException e) {
        }
    }
}