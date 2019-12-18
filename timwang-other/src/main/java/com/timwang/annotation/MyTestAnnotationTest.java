package com.timwang.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author wangjun
 * @date 2019-12-18
 */
public class MyTestAnnotationTest {
    public static void main(String[] args) throws NoSuchMethodException {

        /**
         * 获取类注解属性
         */
        Class<Father> fatherClass = Father.class;
        boolean annotationPresent = fatherClass.isAnnotationPresent(MyTestAnnotation.class);
        if(annotationPresent){
            MyTestAnnotation annotation = fatherClass.getAnnotation(MyTestAnnotation.class);
            System.out.println(annotation.name());
            System.out.println(annotation.age());
        }
    }
}
