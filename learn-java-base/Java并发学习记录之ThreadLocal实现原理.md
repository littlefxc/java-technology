# ThreadLocal 的实现原理

JDK 版本：1.8
<hr>

我们需要关注的，自然是 ThreadLocal 的 set() 方法和 get() 方法。

## ThreadLocal 的 set() 方法：

```java
public class ThreadLocal<T> {
    
    public void set(T value) {
        // 获取当前线程对象
        Thread t = Thread.currentThread();
        // 获得当前线程的 ThreadLocalMap
        ThreadLocalMap map = getMap(t);
        // 将值放入 ThreadLocalMap 中
        if (map != null)
            map.set(this, value);
        else
            createMap(t, value);
    }
    
    // ...
}
```

在 set 时，首先获得当前线程对象，然后通过 getMap() 拿到线程的 ThreadLocalMap，并将值放入
 ThreadLocalMap 中。
 
**那么这个 ThreadLocalMap 和 Thread 有什么关系？**
 
 可以理解为一个 Map (虽然不是，但可以简单地理解为 HashMap)，但是它是定义在 Thread 内部的成员。
 
 ```java
public class Thread implements Runnable {
    /* 与此线程相关的 ThreadLocal 值。这个 Map 由 ThreadLocal 类维护。 */
    ThreadLocal.ThreadLocalMap threadLocals = null;
    
    // ...
}
```

而设置到 ThreadLocal 中的值，就是写入 threadLocals 这个 Map。其中 key 为 ThreadLocal 当前对象，
value 就是我们需要的值。而 threads 本身就保存了自己所在线程的所有 “局部变量”，也就是一个 ThreadLocal 变量的集合。

而 get() 方法也是先取得当前线程的 ThreadLocalMap 对象。然后，通过将自己作为 key 取得内部的实际数据。

同时也可以看到在 Thread 的 exit() 方法中， Thread 类会进行一些清理工作，注意下述代码：

```java
public class Thread implements Runnable {
    /**
     * 系统调用此方法是为了让线程在实际退出之前有机会进行清理。
     */
    private void exit() {
        if (group != null) {
            group.threadTerminated(this);
            group = null;
        }
        /* 侵略性地清除所有引用字段:参见bug 4006245 */
        target = null;
        /* 加快这些资源的释放 */
        threadLocals = null;
        inheritableThreadLocals = null;
        inheritedAccessControlContext = null;
        blocker = null;
        uncaughtExceptionHandler = null;
    }
}
```

因此如果我们使用线程池，那就意味着线程未必会退出。如果这样，将一些对象设置到 ThreadLocal 中，
可能会使系统出现内存泄漏（JVM无法回收你不再使用的对象）的可能。

但又要及时回收对象，就可以使用 ThreadLocal.remove()，告诉JVM，回收对象，防止内存泄漏。

**那么，ThreadLocalMap 是什么鬼？**

首先，通过前面的 ThreadLocal 的核心的 set() 方法分析，ThreadLocal 的 set 方法是通过代理给它的内部类ThreadLocalMap实现的。
于是对于 ThreadLocal 的分析就转换为对内部类 ThreadLocalMap 的分析。

## 对 ThreadLocalMap 的 set() 方法和相关属性/方法的分析：

