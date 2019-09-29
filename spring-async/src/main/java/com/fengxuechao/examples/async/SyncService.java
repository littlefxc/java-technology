package com.fengxuechao.examples.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

@Slf4j
@Component
public class SyncService {

    /**
     * 异步
     * @throws InterruptedException
     */
    //    @Async
    @Async("asyncPoolTaskExecutor")
    public void asyncEvent() throws InterruptedException {
        //休眠1s
        Thread.sleep(1000);
        //log.info("异步方法输出：{}!", System.currentTimeMillis());
        log.info("异步方法内部线程名称:{}", Thread.currentThread().getName());
    }

    /**
     * 异步回调
     * @return
     * @throws InterruptedException
     */
    @Async("asyncPoolTaskExecutor")
    public Future<String> asyncFuture() throws InterruptedException {
        ///休眠1s
        Thread.sleep(1000);
//        Thread.sleep(2000);
        log.info("异步方法内部线程名称：{}!", Thread.currentThread().getName());
        return new AsyncResult<>("异步方法返回值");
    }

    /**
     * 同步
     * @throws InterruptedException
     */
    public void syncEvent() throws InterruptedException {
        Thread.sleep(1000);
        //log.info("同步方法输出：{}!", System.currentTimeMillis());
    }

}