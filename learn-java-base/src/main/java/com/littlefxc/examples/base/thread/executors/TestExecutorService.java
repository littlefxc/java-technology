package com.littlefxc.examples.base.thread.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestExecutorService {
    public static void main(String[] args){
        ExecutorService executorService = Executors.newCachedThreadPool();
//      ExecutorService executorService = Executors.newFixedThreadPool(5);
//      ExecutorService executorService = Executors.newSingleThreadExecutor();
        TestRunnable runnable = new TestRunnable();
        for (int i = 0; i < 10; i++){
            executorService.execute(runnable);
            System.out.println("sum = " + i);
        }
        executorService.shutdown();
    }

    static class TestRunnable implements Runnable{
        @Override
        public void run(){
            System.out.println("线程 " + Thread.currentThread().getName() + " 线程被调用了。");
        }
    }
}


