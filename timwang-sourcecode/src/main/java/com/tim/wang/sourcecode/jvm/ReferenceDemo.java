package com.tim.wang.sourcecode.jvm;

import java.lang.ref.SoftReference;

/**
 * @author wangjun
 * @date 2020-03-31
 */
public class ReferenceDemo {

    public static void main(String[] args) {
        strongReference();

        Object obj = new Object();
        SoftReference<Object> softReference = new SoftReference<>(obj);
        obj = null;

        System.gc();

        System.out.println(obj);
        // 此时内存够用
        System.out.println(softReference.get());
    }

    private static void strongReference() {
        // 这样定义默认强引用
        Object obj = new Object();
        Object objRefernce = obj;
        obj = null;
        System.gc();
        System.out.println(objRefernce);
    }

}