```java
/**
* ThreadLocalMap是一个定制的哈希映射，只适合维护线程本地值。
* 在ThreadLocal类之外不导出任何操作。类是包私有的，允许在类线程中声明字段。
* 为了帮助处理非常大且长期存在的使用，哈希表项对键使用弱引用。
* 但是，由于没有使用引用队列，因此只有在表开始耗尽空间时才保证删除过时的条目。
*/
static class ThreadLocalMap {

    /**
     * 该类继承了WeakReference是方便垃圾回收，在底层map扩容之前进行entry的回收，
     * 减少扩容的概率,提高性能
     */
    static class Entry extends WeakReference<ThreadLocal<?>> {
        /** The value associated with this ThreadLocal. */
        Object value;

        Entry(ThreadLocal<?> k, Object v) {
            super(k);
            value = v;
        }
    }

    /**
     * 初始容量
     */
    private static final int INITIAL_CAPACITY = 16;

    /**
     * 底层数组
     */
    private Entry[] table;

    /**
     * map中entry的个数
     */
    private int size = 0;

    /**
     * 阈值，超过这个阈值之后就需要进行扩容
     */
    private int threshold; // Default to 0

    /**
     * 阈值是底层数组长度的2/3
     */
    private void setThreshold(int len) {
        threshold = len * 2 / 3;
    }

    /**
     * 计算下一个索引，hash算法定位失败的时候（也就是该索引位置存在元素）
     */
    private static int nextIndex(int i, int len) {
        return ((i + 1 < len) ? i + 1 : 0);
    }

    /**
     * 上一个位置索引，hash算法定位失败的时候（也就是该索引位置存在元素）
     */
    private static int prevIndex(int i, int len) {
        return ((i - 1 >= 0) ? i - 1 : len - 1);
    }

    /**
     * 根据 key 和 value 构建 ThreadLocalMap
     */
    ThreadLocalMap(ThreadLocal<?> firstKey, Object firstValue) {
        //初始化table
        table = new Entry[INITIAL_CAPACITY];
        //计算索引
        // & (INITIAL_CAPACITY - 1) 这是取模的一种方式，对于2的幂作为模数取模，用此代替%(2^n)
        // firstKey.threadLocalHashCode 其主要目的就是为了让哈希码能均匀的分布在2的n次方的数组里, 也就是Entry[] table中
        int i = firstKey.threadLocalHashCode & (INITIAL_CAPACITY - 1);
        //设置值
        table[i] = new Entry(firstKey, firstValue);
        size = 1;
        //设置阈值
        setThreshold(INITIAL_CAPACITY);
    }

    /**
     * 根据父容器构造ThreadLocalMap
     *
     * @param parentMap the map associated with parent thread.
     */
    private ThreadLocalMap(ThreadLocalMap parentMap) {
        Entry[] parentTable = parentMap.table;
        int len = parentTable.length;
        setThreshold(len);
        table = new Entry[len];
        //复制遍历
        for (int j = 0; j < len; j++) {
            Entry e = parentTable[j];
            if (e != null) {
                @SuppressWarnings("unchecked")
                ThreadLocal<Object> key = (ThreadLocal<Object>) e.get();
                if (key != null) {
                    Object value = key.childValue(e.value);
                    Entry c = new Entry(key, value);
                    //该句相当于 hashcode % len但是&运算更加高效
                    int h = key.threadLocalHashCode & (len - 1);
                    //hash算法定位失败，找下一个索引
                    while (table[h] != null)
                        h = nextIndex(h, len);
                    table[h] = c;
                    size++;
                }
            }
        }
    }

    /**
     * ThreadLocalMap使用线性探测法来解决哈希冲突，线性探测法的地址增量di = 1, 2, … , m-1，
     * 其中，i为探测次数。该方法一次探测下一个地址，直到有空的地址后插入，若整个空间都找不到空余的地址，则产生溢出。
     * 假设当前table长度为16，也就是说如果计算出来key的hash值为14，如果table[14]上已经有值，并且其key与当前key不一致，
     * 那么就发生了hash冲突，这个时候将14加1得到15，取table[15]进行判断，
     * 这个时候如果还是冲突会回到0，取table[0],以此类推，直到可以插入。
     *
     */
    private void set(ThreadLocal<?> key, Object value) {

        Entry[] tab = table;
        int len = tab.length;
         //计算索引。
        int i = key.threadLocalHashCode & (len-1);

        // 根据获取到的索引进行循环，如果当前索引上的table[i]不为空，在没有return的情况下，
        // 就使用nextIndex()获取下一个（方法注释上提到到线性探测法）。
        for (Entry e = tab[i];
             e != null;
             e = tab[i = nextIndex(i, len)]) {
            ThreadLocal<?> k = e.get();
            //table[i]上key不为空，并且和当前key相同，更新value
            if (k == key) {
                e.value = value;
                return;
            }
            //table[i]上的key为空，说明被回收了（上面的弱引用中提到过）。
            //这个时候说明改table[i]可以重新使用，用新的key-value将其替换,并删除其他无效的entry
            if (k == null) {
                replaceStaleEntry(key, value, i);
                return;
            }
        }
        //找到为空的插入位置，插入值，在为空的位置插入需要对size进行加1操作
        tab[i] = new Entry(key, value);
        int sz = ++size;
        /**
         * cleanSomeSlots用于清除那些e.get()==null，也就是table[index] != null && table[index].get()==null
         * 之前提到过，这种数据key关联的对象已经被回收，所以这个Entry(table[index])可以被置null。
         * 如果没有清除任何entry,并且当前使用量达到了负载因子所定义(长度的2/3)，那么进行rehash()
         */
        if (!cleanSomeSlots(i, sz) && sz >= threshold)
            rehash();
    }

    /**
     * 替换无效entry
     */
    private void replaceStaleEntry(ThreadLocal<?> key, Object value,
                                   int staleSlot) {
        Entry[] tab = table;
        int len = tab.length;
        Entry e;

        /**
         * 根据传入的无效entry的位置（staleSlot）,向前扫描
         * 一段连续的entry(这里的连续是指一段相邻的entry并且table[i] != null),
         * 直到找到一个无效entry，或者扫描完也没找到
         */
        int slotToExpunge = staleSlot;//之后用于清理的起点
        for (int i = prevIndex(staleSlot, len);
             (e = tab[i]) != null;
             i = prevIndex(i, len))
            if (e.get() == null)
                slotToExpunge = i;

        // 向后扫描一段连续的entry
        for (int i = nextIndex(staleSlot, len);
             (e = tab[i]) != null;
             i = nextIndex(i, len)) {
            ThreadLocal<?> k = e.get();

            //  如果找到了key，将其与传入的无效entry替换，也就是与table[staleSlot]进行替换
            if (k == key) {
                e.value = value;

                tab[i] = tab[staleSlot];
                tab[staleSlot] = e;

                //如果向前查找没有找到无效entry，则更新slotToExpunge为当前值i
                if (slotToExpunge == staleSlot)
                    slotToExpunge = i;
                cleanSomeSlots(expungeStaleEntry(slotToExpunge), len);
                return;
            }

            // 如果向前查找没有找到无效entry，并且当前向后扫描的entry无效，则更新slotToExpunge为当前值i
            if (k == null && slotToExpunge == staleSlot)
                slotToExpunge = i;
        }

        // 如果没有找到key,也就是说key之前不存在table中，就直接最开始的无效entry——tab[staleSlot]上直接新增即可
        tab[staleSlot].value = null;
        tab[staleSlot] = new Entry(key, value);

        // slotToExpunge != staleSlot,说明存在其他的无效entry需要进行清理。
        if (slotToExpunge != staleSlot)
            cleanSomeSlots(expungeStaleEntry(slotToExpunge), len);
    }

    /**
     * 连续段清除
     * 根据传入的staleSlot,清理对应的无效entry——table[staleSlot],
     * 并且根据当前传入的staleSlot,向后扫描一段连续的entry(这里的连续是指一段相邻的entry并且table[i] != null),
     * 对可能存在hash冲突的entry进行rehash，并且清理遇到的无效entry.
     *
     * @param staleSlot key为null,需要无效entry所在的table中的索引
     * @return 返回下一个为空的solt的索引。
     */
    private int expungeStaleEntry(int staleSlot) {
        Entry[] tab = table;
        int len = tab.length;

        // 清理无效entry，置空
        tab[staleSlot].value = null;
        tab[staleSlot] = null;
        //size减1，置空后table的被使用量减1
        size--;

        // Rehash until we encounter null
        Entry e;
        int i;
        // 从staleSlot开始向后扫描一段连续的entry
        for (i = nextIndex(staleSlot, len);
             (e = tab[i]) != null;
             i = nextIndex(i, len)) {
            ThreadLocal<?> k = e.get();
            //如果遇到key为null,表示无效entry，进行清理.
            if (k == null) {
                e.value = null;
                tab[i] = null;
                size--;
            } else {
                //如果key不为null,计算索引
                int h = k.threadLocalHashCode & (len - 1);
                /**
                 * 计算出来的索引——h，与其现在所在位置的索引——i不一致，置空当前的table[i]
                 * 从h开始向后线性探测到第一个空的slot，把当前的entry挪过去。
                 */
                if (h != i) {
                    tab[i] = null;

                    // Unlike Knuth 6.4 Algorithm R, we must scan until
                    // null because multiple entries could have been stale.
                    while (tab[h] != null)
                        h = nextIndex(h, len);
                    tab[h] = e;
                }
            }
        }
        //下一个为空的solt的索引。
        return i;
    }

    /**
     * 启发式的扫描清除，扫描次数由传入的参数n决定
     *
     * @param i 从i向后开始扫描（不包括i，因为索引为i的Slot肯定为null）
     *
     * @param n 控制扫描次数，正常情况下为 log2(n) ，
     * 如果找到了无效entry，会将n重置为table的长度len,进行段清除。
     *
     * map.set()点用的时候传入的是元素个数，replaceStaleEntry()调用的时候传入的是table的长度len
     *
     * @return true if any stale entries have been removed.
     */
    private boolean cleanSomeSlots(int i, int n) {
        boolean removed = false;
        Entry[] tab = table;
        int len = tab.length;
        do {
            i = nextIndex(i, len);
            Entry e = tab[i];
            if (e != null && e.get() == null) {
                n = len;
                removed = true;
                i = expungeStaleEntry(i);
            }
        } while ( (n >>>= 1) != 0);
        return removed;
    }

    /**
     * Re-pack and/or re-size the table. First scan the entire
     * table removing stale entries. If this doesn't sufficiently
     * shrink the size of the table, double the table size.
     */
    private void rehash() {
        //全清理
        expungeStaleEntries();

        // threshold = 2/3 * len，所以threshold - threshold / 4 = 1en/2
        // 这里主要是因为上面做了一次全清理所以size减小，需要进行判断。
        // 判断的时候把阈值调低了。
        if (size >= threshold - threshold / 4)
            resize();
    }

    /**
     * 扩容，扩大为原来的2倍（这样保证了长度为2的冥）
     */
    private void resize() {
        Entry[] oldTab = table;
        int oldLen = oldTab.length;
        int newLen = oldLen * 2;
        Entry[] newTab = new Entry[newLen];
        int count = 0;

        for (int j = 0; j < oldLen; ++j) {
            Entry e = oldTab[j];
            if (e != null) {
                ThreadLocal<?> k = e.get();
                //虽然做过一次清理，但在扩容的时候可能会又存在key==null的情况。
                if (k == null) {
                    e.value = null; // Help the GC
                } else {
                    //同样适用线性探测来设置值。
                    int h = k.threadLocalHashCode & (newLen - 1);
                    while (newTab[h] != null)
                        h = nextIndex(h, newLen);
                    newTab[h] = e;
                    count++;
                }
            }
        }
        //设置新的阈值
        setThreshold(newLen);
        size = count;
        table = newTab;
    }

    /**
     * 全清理，清理所有无效entry
     */
    private void expungeStaleEntries() {
        Entry[] tab = table;
        int len = tab.length;
        for (int j = 0; j < len; j++) {
            Entry e = tab[j];
            if (e != null && e.get() == null)
                //使用连续段清理
                expungeStaleEntry(j);
        }
    }
}
```

