package com.tim.wang.sourcecode.reflection.methods;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static java.lang.System.out;

/**
 * @author wangjun
 * @date 2020-07-04
 */
public class MethodInvokeDemo {
    static class Student extends Person {
        public void sayHello() {
            out.println("say student hello");
        }
    }

    static class Person {
        public void sayHello() {
            out.println("say person hello");
        }
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        invokeMethod();
        invokeStaticMethod();
        invokeMultiModeMethod();
    }

    @SuppressWarnings("unchecked")
    private static void invokeMultiModeMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class clazz = Person.class;
        Method sayHello = clazz.getMethod("sayHello");
        Object invoke = sayHello.invoke(new Student());
        out.println(invoke);
    }

    @SuppressWarnings("unchecked")
    private static void invokeStaticMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String items = "304";
        Class clazz = Integer.class;
        Method parseInt = clazz.getMethod("parseInt", String.class);
        Object invoke = parseInt.invoke(null, items);
        out.println(invoke);
    }

    @SuppressWarnings("unchecked")
    private static void invokeMethod() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String str = "hello world";
        Class clazz = Class.forName("java.lang.String");
        Class clazz2 = String.class;
        Method subString = clazz.getMethod("substring", int.class);
        Object invoke = subString.invoke(str, 6);
        out.println(invoke);
    }

}
