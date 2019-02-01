package com.example.leanfunctional.basic;

import java.util.function.Consumer;
import java.util.function.Function;

public class ConsumerExample {

    /**
     * Consumer 无需定义返回类型
     *
     * @param args
     */
    public static void main(String[] args) {
        Consumer<Integer> consumer = System.out::println;
        consumer.accept(100);
        //use function, you always need one return value.
        Function<Integer, Integer> function = x -> {
            System.out.println(x);
            return x;
        };
        function.apply(100);
    }
}
