package com.littlefxc.examples.base.thread;

// YieldTest.java的源码
class YieldThreadA extends Thread {

    public YieldThreadA(String name) {
        super(name);
    }

    public synchronized void run() {
        for (int i = 0; i < 10; i++) {
            System.out.printf("%s [%d]:%d\n", this.getName(), this.getPriority(), i);
            // i整除4时，调用yield
            if (i % 4 == 0)
                Thread.yield();
        }
    }
}

public class YieldTest {
    public static void main(String[] args) {
        YieldThreadA t1 = new YieldThreadA("t1");
        YieldThreadA t2 = new YieldThreadA("t2");
        t1.start();
        t2.start();
    }
}