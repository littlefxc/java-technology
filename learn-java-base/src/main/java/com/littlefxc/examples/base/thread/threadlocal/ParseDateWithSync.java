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
public class ParseDateWithSync {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
                synchronized (ParseDateWithSync.class) {
                    Date date = SDF.parse("2019-02-26 16:23:" + i % 60);
                    System.out.println(i + ":" + date);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}

