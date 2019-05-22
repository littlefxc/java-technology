## 概述

双链表实现了List和Deque接口。 实现所有可选列表操作，并允许所有元素（包括null ）。

所有的操作都能像双向列表一样预期。 索引到列表中的操作将从开始或结束遍历列表，以更接近指定的索引为准。

**请注意，此实现不同步。** 如果多个线程同时访问链接列表，并且至少有一个线程在结构上修改列表，则必须在外部进行同步。 （结构修改是添加或删除一个或多个元素的任何操作;仅设置元素的值不是结构修改。）
这通常通过在自然封装列表的对象上进行同步来实现。 如果没有这样的对象存在，列表应该使用 Collections.synchronizedList 方法“包装”。 这最好在创建时完成，以防止意外的不同步访问列表：

    List list = Collections.synchronizedList(new LinkedList(...)); 
  
这个类的 iterator 和 listIterator 方法返回的迭代器是故障快速的 ：如果列表在迭代器创建之后的任何时间被结构化地修改，除了通过迭代器自己的remove或add方法之外，
迭代器将会抛出一个ConcurrentModificationException 。 因此，面对并发修改，迭代器将快速而干净地失败，而不是在未来未确定的时间冒着任意的非确定性行为。

请注意，迭代器的故障快速行为无法保证，因为一般来说，在不同步并发修改的情况下，无法做出任何硬性保证。 
失败快速迭代器尽力投入ConcurrentModificationException 。 因此，编写依赖于此异常的程序的正确性将是错误的：迭代器的故障快速行为应仅用于检测错误。

（以上来自 Java8 api）

## 分析

首先看一下 LinkedList 的继承关系：

![LinkedListUML.png](../images/LinkedListUML.png)

### 定义

```java
public class LinkedList<E>
    extends AbstractSequentialList<E>
    implements List<E>, Deque<E>, Cloneable, java.io.Serializable{}
```

1. LinkedList 是一个继承于 AbstractSequentialList 的双向链表。它也可以被当作堆栈、队列或双端队列进行操作。
最大限度地减少了实现受“连续访问”数据存储（如链接列表）支持的此接口所需的工作,从而以减少实现 List 接口的复杂度。
2. LinkedList 实现 List 接口，能对它进行序列（有序集合）操作。
3. LinkedList 实现 Deque 接口，即能将LinkedList当作双端队列使用。
4. LinkedList 实现了 Cloneable 接口，即覆盖了函数 clone()，能克隆。
5. LinkedList 实现 java.io.Serializable 接口，这意味着 LinkedList 支持序列化，能通过序列化去传输。
6. LinkedList 是非同步的。

### 属性

```java
public class LinkedList<E> extends AbstractSequentialList<E> implements List<E>, Deque<E>, Cloneable, java.io.Serializable{

    transient int size = 0;// list中的元素个数
    
    /**
     * 链表头节点
     * 不变式: (first == null && last == null) || (first.prev == null && first.item != null)
     */
    transient Node<E> first;
    
    /**
     * 链表尾节点
     * 不变式: (first == null && last == null) || (last.next == null && last.item != null)
     */
    transient Node<E> last;
    
    private static class Node<E> {
        E item;// 实际存放的元素
        Node<E> next;// 后一个节点
        Node<E> prev;// 前一个节点
        
        // 构造函数元素顺序分别为前，自己，后。就像排队一样
        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }        
}
```

### 构造方法

由于采用的是链表结构，所以不像 ArrayList 一样，有指定容量的构造方法

```java
public class LinkedList<E> extends AbstractSequentialList<E> implements List<E>, Deque<E>, Cloneable, java.io.Serializable{
     
    /**
      * 构造一个空列表.
      */
     public LinkedList() {
     }
    
     /**
      * 构造一个包含指定 collection 中的元素的列表，这些元素按其 collection 的迭代器返回的顺序排列
      */
     public LinkedList(Collection<? extends E> c) {
         this();// 什么都不做
         addAll(c);// 将 c 集合里的元素添加进链表
     }
     
     /**
       * 按照指定集合的迭代器返回的顺序将指定集合中的所有元素追加到此列表的末尾。
       */
      public boolean addAll(Collection<? extends E> c) {
          return addAll(size, c);
      }
      
      private void checkPositionIndex(int index) {
          if (!isPositionIndex(index))
              throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
      }
      
      /**
        * 判断参数是迭代器或添加操作的有效位置的索引。
        */
       private boolean isPositionIndex(int index) {
           return index >= 0 && index <= size;
       }
  
      /**
       * 从指定位置开始，将指定集合中的所有元素插入此列表。 
       * 将当前位置的元素（如果有）和任何后续元素向右移动（增加其索引）。 
       * 新元素将按照指定集合的迭代器返回的顺序出现在列表中。
       */
      public boolean addAll(int index, Collection<? extends E> c) {
          checkPositionIndex(index);// 检查索引是否正确，即在 0 <= index <= size
  
          Object[] a = c.toArray();// 将 collection 转为数组
          int numNew = a.length;
          if (numNew == 0)
              return false;
  
          Node<E> pred, succ;// 声明 pred 为前置元素，succ 为后置元素
          if (index == size) {// 说明要插入元素的位置就在链表的末尾，后置元素为null，前一个元素就是last
              succ = null;
              pred = last;
          } else { // 说明在链表的中间插入，这时 pred 为原来 index 的 prev，succ 为原来的元素
              succ = node(index);
              pred = succ.prev;
          }
  
          for (Object o : a) {// 遍历数组，逐个添加
              @SuppressWarnings("unchecked") E e = (E) o;
              Node<E> newNode = new Node<>(pred, e, null);
              if (pred == null)
                  first = newNode;
              else
                  pred.next = newNode;
              pred = newNode;
          }
  
          if (succ == null) {// 如果后继元素为空，那么插入完后的最后一个元素，就 pred 就是 last
              last = pred;
          } else {// 否则就维护最后一个元素和之前的元素之间的关系
              pred.next = succ;
              succ.prev = pred;
          }
  
          size += numNew;
          modCount++;// 链表结构发生改动
          return true;
      }
}
```

### 增加

### 移除

### 查询