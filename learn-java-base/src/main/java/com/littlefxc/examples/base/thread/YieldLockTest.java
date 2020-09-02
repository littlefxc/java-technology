package com.littlefxc.examples.base.thread;

// YieldLockTest.java 的源码
public class YieldLockTest{

    private static Object obj = new Object();

    public static void main(String[] args){
        YieldLockThreadA t1 = new YieldLockThreadA("t1");
        YieldLockThreadA t2 = new YieldLockThreadA("t2");
        t1.start();
        t2.start();
    }

    static class YieldLockThreadA extends Thread{
        public YieldLockThreadA(String name){
            super(name);
        }
        public void run(){
            // 获取obj对象的同步锁
            synchronized (obj) {
                for(int i=0; i <10; i++){
                    System.out.printf("%s [%d]:%d\n", this.getName(), this.getPriority(), i);
                    // i整除4时，调用yield
                    if (i%4 == 0)
                        Thread.yield();
                }
            }
        }
    }
}