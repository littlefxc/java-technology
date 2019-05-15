---
title: Java集合学习之fail-fast
date: 2019-05-14 18:31:08
categories: Java
tags:
- Java集合
---

<!-- more -->

[TOC]

## 概要

本文主要以 ArrayList 为例，对 Iterator 的快速失败(`fail-fast`), 也就是 Java 集合的错误检测机制进行学习总结。主要内容有：

1. 简介
2. 错误展示
3. 问题解决
4. 理解原理
5. JDK的解决办法

## 简介

“快速失败”也就是 fail-fast，它是 Java 集合的一种错误检测机制。当多个线程对集合进行结构上的改变的操作时，有可能会产生 `fail-fast` 机制。
记住是有可能，而不是一定。例如：假设存在两个线程（线程 1、线程 2），线程 1 通过 Iterator 在遍历集合 A 中的元素，
在某个时候线程 2 修改了集合 A 的结构（是结构上面的修改，而不是简单的修改集合元素的内容），
那么这个时候程序就会抛出 ConcurrentModificationException 异常，从而产生 fail-fast 机制。

## 错误示例

```java
package com.littlefxc.examples.base.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Java 集合的错误检测机制 fail-fast 的示例
 *
 * @author fengxuechao
 */
public class FailFastTest {

    private static List<Integer> list = new ArrayList<>();
    //private static List<String> list = new CopyOnWriteArrayList<String>();

    /**
     * 线程one迭代list
     */
    private static class threadOne extends Thread {

        @Override
        public void run() {
            Iterator<Integer> iterator = list.iterator();
            while (iterator.hasNext()) {
                int i = iterator.next();
                System.out.println("ThreadOne 遍历:" + i);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 当i == 3时，修改list
     */
    private static class threadTwo extends Thread {

        @Override
        public void run() {
            int i = 0;
            while (i < 6) {
                System.out.println("ThreadTwo run：" + i);
                if (i == 3) {
                    list.remove(i);
                }
                i++;
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        new threadOne().start();
        new threadTwo().start();
    }
}
```

运行结果：

```log
ThreadOne 遍历:0
ThreadTwo run：0
ThreadTwo run：1
ThreadTwo run：2
ThreadTwo run：3
ThreadTwo run：4
ThreadTwo run：5
Exception in thread "Thread-0" java.util.ConcurrentModificationException
	at java.util.ArrayList$Itr.checkForComodification(ArrayList.java:901)
	at java.util.ArrayList$Itr.next(ArrayList.java:851)
	at com.littlefxc.examples.base.collections.FailFastTest$threadOne.run(FailFastTest.java:25)

Process finished with exit code 0
```

## 问题解决

先说解决办法：

1. 在遍历过程中所有涉及到改变 modCount 值得地方全部加上 synchronized 或者直接使用 Collections.synchronizedList，这样就可以解决。但是不推荐，因为增删造成的同步锁可能会阻塞遍历操作。
2. 使用 CopyOnWriteArrayList 来替换 ArrayList。推荐使用该方案。

## 理解原理

同过上面的错误示例和问题解决，可以初步了解到产生 `fail-fast` 的原因就在于
当某一个线程遍历list的过程中，list的内容被另外一个线程所改变了；
就会抛出 `ConcurrentModificationException` 异常，产生fail-fast事件。

`ConcurrentModificationException` 的产生：当方法检测到对象的并发修改，但不允许这种修改时就抛出该异常。

也就是说，即便是在单线程环境中，只要违反了规则，同样也可能会抛出异常。

当我对代码运行多次时，发现代码运行有几率不抛出异常，这就说明迭代器的快速失败行为并不能得到保证，所以，不要写依赖这个异常的程序代码。
正确的做法是：`ConcurrentModificationException` 应该仅用于检测 bug。

`AbstractList` 抛出 `ConcurrentModificationException` 的部分代码(Java8)：

