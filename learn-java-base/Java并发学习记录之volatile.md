# Java并发学习记录之volatile

首先来个感性认识：
- `volatile` 关键字仅能实现对原始变量(如boolen、short、int、long等)赋值操作的原子性，但是复合操作如 `i++`则不保证。


 