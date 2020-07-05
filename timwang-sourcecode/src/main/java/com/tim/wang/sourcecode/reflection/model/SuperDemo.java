package com.tim.wang.sourcecode.reflection.model;

/**
 * @author tim.wang
 * @date 2020/5/31 9:45
 */
public class SuperDemo {

    public static void main(String[] args) throws Exception {
        /**
         * 获取父类
         */
//        System.out.println("-----------获取类的父类-------------");
//        Class n = Integer.class.getSuperclass();
//        System.out.println(n);
//        Class o = n.getSuperclass();
//        System.out.println(o);
//        System.out.println(o.getSuperclass());

        /**
         * 获取接口
         * 要特别注意：getInterfaces()只返回当前类直接实现的接口类型，并不包括其父类实现的接口类型：
         */
//        System.out.println("-----------获取类实现的接口--------------");
//        Class s = Integer.class;
//        Class[] is = s.getInterfaces();
//        for (Class i : is) {
//            System.out.println(i);
//        }

        // 问题1：怎么获取父类实现的接口？
        // 问题2：怎么获取接口的父接口














        /**
         * 获取父类接口
         */
//        System.out.println("-----------获取父类的实现接口--------------");
//        Class iClass = Integer.class;
//        Class[] iSuperInter = iClass.getSuperclass().getInterfaces();
//        for (Class i : iSuperInter) {
//            System.out.println(i);
//        }
////
//        System.out.println("------------获取接口的父接口-------------");
//        final Class<?>[] interfaces = Comparable.class.getInterfaces();
//        for(Class c : interfaces) {
//            System.out.println(c);
//        }
//        final Class<?>[] interfaces1 = Closeable.class.getInterfaces();
//        for(Class c : interfaces1) {
//            System.out.println(c);
//        }













        /**
         * 向上转型判断
         */
        System.out.println("--------------向上转型判断----------------");
        Object object = Integer.valueOf(123);
        System.out.println(object instanceof Integer);
        System.out.println(object instanceof Number);
        System.out.println(object instanceof java.io.Serializable);

//        如果是两个Class实例，要判断一个向上转型是否成立，可以调用isAssignableFrom()：
        System.out.println(Integer.class.isAssignableFrom(Integer.class));
        System.out.println(Number.class.isAssignableFrom(Integer.class));
        System.out.println(Object.class.isAssignableFrom(Integer.class));
        System.out.println(Integer.class.isAssignableFrom(Number.class));










        /**
         * 小结
         *         通过Class对象可以获取继承关系：
         *
         *              Class getSuperclass()：获取父类类型；
         *
         *              Class[] getInterfaces()：获取当前类实现的所有接口。
         *
         *             通过Class对象的isAssignableFrom()方法可以判断一个向上转型是否可以实现。
         */
    }
}