```java
package java.util;

public abstract class AbstractList<E> extends AbstractCollection<E> implements List<E> {

    //神略代码...

    // AbstractList中唯一的属性
    // 用来记录List修改的次数：每修改一次(添加/删除等操作)，将modCount+1
    protected transient int modCount = 0;

    // 返回List对应迭代器。实际上，是返回Itr对象。
    public Iterator<E> iterator() {
        return new Itr();
    }

    // Itr是Iterator(迭代器)的实现类
    private class Itr implements Iterator<E> {
        int cursor = 0;

        int lastRet = -1;

        // 修改数的记录值。
        // 每次新建Itr()对象时，都会保存新建该对象时对应的modCount；
        // 以后每次遍历List中的元素的时候，都会比较expectedModCount和modCount是否相等；
        // 若不相等，则抛出ConcurrentModificationException异常，产生fail-fast事件。
        int expectedModCount = modCount;

        public boolean hasNext() {
            return cursor != size();
        }

        public E next() {
            // 获取下一个元素之前，都会判断“新建Itr对象时保存的modCount”和“当前的modCount”是否相等；
            // 若不相等，则抛出ConcurrentModificationException异常，产生fail-fast事件。
            checkForComodification();
            try {
                E next = get(cursor);
                lastRet = cursor++;
                return next;
            } catch (IndexOutOfBoundsException e) {
                checkForComodification();
                throw new NoSuchElementException();
            }
        }

        public void remove() {
            if (lastRet == -1)
                throw new IllegalStateException();
            checkForComodification();

            try {
                AbstractList.this.remove(lastRet);
                if (lastRet < cursor)
                    cursor--;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    //省略代码...
}
```

从中，我们可以发现在调用 next() 和 remove()时，都会执行 checkForComodification()。若 “modCount 不等于 expectedModCount”，则抛出ConcurrentModificationException异常，产生fail-fast事件。

要搞明白 fail-fast机制，我们就要需要理解什么时候“modCount 不等于 expectedModCount”！
从Itr类中，我们知道 expectedModCount 在创建Itr对象时，被赋值为 modCount。通过Itr，我们知道：expectedModCount不可能被修改为不等于 modCount。所以，需要考证的就是modCount何时会被修改。

那么它(modCount)在什么时候因为什么原因而发生改变呢？

ArrayList部分源码：

```java
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
    /**
     * 最小化列表容量
     */
    public void trimToSize() {
        modCount++;
        if (size < elementData.length) {
            elementData = (size == 0)
              ? EMPTY_ELEMENTDATA
              : Arrays.copyOf(elementData, size);
        }
    }
    
    /**
     * 确定动态扩容所需容量
     */
    public void ensureCapacity(int minCapacity) {
        int minExpand = (elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA)
            // any size if not default element table
            ? 0
            // larger than default for default empty table. It's already
            // supposed to be at default size.
            : DEFAULT_CAPACITY;
    
        if (minCapacity > minExpand) {
            ensureExplicitCapacity(minCapacity);
        }
    }
    
    /**
     * 确定动态扩容所需容量
     */
    private void ensureCapacityInternal(int minCapacity) {
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
        }
    
        ensureExplicitCapacity(minCapacity);
    }
    
    /**
     * 动态扩容
     */
    private void ensureExplicitCapacity(int minCapacity) {
        modCount++;
    
        // overflow-conscious code
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }
    
    /**
     * 1. 将指定元素的索引及后续元素的索引向右移动(索引+1)
     * 2. 在指定的索引插入元素
     */
    public void add(int index, E element) {
        rangeCheckForAdd(index);
    
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        System.arraycopy(elementData, index, elementData, index + 1,
                         size - index);
        elementData[index] = element;
        size++;
    }
    
    /**
     * 1. 将指定索引及后续元素的索引向左移动
     * 2. 数组元素实际数量 - 1
     */
    public E remove(int index) {
        rangeCheck(index);
    
        modCount++;
        E oldValue = elementData(index);
    
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index,
                             numMoved);
        elementData[--size] = null; // clear to let GC do its work
    
        return oldValue;
    }
    
    // 循环比较元素，获取要移除元素的索引，然后将该索引及后续元素的索引向左移动
    public boolean remove(Object o) {
        if (o == null) {
            for (int index = 0; index < size; index++)
                if (elementData[index] == null) {
                    fastRemove(index);
                    return true;
                }
        } else {
            for (int index = 0; index < size; index++)
                if (o.equals(elementData[index])) {
                    fastRemove(index);
                    return true;
                }
        }
        return false;
    }
    
    private void fastRemove(int index) {
        modCount++;
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index,
                             numMoved);
        elementData[--size] = null; // clear to let GC do its work
    }
    
    /**
     * 循环设置所有元素值为null, 加快垃圾回收
     */
    public void clear() {
        modCount++;
    
        // clear to let GC do its work
        for (int i = 0; i < size; i++)
            elementData[i] = null;
    
        size = 0;
    }
}
```

从上面的源代码我们可以看出，ArrayList 中无论 add、remove、clear 方法只要是涉及了改变 ArrayList 元素的个数的方法都会导致 modCount 的改变。
所以我们这里可以初步判断由于 expectedModCount 得值与 modCount 的改变不同步，导致两者之间不等从而产生 fail-fast 机制。

