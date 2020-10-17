# LinkedHashMap底层数据结构？能够实现LRU吗？

LinkedHashMap实现与HashMap的不同之处在于，后者维护着一个运行于所有条目的双重链接列表。此链接列表定义了迭代顺序，该迭代顺序可以是插入顺序或者是访问顺序。重新了newNode构建自己的节点对象。put方法中LinkedHashMap重写了`afterNodeInsertion`和`afterNodeAccess`方法。

```
public class LRUCache<K,V> extends LinkedHashMap<K,V> {
    
  private int cacheSize;
  
  public LRUCache(int cacheSize) {
      super(16,0.75f,true);
      this.cacheSize = cacheSize;
  }

  /**
   * 判断元素个数是否超过缓存容量
   */
  @Override
  protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
      return size() > cacheSize;
  }
}
```

# HashMap什么情况会扩容

什么时候触发扩容，扩容之后的 table.length、阀值各是多少

- 当 size > threshold 的时候进行扩容
- 扩容之后的 table.length = 旧 table.length * 2,
- 扩容之后的 threshold = 旧 threshold * 2

**Map map = new HashMap(1000); 当我们存入多少个元素时会触发map的扩容**

此时的 table.length = 2^10 = 1024; threshold = 1024 * 0.75 = 768; 所以存入第 769 个元素时进行扩容

**Map map1 = new HashMap(10000); 我们存入第 10001个元素时会触发 map1 扩容吗**

此时的 table.length = 2^14 = 16384; threshold = 16384 * 0.75 = 12288; 所以存入第 10001 个元素时不会进行扩容

# HashMap如何处理碰撞

- 链表法就是将相同hash值的对象组织成一个链表放在hash值对应的槽位；

- 开放地址法是通过一个探测算法，当某个槽位已经被占据的情况下继续查找下一个可以使用的槽位。

# HashMap加载因子为什么是0.75

- 如果loadFactor太小，那么map中的table需要不断的扩容，扩容是个耗时的过程
- 如果loadFactor太大，那么map中table放满了也不不会扩容，导致冲突越来越多，解决冲突而起的链表越来越长，效率越来越低
- 而 0.75 这是一个折中的值，是一个比较理想的值

# HashMap **table 的 length 为什么是 2 的 n 次幂**

为了利用位运算 & 求 key 的下标

**求索引的时候为什么是：h&(length-1)，而不是 h&length，更不是 h%length**

- h%length 效率不如位运算快
- h&length 会提高碰撞几率，导致 table 的空间得不到更充分的利用、降低 table 的操作效率
- length-1为了 15就是充分利用table的空间（1111）

# HashMap **table 的初始化时机是什么时候**

一般情况下，在第一次 put 的时候，调用 resize 方法进行 table 的初始化（懒初始化，懒加载思想在很多框架中都有应用！）

初始化的 table.length 是多少、阀值（threshold）是多少，实际能容下多少元素

- 默认情况下，table.length = 16; 指定了 initialCapacity 的情况放到问题 5 中分析
- 默认情况下，threshold = 12; 指定了 initialCapacity 的情况放到问题 5 中分析
- 默认情况下，能存放 12 个元素，当存放第 13 个元素后进行扩容

# **elementData[]为什么使用 transient 修饰**

1. 只是实现了Serializable接口。 
   序列化时，调用java.io.ObjectOutputStream的defaultWriteObject方法，将对象序列化。 
   注意：此时transient修饰的字段，不会被序列化。 

2. 实现了Serializable接口，同时提供了writeObject方法。 
   序列化时，会调用该类的writeObject方法。而不是java.io.ObjectOutputStream的defaultWriteObject方法。 
   注意：此时transient修饰的字段，是否会被序列化，取决于writeObject。

# HashMap PUT操作

虽然是构造函数，但是真正的初始化都是在第一次添加操作里面实现的。

- 在第一次添加操作中，HashMap 会先判断存储数组有没有初始化，如果没有先进行初始化操作，初始化过程中会取比用户指定的容量大的最近的2 的幂次方数作为数组的初始容量，并更新扩容的阈值。

- 先判断有没有初始化
- 再判断传入的key 是否为空，为空保存在table[o] 位置
- key 不为空就对key 进hash，hash 的结果再& 数组的长度就得到存储的位置
- 如果存储位置为空则创建节点，不为空就说明存在冲突
- 解决冲突HashMap 会先遍历链表，如果有相同的value 就更新旧值，否则构建节点添加到链表头
- 添加还要先判断存储的节点数量是否达到阈值，到达阈值要进行扩容
- 扩容扩2倍，是新建数组所以要先转移节点，转移时都重新计算存储位置，可能保持不变可能为旧容量+位置。
- 扩容结束后新插入的元素也得再hash 一遍才能插入。

# Hash1.7 和1.8 最大的不同

- 在hash 取下标时将1.7 的9次扰动（5次按位与和4次位运算）改为2次（一次按位与和一次位运算）
- 1.7 的底层节点为Entry，1.8 为node ，但是本质一样，都是Map.Entry 的实现
- 还有就是在存取数据时添加了关于[树结构的遍历](http://mp.weixin.qq.com/s?__biz=MzIyNDU2ODA4OQ==&mid=2247484145&idx=1&sn=6200268056e6cf5f44ee4324fa5aa95b&chksm=e80db487df7a3d914198b0ea67fe25992d92cfb2ddffc93296e630db04f0fd54c109bf4ee870&scene=21#wechat_redirect)更新与添加操作，并采用了尾插法来避免环形链表的产生
- 但是并发丢失更新的问题依然存在。

# ConcurrentHashMap

- 在1.8中ConcurrentHashMap的get操作全程不需要加锁，这也是它比其他并发集合比如hashtable、用Collections.synchronizedMap()包装的hashmap;安全效率高的原因之一。
- get操作全程不需要加锁是因为Node的成员val是用volatile修饰的和数组用volatile修饰没有关系。
- 数组用volatile修饰主要是保证在数组扩容的时候保证可见性。

（1）：JDK1.7版本的ReentrantLock+Segment+HashEntry（数组）

（2）：JDK1.7采用segment的分段锁机制实现线程安全

（3）：JDK1.8版本中synchronized+CAS+HashEntry（数组）+红黑树

（4）：JDK1.8采用CAS+Synchronized保证线程安全

（5）：查询时间复杂度从原来的遍历链表O（n），变成遍历红黑树O（logN）

# hashset为什么不能重复，依据equals还是hashcode

HashSet的底层还是用HashMap来实现的。将Entry<K,V>的V都变成了同一个Object对象，public static final PRESENT = new Object()。
而HashMap的数据结构是数组+链表+红黑树。

调用K的hashCode方法，然后高低16位进行&运算。得到的hash值，与数组tab[]（桶）的长度-1进行&运算，确定插入对象在哪一个桶上。然后调用对象的equals方法，形成链表。当链表长度大于8时，链表转红黑树。

# linkedhashset和treeset区别，使用上什么区别

**TreeSet**：提供一个使用树结构存储Set接口的实现，对象以升序顺序存储，访问和遍历的时间很快。TreeSet是有序的，而类不是有序的，我们需要将类实现Comparable接口。

**LinkedHashSet**：以元素插入的顺序来维护集合的链接表，允许以插入的顺序在集合中迭代；



