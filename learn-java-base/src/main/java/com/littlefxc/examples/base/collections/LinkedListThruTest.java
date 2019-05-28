package com.littlefxc.examples.base.collections;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * 测试LinkedList的几种遍历方式和效率
 *
 * @author fengxuechao
 */
public class LinkedListThruTest {
    public static void main(String[] args) {
        // 通过Iterator遍历LinkedList
        iterator(getLinkedList());

        // 通过快速随机访问遍历LinkedList
        randomAccess(getLinkedList());

        // 通过for循环的变种来访问遍历LinkedList
        foreach(getLinkedList());

        // 通过PollFirst()遍历LinkedList
        while_pollFirst(getLinkedList());

        // 通过PollLast()遍历LinkedList
        while_pollLast(getLinkedList());

        // 通过removeFirst()遍历LinkedList
        while_removeFirst(getLinkedList());

        // 通过removeLast()遍历LinkedList
        while_removeLast(getLinkedList());
    }

    private static LinkedList getLinkedList() {
        LinkedList llist = new LinkedList();
        for (int i = 0; i < 100000; i++)
            llist.addLast(i);

        return llist;
    }

    /**
     * 通过快迭代器遍历LinkedList
     */
    private static void iterator(LinkedList<Integer> list) {
        if (list == null)
            return;

        // 记录开始时间
        long start = System.currentTimeMillis();

        for (Iterator iter = list.iterator(); iter.hasNext(); )
            iter.next();

        // 记录结束时间
        long end = System.currentTimeMillis();
        long interval = end - start;
        System.out.println("通过Iterator遍历LinkedList：" + interval + " ms");
    }

    /**
     * 通过快速随机访问遍历LinkedList
     */
    private static void randomAccess(LinkedList<Integer> list) {
        if (list == null)
            return;

        // 记录开始时间
        long start = System.currentTimeMillis();

        int size = list.size();
        for (int i = 0; i < size; i++) {
            list.get(i);
        }
        // 记录结束时间
        long end = System.currentTimeMillis();
        long interval = end - start;
        System.out.println("通过快速随机访问遍历LinkedList：" + interval + " ms");
    }

    /**
     * 通过另外一种for循环来遍历LinkedList
     */
    private static void foreach(LinkedList<Integer> list) {
        if (list == null)
            return;

        // 记录开始时间
        long start = System.currentTimeMillis();

        for (Integer integ : list)
            ;

        // 记录结束时间
        long end = System.currentTimeMillis();
        long interval = end - start;
        System.out.println("通过for循环的变种来访问遍历LinkedList：" + interval + " ms");
    }

    /**
     * 通过pollFirst()来遍历LinkedList
     */
    private static void while_pollFirst(LinkedList<Integer> list) {
        if (list == null)
            return;

        // 记录开始时间
        long start = System.currentTimeMillis();
        while (list.pollFirst() != null)
            ;

        // 记录结束时间
        long end = System.currentTimeMillis();
        long interval = end - start;
        System.out.println("通过PollFirst()遍历LinkedList：" + interval + " ms");
    }

    /**
     * 通过pollLast()来遍历LinkedList
     */
    private static void while_pollLast(LinkedList<Integer> list) {
        if (list == null)
            return;

        // 记录开始时间
        long start = System.currentTimeMillis();
        while (list.pollLast() != null)
            ;

        // 记录结束时间
        long end = System.currentTimeMillis();
        long interval = end - start;
        System.out.println("通过PollLast()遍历LinkedList：" + interval + " ms");
    }

    /**
     * 通过removeFirst()来遍历LinkedList
     */
    private static void while_removeFirst(LinkedList<Integer> list) {
        if (list == null)
            return;

        // 记录开始时间
        long start = System.currentTimeMillis();
        try {
            while (list.removeFirst() != null)
                ;
        } catch (NoSuchElementException e) {
        }

        // 记录结束时间
        long end = System.currentTimeMillis();
        long interval = end - start;
        System.out.println("通过removeFirst()遍历LinkedList：" + interval + " ms");
    }

    /**
     * 通过removeLast()来遍历LinkedList
     */
    private static void while_removeLast(LinkedList<Integer> list) {
        if (list == null)
            return;

        // 记录开始时间
        long start = System.currentTimeMillis();
        try {
            while (list.removeLast() != null)
                ;
        } catch (NoSuchElementException e) {
        }

        // 记录结束时间
        long end = System.currentTimeMillis();
        long interval = end - start;
        System.out.println("通过removeLast()遍历LinkedList：" + interval + " ms");
    }

}