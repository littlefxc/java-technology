package com.littlefxc.examples.base;

import java.util.Map;

/**
 * @author fengxuechao
 * @date 2019/1/11
 **/
public class PassByValue {

    public static void main(String[] args) {
        Foo f = new Foo("f");
        System.out.println(f);

        changeReference(f);// 引用变量f不会被改变！
        System.out.println(f);

        modifyReference(f);// 引用变量f的attribute会改变
        System.out.println(f);


    }

    public static void changeReference(Foo a) {
        Foo b = new Foo("b");
        a = b;
    }

    public static void modifyReference(Foo c) {
        c.setAttribute("c");
    }
}

class Foo {
    private String attribute;

    public Foo(String a) {
        this.attribute = a;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
}

