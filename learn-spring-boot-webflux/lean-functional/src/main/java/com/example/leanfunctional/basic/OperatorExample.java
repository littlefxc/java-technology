package com.example.leanfunctional.basic;

import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public class OperatorExample {

    /**
     * 在此接口下，只需声明一个泛型参数T即可.
     * 提供了两个默认的static快捷实现，帮助实现二元函数min(x,y)和max(x,y)
     *
     * @param args
     */
    public static void main(String[] args) {
        UnaryOperator<Integer> add = x -> x + 1;
        System.out.println("f(x)=x+1, when x = 1, f(x) = " + add.apply(1));

        BinaryOperator<Integer> addxy = (x, y) -> x + y;
        System.out.println("f(x,y)=x+y, when x = 3 and y = 5, f(x) = " + addxy.apply(3, 5));

        BinaryOperator<Integer> min = BinaryOperator.minBy((o1, o2) -> o1 - o2);
        System.out.println("min = " + min.apply(100, 200));

        BinaryOperator<Integer> max = BinaryOperator.maxBy((o1, o2) -> o1 - o2);
        System.out.println("max = " + max.apply(100, 200));
    }
}