场景还原：

有两个线程（线程 A，线程 B），其中线程 A 负责遍历 list、线程B修改 list。线程 A 在遍历 list 过程的某个时候（此时 expectedModCount = modCount=N），
线程启动，同时线程B增加一个元素，这是 modCount 的值发生改变（modCount + 1 = N + 1）。
线程 A 继续遍历执行 next 方法时，通告 checkForComodification 方法发现 expectedModCount = N ，而 modCount = N + 1，两者不等，
这时就抛出ConcurrentModificationException 异常，从而产生 fail-fast 机制。

至此，**我们就完全了解了fail-fast是如何产生的！**

也就是，当多个线程对同一个集合进行操作的时候，某线程访问集合的过程中，该集合的内容被其他线程所改变(即其它线程通过add、remove、clear等方法，改变了modCount的值)；
这时，就会抛出ConcurrentModificationException异常，产生fail-fast事件。

## JDK的解决办法：CopyOnWriteArrayList

CopyOnWriteArrayList 是 ArrayList 的一个线程安全的变体，其中所有可变操作（add、set 等等）都是通过对底层数组进行一次新的复制来实现的。 
该类产生的开销比较大，但是在两种情况下，它非常适合使用。

1. 在不能或不想进行同步遍历，但又需要从并发线程中排除冲突时。
2. 当遍历操作的数量大大超过可变操作的数量时。

遇到这两种情况使用 CopyOnWriteArrayList 来替代 ArrayList 再适合不过了。

```java
package java.util.concurrent;
import java.util.*;
import java.util.concurrent.locks.*;
import sun.misc.Unsafe;

public class CopyOnWriteArrayList<E>
    implements List<E>, RandomAccess, Cloneable, java.io.Serializable {

    // 省略代码...

    // 返回集合对应的迭代器
    public Iterator<E> iterator() {
        return new COWIterator<E>(getArray(), 0);
    }

    // 省略代码...
   
    private static class COWIterator<E> implements ListIterator<E> {
        private final Object[] snapshot;

        private int cursor;

        private COWIterator(Object[] elements, int initialCursor) {
            cursor = initialCursor;
            // 新建COWIterator时，将集合中的元素保存到一个新的拷贝数组中。
            // 这样，当原始集合的数据改变，拷贝数据中的值也不会变化。
            snapshot = elements;
        }

        public boolean hasNext() {
            return cursor < snapshot.length;
        }

        public boolean hasPrevious() {
            return cursor > 0;
        }

        public E next() {
            if (! hasNext())
                throw new NoSuchElementException();
            return (E) snapshot[cursor++];
        }

        public E previous() {
            if (! hasPrevious())
                throw new NoSuchElementException();
            return (E) snapshot[--cursor];
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor-1;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public void set(E e) {
            throw new UnsupportedOperationException();
        }

        public void add(E e) {
            throw new UnsupportedOperationException();
        }
    }
  
    // 省略代码...

}
```

可以从上面的源码中可以看出：

1. 和 ArrayList 继承于 AbstractList 不同，CopyOnWriteArrayList 没有继承于 AbstractList，它仅仅只是实现了 List 接口。
2. ArrayList 的 iterator() 函数返回的 Iterator 是在 AbstractList 中实现的；而 CopyOnWriteArrayList 是自己实现 Iterator。
3. ArrayList 的 Iterator 实现类中调用 next() 时，会“调用 checkForComodification() 比较 `expectedModCount ` 和 `modCount` 的大小”；但是，CopyOnWriteArrayList 的 Iterator 实现类中，没有所谓的 checkForComodification()，更不会抛出 ConcurrentModificationException 异常！

CopyOnWriterArrayList 的 add 方法与 ArrayList 的 add 方法有一个最大的不同点就在于，下面三句代码：

```java
Object[] newElements = Arrays.copyOf(elements, len + 1);
newElements[len] = e;
setArray(newElements);
```

就是这三句代码使得 CopyOnWriterArrayList 不会抛 ConcurrentModificationException 异常。
它们就是 copy 原来的 array，再在 copy 数组上进行 add 操作，这样做就完全不会影响 COWIterator 中的 array 了

CopyOnWriterArrayList 的核心概念就是：

任何对 array 在结构上有所改变的操作（add、remove、clear 等），CopyOnWriterArrayList 都会 copy 现有的数据，再在 copy 的数据上修改，
这样就不会影响 COWIterator 中的数据了，修改完成之后改变原有数据的引用即可。同时这样造成的代价就是产生大量的对象，
同时数组的 copy 也是相当有损耗的。