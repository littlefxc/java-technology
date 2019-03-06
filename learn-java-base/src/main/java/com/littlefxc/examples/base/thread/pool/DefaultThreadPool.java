package com.littlefxc.examples.base.thread.pool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author fengxuechao
 * @date 2019-03-06
 */
public class DefaultThreadPool<Job extends Runnable> implements ThreadPool<Job> {

    /**
     * 线程池最大数量
     */
    private static final int MAX_WORKER_NUMBERS = 10;

    /**
     * 线程池默认数量
     */
    private static final int DEFAULT_WORKER_NUMBERS = 5;

    /**
     * 线程池最小数量
     */
    private static final int MIN_WORKER_NUMBERS = 1;

    /**
     * 工作列表
     */
    private final LinkedList<Job> jobs = new LinkedList<>();

    /**
     * 工作者列表
     */
    private final List<Worker> workers = Collections.synchronizedList(new ArrayList<>());

    /**
     * 工作者线程的数量
     */
    private int workerNum = DEFAULT_WORKER_NUMBERS;

    /**
     * 线程编号生成
     */
    private AtomicInteger threadNum = new AtomicInteger();

    public DefaultThreadPool() {
        initializeWorkers(DEFAULT_WORKER_NUMBERS);
    }

    public DefaultThreadPool(int workerNum) {
        this.workerNum = workerNum > MAX_WORKER_NUMBERS ? MAX_WORKER_NUMBERS :
                workerNum < MIN_WORKER_NUMBERS ? MIN_WORKER_NUMBERS : workerNum;
        initializeWorkers(this.workerNum);
    }

    @Override
    public void execute(Job job) {
        if (job != null) {
            // 添加一个工作，然后进行等待
            synchronized (jobs) {
                jobs.addLast(job);
                jobs.notify();
            }
        }
    }

    @Override
    public void shutdown() {
        for (Worker worker : workers) {
            worker.shutdown();
        }
    }

    @Override
    public void addWorkers(int num) {
        synchronized (jobs) {
            if (num + this.workerNum > MAX_WORKER_NUMBERS) {
                num = MAX_WORKER_NUMBERS - this.workerNum;
            }
            initializeWorkers(num);
            this.workerNum += num;
        }
    }

    @Override
    public void removeWorker(int num) {
        synchronized (jobs) {
            if (num >= this.workerNum) {
                throw new IllegalArgumentException("beyond workNum");
            }
            // 按照给定的数量停止 Worker
            int count = 0;
            while (count < num) {
                Worker worker = workers.get(count);
                if (workers.remove(worker)) {
                    worker.shutdown();
                    count++;
                }
            }
            this.workerNum -= count;
        }
    }

    @Override
    public int getJobSize() {
        return 0;
    }

    /**
     * 初始化线程工作者
     *
     * @param workerNum
     */
    private void initializeWorkers(int workerNum) {
        for (int i = 0; i < workerNum; i++) {
            Worker worker = new Worker();
            workers.add(worker);
            Thread thread = new Thread(worker, "ThreadPool-Worker-" + threadNum.incrementAndGet());
            thread.start();
        }
    }

    /**
     * 工作者，负责消费任务
     */
    class Worker implements Runnable {


        private volatile boolean running = true;

        @Override
        public void run() {
            while (running) {
                Job job = null;
                synchronized (jobs) {
                    // 如果工作者列表是空的，那么就 wait
                    while (jobs.isEmpty()) {
                        try {
                            jobs.wait();
                        } catch (InterruptedException e) {
                            // 感知到外部对 WorkerThread 的中断操作，返回
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    // 取出一个 Job
                    job = jobs.removeFirst();
                }
                if (job != null) {
                    try {
                        job.run();
                    } catch (Exception e) {
                        // 忽略 Job 执行中的 Exception
                    }
                }
            }
        }

        public void shutdown() {
            running = false;
        }
    }
}
