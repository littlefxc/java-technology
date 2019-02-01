package com.example.leanfunctional.basic;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class PredicateExample {

    public static void main(String[] args) {
        PredicateExample example = new PredicateExample();
        example.printBigValue(10, val -> val > 5);
        example.printBigValueAnd(10, val -> val > 5);
        example.printBigValueAnd(6, val -> val > 5);
        //binary predicate
        BiPredicate<Integer, Long> biPredicate = (x, y) -> x > 9 && y < 100;
        System.out.println(biPredicate.test(100, 50L));
    }

    public void printBigValue(int value, Predicate<Integer> predicate) {
        if (predicate.test(value)) {
            System.out.println(value);
        }
    }

    public void printBigValueAnd(int value, Predicate<Integer> predicate) {
        if (predicate.and(v -> v < 8).test(value)) {
            System.out.println("value < 8 : " + value);
        } else {
            System.out.println("value should < 8 at least.");
        }
    }
}