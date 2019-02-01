package com.example.leanfunctional.basic;

import java.util.function.BiFunction;
import java.util.function.Function;

public class FunctionExample {

    /**
     * 查看源码可知：compose 和 andThen 的不同之处是函数执行的顺序不同。
     * compose 函数先执行参数，然后执行调用者，而 andThen 先执行调用者，然后再执行参数。
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("--------BEGIN--------");
        Function<Integer, Integer> incr1 = x -> x + 1;
        Function<Integer, Integer> multiply = x -> x * 2;
        int x = 2;
        System.out.println("f(x)=x+1,when x=" + x + ", f(x)=" + incr1.apply(x));
        System.out.println("f(x)=x+1,g(x)=2x, when x=" + x + ", f(g(x))=" + incr1.compose(multiply).apply(x));
        System.out.println("f(x)=x+1,g(x)=2x, when x=" + x + ", g(f(x))=" + incr1.andThen(multiply).apply(x));
        System.out.println("compose vs andThen:f(g(x))=" + incr1.compose(multiply).apply(x) + "," + multiply.andThen(incr1).apply(x));
        System.out.println("--------END--------");

        System.out.println("--------BEGIN--------");
        highOrderFunction();
        System.out.println("--------END--------");
    }

    static void highOrderFunction() {
        //high order function
        Function<Integer, Function<Integer, Integer>> makeAdder = z -> y -> z + y;
        int x = 2;
        //define add1
        Function<Integer, Integer> add1 = makeAdder.apply(1);
        System.out.println("f(x)=x+1,when x=" + x + ", f(x)=" + add1.apply(x));
        //define add5
        Function<Integer, Integer> add5 = makeAdder.apply(5);
        System.out.println("f(x)=x+5,when x=" + x + ", f(x)=" + add5.apply(x));

        //binary function
        BiFunction<Integer, Integer, Integer> multiply = (a, b) -> a * b;
        System.out.println("f(z)=x*y, when x=3,y=5, then f(z)=" + multiply.apply(3, 5));
    }

}
