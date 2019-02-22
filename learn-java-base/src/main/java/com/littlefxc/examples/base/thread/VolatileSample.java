package com.littlefxc.examples.base.thread;

/**
 * @author fengxuechao
 * @date 2019/2/21
 **/
public class VolatileSample extends Thread {

    private int number;

    volatile static boolean ready = true;

    @Override
    public void run() {
        while (ready) {
            number++;
        }
        System.out.println(ready);
        System.out.println(number);
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new VolatileSample();
        t1.start();
        Thread.sleep(1000);
        ready = false;
    }
}
