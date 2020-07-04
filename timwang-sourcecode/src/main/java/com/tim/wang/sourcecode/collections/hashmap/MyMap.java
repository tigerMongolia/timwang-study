package com.tim.wang.sourcecode.collections.hashmap;

/**
 * @author wangjun
 * @date 2020-06-12
 */
public interface MyMap<K, V> {

    public V put(K key, V value);

    public V get(K key);

    public V remove(K key);

    public int size();

    interface MyEntry<K, V> {

    }

}