从上面的分析，从 ThreadLocal 的 set() 着手分析再深入到 ThreadLocalMap 的 set() 方法。

同样的对于 ThreadLocalMap 中的 getEntry() 也从 ThreadLocal 的 get() 方法入手。

## ThreadLocal 中的 get()

```java
public T get() {
    //同set方法类似获取对应线程中的ThreadLocalMap实例
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null) {
        ThreadLocalMap.Entry e = map.getEntry(this);
        if (e != null) {
            @SuppressWarnings("unchecked")
            T result = (T)e.value;
            return result;
        }
    }
    //为空返回初始化值
    return setInitialValue();
}
/**
 * 初始化设值的方法，可以被子类覆盖。
 */
protected T initialValue() {
   return null;
}

private T setInitialValue() {
    //获取初始化值，默认为null(如果没有子类进行覆盖)
    T value = initialValue();
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    //不为空不用再初始化，直接调用set操作设值
    if (map != null)
        map.set(this, value);
    else
        //第一次初始化，createMap在上面介绍set()的时候有介绍过。
        createMap(t, value);
    return value;
}
```

## ThreadLocalMap 中的 getEntry()

```java
private ThreadLocal.ThreadLocalMap.Entry getEntry(ThreadLocal<?> key) {
    //根据key计算索引，获取entry
    int i = key.threadLocalHashCode & (table.length - 1);
    ThreadLocal.ThreadLocalMap.Entry e = table[i];
    if (e != null && e.get() == key)
        return e;
    else
        return getEntryAfterMiss(key, i, e);
}

/**
 * 通过直接计算出来的key找不到对于的value的时候适用这个方法.
 */
private ThreadLocal.ThreadLocalMap.Entry getEntryAfterMiss(ThreadLocal<?> key, int i, ThreadLocal.ThreadLocalMap.Entry e) {
    ThreadLocal.ThreadLocalMap.Entry[] tab = table;
    int len = tab.length;

    while (e != null) {
        ThreadLocal<?> k = e.get();
        if (k == key)
            return e;
        if (k == null)
            //清除无效的entry
            expungeStaleEntry(i);
        else
            //基于线性探测法向后扫描
            i = nextIndex(i, len);
        e = tab[i];
    }
    return null;
}
```

## ThreadLocalMap中的remove()

同样的 remove() ，就是找到对应的table[],调用 weakrefrence 的 clear()清除引用，
然后再调用 expungeStaleEntry() 进行清除。

```java
private void remove(ThreadLocal<?> key) {
    ThreadLocal.ThreadLocalMap.Entry[] tab = table;
    int len = tab.length;
    //计算索引
    int i = key.threadLocalHashCode & (len-1);
    //进行线性探测，查找正确的key
    for (ThreadLocal.ThreadLocalMap.Entry e = tab[i];
         e != null;
         e = tab[i = nextIndex(i, len)]) {
        if (e.get() == key) {
            //调用weakrefrence的clear()清除引用
            e.clear();
            //连续段清除
            expungeStaleEntry(i);
            return;
        }
    }
}
```

## 参考引用

[哈希表——线性探测法、链地址法、查找成功、查找不成功的平均长度](https://blog.csdn.net/u011080472/article/details/51177412)

