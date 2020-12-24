package com.littlefxc.examples.base.juc.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author fengxuechao
 * @date 2020/12/24
 */
public class TestAtomicInteger {

    public static void main(String[] args) {
        // 新建AtomicLong对象
        AtomicInteger mAtoInteger = new AtomicInteger();

        mAtoInteger.set(1);
        System.out.printf("%20s : %d\n", "get()", mAtoInteger.get());
        System.out.printf("%20s : %d\n", "intValue()", mAtoInteger.intValue());
        System.out.printf("%20s : %d\n", "longValue()", mAtoInteger.longValue());
        System.out.printf("%20s : %s\n", "doubleValue()", mAtoInteger.doubleValue());
        System.out.printf("%20s : %s\n\n", "floatValue()", mAtoInteger.floatValue());

        System.out.printf("%20s : %s\n", "getAndDecrement()", mAtoInteger.getAndDecrement());
        System.out.printf("%20s : %s\n", "decrementAndGet()", mAtoInteger.decrementAndGet());
        System.out.printf("%20s : %s\n", "getAndIncrement()", mAtoInteger.getAndIncrement());
        System.out.printf("%20s : %s\n\n", "incrementAndGet()", mAtoInteger.incrementAndGet());

        System.out.println("0x 代表16进制，0x10 = 1 * 16^1 + 0 * 16^0 = 16");
        System.out.printf("%20s : %s\n", "addAndGet(0x10)", mAtoInteger.addAndGet(0x10));
        System.out.printf("%20s : %s\n", "getAndAdd(0x10)", mAtoInteger.getAndAdd(0x10));

        System.out.printf("\n%20s : %s\n", "get()", mAtoInteger.get());

        System.out.printf("%20s : %s\n", "compareAndSet()", mAtoInteger.compareAndSet(33, 2));
        System.out.printf("%20s : %s\n", "get()", mAtoInteger.get());
        System.out.printf("%20s : %s\n", "compareAndSet()", mAtoInteger.compareAndSet(1, 2));
        System.out.printf("%20s : %s\n", "get()", mAtoInteger.get());
    }
}
