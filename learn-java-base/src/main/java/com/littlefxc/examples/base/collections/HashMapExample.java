package com.littlefxc.examples.base.collections;

import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author fengxuechao
 * @date 2019/1/17
 **/
public class HashMapExample {

    public static void main(String[] args) {
        HashMap<Integer, String> map = new HashMap<>();

        map.put(1, "A");    // 可以
        map.put(2, "B");    // 可以

//        map.put("3", "C");  // 不可以 - 键不能是字符串类型

        SortedSet<Integer> sortedSet = new TreeSet<>();

        sortedSet.add(2);

        sortedSet.add(4);
        sortedSet.add(1);
        sortedSet.add(1);

        sortedSet.add(3);

        System.out.println(sortedSet);  //[1,2,3,4]
    }
}
