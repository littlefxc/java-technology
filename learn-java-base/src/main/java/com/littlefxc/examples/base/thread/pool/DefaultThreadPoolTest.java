package com.littlefxc.examples.base.thread.pool;

/**
 * @author fengxuechao
 * @date 2019-03-06
 */
public class DefaultThreadPoolTest {

    static class JobRunner implements Runnable{

        @Override
        public void run() {
            for (int i = 0; i < 1000; i++) {
                System.out.println("线程 " + Thread.currentThread().getId() + " 执行任务 " + i);
            }
        }
    }

    public static void main(String[] args) {
        ThreadPool<JobRunner> pool = new DefaultThreadPool<>();
        pool.execute(new JobRunner());
        pool.shutdown();
    }
}
