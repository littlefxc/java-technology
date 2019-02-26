package com.littlefxc.examples.base.thread.threadlocal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author fengxuechao
 * @date 2019/2/26
 **/
public class ParseDateWithThreadLocal {

    static final String pattern = "yyyy-MM-dd HH:mm:ss";
    private static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>();

    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 1000; i++) {
            es.execute(new ParseDate(i));
        }
        es.shutdown();
    }

    private static class ParseDate implements Runnable {

        int i = 0;

        public ParseDate(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            try {
                // 如果当前线程不持有 SimpleDateFormat 对象。那就新建并保存设置在当前线程中，如果已持有，则直接使用。
                if (threadLocal.get()==null) {
                    threadLocal.set(new SimpleDateFormat(pattern));
                }
                Date date = threadLocal.get().parse("2019-02-26 16:23:" + i % 60);
                System.out.println(i + ":" + date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
