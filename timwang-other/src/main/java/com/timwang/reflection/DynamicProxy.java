package com.timwang.reflection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/**
 * @author wangjun
 * @date 2020-04-06
 */
public class DynamicProxy {

    interface Hello {
        void morning(String name);
    }

    public static void main(String[] args) {
        InvocationHandler invocationHandler = (proxy, method, args1) -> {
            System.out.println(method);
            if (method.getName().equals("morning")) {
                System.out.println("good morning " + args1[0]);
            }
            return null;
        };
        Hello hello = (Hello) Proxy.newProxyInstance(
                Hello.class.getClassLoader(),
                new Class[]{Hello.class},
                invocationHandler
        );

        hello.morning("tim");
    }
}
