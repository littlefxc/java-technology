package com.example.leanfunctional.basic;

import java.util.function.Supplier;

public class SupplierExample {

    public static void main(String[] args) {
        Supplier<SupplierExample> anotherSupplier;
        for (int i = 0; i < 10; i++) {
            anotherSupplier = SupplierExample::new;
            System.out.println(anotherSupplier.get());
        }
    }
}
