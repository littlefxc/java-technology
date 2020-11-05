/*
 * 版权所有: 爱WiFi无线运营中心
 * 创建日期: 2020/11/4
 * 创建作者: 冯雪超
 * 文件名称: PriorityThreadDemo.java
 * 版本: v1.0
 * 功能:
 * 修改记录：
 */
package com.littlefxc.examples.base.thread;

/**
 * @author fengxuechao
 * @date 2020/11/4
 */
class PriorityThreadThread extends Thread{
    public PriorityThreadThread(String name) {
        super(name);
    }

    public void run(){
        for (int i=0; i<5; i++) {
            System.out.println(Thread.currentThread().getName()
                    +"("+Thread.currentThread().getPriority()+ ")"
                    +", loop "+i);
        }
    }
};

public class PriorityThreadDemo {
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName()
                +"("+Thread.currentThread().getPriority()+ ")");

        Thread t1=new PriorityThreadThread("t1");    // 新建t1
        Thread t2=new PriorityThreadThread("t2");    // 新建t2
        t1.setPriority(1);                                  // 设置t1的优先级为1
        t2.setPriority(10);                                 // 设置t2的优先级为10
        t1.start();                                         // 启动t1
        t2.start();                                         // 启动t2
    }
}
