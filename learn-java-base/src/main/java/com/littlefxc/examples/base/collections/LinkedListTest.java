package com.littlefxc.examples.base.collections;

import java.util.LinkedList;

/**
 * @author fengxuechao
 * @date 2019-05-22
 */
public class LinkedListTest {

    public static void main(String[] args) {
        LinkedList<String> list = new LinkedList<>();
        list.add("123");
        list.add(null);
        list.add("456");
        list.add(null);
        System.out.println(list);
        list.remove(null);
        System.out.println(list);
    }
}
