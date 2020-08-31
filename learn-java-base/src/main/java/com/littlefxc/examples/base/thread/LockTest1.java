package com.littlefxc.examples.base.thread;

// LockTest1.java的源码
class Something {
    public synchronized void isSyncA() {
        try {
            for (int i = 0; i < 5; i++) {
                Thread.sleep(100); // 休眠100ms
                System.out.println(Thread.currentThread().getName() + " : isSyncA");
            }
        } catch (InterruptedException ie) {
        }
    }

    public synchronized void isSyncB() {
        try {
            for (int i = 0; i < 5; i++) {
                Thread.sleep(100); // 休眠100ms
                System.out.println(Thread.currentThread().getName() + " : isSyncB");
            }
        } catch (InterruptedException ie) {
        }
    }

    public static synchronized void cSyncA() {
        try {
            for (int i = 0; i < 5; i++) {
                Thread.sleep(100); // 休眠100ms
                System.out.println(Thread.currentThread().getName() + " : cSyncA");
            }
        } catch (InterruptedException ie) {
        }
    }

    public static synchronized void cSyncB() {
        try {
            for (int i = 0; i < 5; i++) {
                Thread.sleep(100); // 休眠100ms
                System.out.println(Thread.currentThread().getName() + " : cSyncB");
            }
        } catch (InterruptedException ie) {
        }
    }
}

public class LockTest1 {

    Something x = new Something();
    Something y = new Something();

    public static void main(String[] args) {
        LockTest1 demo = new LockTest1();
        demo.test1();
    }

    // 比较(01) x.isSyncA()与x.isSyncB()
    private void test1() {
        // 新建t11, t11会调用 x.isSyncA()
        Thread t11 = new Thread(() -> x.isSyncA(), "t11");

        // 新建t12, t12会调用 x.isSyncB()
        Thread t12 = new Thread(() -> x.isSyncB(), "t12");


        t11.start();  // 启动t11
        t12.start();  // 启动t12
    }
}