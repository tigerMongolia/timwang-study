package tim.wang.sourcecode.collections.hashmap;

import java.util.Objects;

/**
 * @see https://zhuanlan.zhihu.com/p/34280652
 * @see https://juejin.im/post/5cb163bee51d456e46603dfe
 * @see https://juejin.im/post/5c8f461c5188252da90125ba
 * @author wangjun
 * @date 2020-06-15
 */
public class ExplainHashMap<K, V> {
    /**
     * 学习重点：
     * （1）允许NULL值，NULL键
     * （2）不要轻易改变负载因子，负载因子过高会导致链表过长，查找键值对时间复杂度就会增高，负载因子过低会导致hash桶的 数量过多，空间复杂度会增高
     * （3）Hash表每次会扩容长度为以前的2倍
     * （4）HashMap是多线程不安全的，我在JDK1.7进行多线程put操作，之后遍历，直接死循环，CPU飙到100%，
     *     在JDK 1.8中进行多线程操作会出现节点和value值丢失，为什么JDK1.7与JDK1.8多线程操作会出现很大不同，
     *     是因为JDK 1.8的作者对resize方法进行了优化不会产生链表闭环
     * （5）尽量设置HashMap的初始容量，尤其在数据量大的时候，防止多次resize
     */


    /**
     * ！！类常量！！
     */

    //默认hash桶初始长度16
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

    //hash表最大容量2的30次幂
    static final int MAXIMUM_CAPACITY = 1 << 30;

    //默认负载因子 0.75
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    //链表的数量大于等于8个并且桶的数量大于等于64时链表树化
    static final int TREEIFY_THRESHOLD = 8;

    //hash表某个节点链表的数量小于等于6时树拆分
    static final int UNTREEIFY_THRESHOLD = 6;

    //树化时最小桶的数量
    static final int MIN_TREEIFY_CAPACITY = 64;

    /**
     * ！！实例变量！！
     */
    //hash桶
    // transient Node<K,V>[] table;

    //键值对的数量
    transient int size;

    //HashMap结构修改的次数
    transient int modCount;

    //扩容的阀值，当键值对的数量超过这个阀值会产生扩容
    int threshold;


}
