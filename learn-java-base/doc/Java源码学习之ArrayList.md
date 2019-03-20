---
title: Java源码学习之ArrayList
date: 2019-03-20 09:53:28
categories: 
- 源码学习
tags:
- Java
---

# Java源码学习之ArrayList

```java
package java.util;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 8683452581122892189L;

    /**
     * 默认容量
     */
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * 空数组, new ArrayList(0)的时候默认数组构建一个空数组
     */
    private static final Object[] EMPTY_ELEMENTDATA = {};

    /**
     * 空数组, 调用无参构造函数的时候默认给一个空数组
     */
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

    /**
     * 保存数据的数组
     */
    transient Object[] elementData; // non-private to simplify nested class access

    /**
     * ArrayList的实际元素数量
     *
     * @serial
     */
    private int size;

    /**
     * 给定一个初始容量来构造一个空数组
     */
    public ArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+
                                               initialCapacity);
        }
    }

    /**
     * 无参数构造方法默认为空数组
     */
    public ArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }

    /**
     * 构造方法传入一个Collection， 则将Collection里面的值copy到arrayList
     */
    public ArrayList(Collection<? extends E> c) {
        elementData = c.toArray();
        if ((size = elementData.length) != 0) {
            // c.toArray might (incorrectly) not return Object[] (see 6260652)
            if (elementData.getClass() != Object[].class)
                elementData = Arrays.copyOf(elementData, size, Object[].class);
        } else {
            // replace with empty array.
            this.elementData = EMPTY_ELEMENTDATA;
        }
    }

    /**
     * 调整当前实例的容量为实际数组的大小，用于最小化实例的内存空间。
     * 可以解决平时新增、删除元素后elementData过大的问题。
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
    private void ensureCapacityInternal(int minCapacity) {
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
        }

        ensureExplicitCapacity(minCapacity);
    }

    private void ensureExplicitCapacity(int minCapacity) {
        modCount++;

        // 超出了数组可容纳的长度，需要进行动态扩展
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }

    /**
     * 1. 一些JVM可能存储Headerwords
     * 2. 避免一些机器内存溢出，减少出错几率
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /**
     * 动态扩容的核心方法。
     *
     * @param minCapacity the desired minimum capacity
     */
    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;
        //  先对容量扩大1.5倍, 这里 oldCapacity >> 1 是二进制操作右移，相当于除以2, 我称之为期望容量
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        // minCapacity 我称之为最小容量
        // 比较期望容量与最小容量
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        // 判断期望容量是否超过 Integer.MAX_VALUE - 8. 一般很少用到，那么多数据也不会用ArrayList来做容器了吧
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    // 这辈子都不太有机会用到吧
    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }

    public boolean add(E e) {
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        elementData[size++] = e;
        return true;
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

    // 只要将前面的源码读懂，后面的都是类似的

}
```

从上面的源码分析中就可以看出 `ArrayList` 的本质就是数组。`ArrayList` 的一些特性都来源于数组：有序、元素可重复、插入慢、 索引快。
而所谓的动态扩容不就是复制原数组到扩容后的数组。