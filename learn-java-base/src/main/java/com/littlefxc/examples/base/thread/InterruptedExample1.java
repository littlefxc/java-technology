package com.littlefxc.examples.base.thread;

public class InterruptedExample1 {
	static volatile boolean stop;
	public static void main(String[] args) throws InterruptedException {
		Thread t=new Thread(new Runnable() {
			@Override
			public void run() {
				while(!stop) {
					System.out.println("running...");
					long time = System.currentTimeMillis();
					while((System.currentTimeMillis()-time < 1000)) {
						//延时一秒
					}
				}
				System.out.println("线程结束");
			}
		});
		t.start();
		Thread.sleep(3000);
		stop=true;
	}
}