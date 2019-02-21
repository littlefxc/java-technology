package com.littlefxc.examples.base.thread;

/**
 * @author fengxuechao
 * @date 2019/2/21
 **/
public class VolatileSample extends Thread {

    public volatile int count;

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            count=i;
        }
        if (count !=99)
            System.out.println("count=" + count);
    }

    public static void main(String[] args) {
        VolatileSample[] samples = new VolatileSample[100000];
        for (int i = 0; i < 100000; i++) {
            samples[i] = new VolatileSample();
        }

        for (int i = 0; i < 100; i++) {
            samples[i].start();
        }
    }
}
