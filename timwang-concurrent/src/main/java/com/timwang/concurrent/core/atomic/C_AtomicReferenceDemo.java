package com.timwang.concurrent.core.atomic;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

public class C_AtomicReferenceDemo {
    //演示AtomicReference类的使用
    @Test
    public void demo1() {
        User zhangSan = new User("张三", 10);
        User liSi = new User("李四", 15);

        AtomicReference<User> atomicReference = new AtomicReference<>();
        //这一步别忘了
        atomicReference.set(zhangSan);

        //参数1：预期值
        //参数2：期望修改成的值
        System.out.println(atomicReference.compareAndSet(zhangSan, liSi));
        System.out.println(atomicReference.get().toString());

        System.out.println(atomicReference.compareAndSet(zhangSan, liSi));
        System.out.println(atomicReference.get().toString());
    }

}
