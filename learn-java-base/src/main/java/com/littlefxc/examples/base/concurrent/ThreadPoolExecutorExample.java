package com.littlefxc.examples.base.concurrent;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * BlockingQueue是双缓冲队列。BlockingQueue内部使用两条队列，允许两个线程同时向队列一个存储，一个取出操作。在保证并发安全的同时，提高了队列的存取效率。
 * 常用的几种BlockingQueue：
 * 1. ArrayBlockingQueue（int i）:规定大小的BlockingQueue，其构造必须指定大小。其所含的对象是FIFO顺序排序的。
 * 2. LinkedBlockingQueue（）或者（int i）:大小不固定的BlockingQueue，若其构造时指定大小，生成的BlockingQueue有大小限制，不指定大小，其大小有Integer.MAX_VALUE来决定,其所含的对象是FIFO顺序排序的。
 * 3. PriorityBlockingQueue（）或者（int i）:类似于LinkedBlockingQueue，但是其所含对象的排序不是FIFO，而是依据对象的自然顺序或者构造函数的Comparator决定。
 * 4. SynchronizedQueue（）:特殊的BlockingQueue，对其的操作必须是放和取交替完成。
 * <p>
 * 自定义线程池，可以用ThreadPoolExecutor类创建，它有多个构造方法来创建线程池。
 *
 * @author fengxuechao
 * @version 0.1
 * @date 2020/1/7
 */
public class ThreadPoolExecutorExample implements Runnable {

    @Override
    public void run() {
        // 打印正在执行的缓存线程信息
        System.out.println(Thread.currentThread().getName() + "正在被执行");
        try {
            // sleep一秒保证3个任务在分别在3个线程上执行
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 创建数组型缓冲等待队列
        BlockingQueue<Runnable> bq = new ArrayBlockingQueue<Runnable>(10);
        // 用开源框架 guava 提供的 ThreadFactoryBuilder 可以快
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("guava-pool-%d").build();
        // ThreadPoolExecutor:创建自定义线程池，池中保存的线程数为3，允许最大的线程数为6
        ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 6, 50, TimeUnit.MILLISECONDS, bq, threadFactory);
        for (int i = 0; i < 3; i++) {
            executor.execute(new ThreadPoolExecutorExample());
        }
        // 关闭自定义线程池
        executor.shutdown();
    }
}
