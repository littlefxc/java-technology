package com.littlefxc.examples.base.concurrent;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Executors.newScheduledThreadPool(int n)：创建一个定长线程池，支持定时及周期性任务执行
 *
 * @author fengxuechao
 * @version 0.1
 * @date 2020/1/7
 */
public class ScheduledThreadPoolExample {

    public static void main(String[] args) {
        //创建一个定长线程池，支持定时及周期性任务执行——延迟执行
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
        System.out.println(LocalDateTime.now(ZoneOffset.of("+8")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        //延迟1秒执行
        scheduledThreadPool.schedule(() -> System.out.println(LocalDateTime.now(ZoneOffset.of("+8")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))), 1, TimeUnit.SECONDS);

        // 创建并执行一个在给定初始延迟后首次启用的定期操作，后续操作具有给定的周期；
        // 也就是将在 initialDelay 后开始执行，然后在 initialDelay+period 后执行，接着在 initialDelay + 2 * period 后执行，依此类推
        /*scheduledThreadPool.scheduleAtFixedRate(
                () -> System.out.println(LocalDateTime.now(ZoneOffset.of("+8")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))),
                2, 4, TimeUnit.SECONDS);*/

        // 创建并执行一个在给定初始延迟后首次启用的定期操作，随后，在每一次执行终止和下一次执行开始之间都存在给定的延迟。
        /*scheduledThreadPool.scheduleWithFixedDelay(
                () -> System.out.println(LocalDateTime.now(ZoneOffset.of("+8")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))),
                2, 4, TimeUnit.SECONDS);*/
    }
}
