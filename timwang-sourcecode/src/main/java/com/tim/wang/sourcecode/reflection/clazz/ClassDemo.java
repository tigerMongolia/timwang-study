package com.tim.wang.sourcecode.reflection.clazz;

import static java.lang.System.out;

/**
 * @author tim.wang
 * @date 2020/5/30 21:11
 */
public class ClassDemo {

    /**
     * 方法一：直接通过一个class的静态变量class获取：
     * Class cls = String.class;
     * 方法二：如果我们有一个实例变量，可以通过该实例变量提供的getClass()方法获取：
     * String s = "Hello";
     * Class cls = s.getClass();
     * 方法三：如果知道一个class的完整类名，可以通过静态方法Class.forName()获取：
     * Class cls = Class.forName("java.lang.String");
     *
     * @param args
     */
    public static void main(String[] args) {
        printClassInfo("".getClass());
        printClassInfo(Runnable.class);
        printClassInfo(java.time.Month.class);
        printClassInfo(String[].class);
        printClassInfo(int.class);
    }

    static void printClassInfo(Class cls) {
        out.println("Class name: " + cls.getName());
        out.println("Simple name: " + cls.getSimpleName());
        if (cls.getPackage() != null) {
            out.println("Package name: " + cls.getPackage().getName());
        }
        out.println("is interface: " + cls.isInterface());
        out.println("is enum: " + cls.isEnum());
        out.println("is array: " + cls.isArray());
        out.println("is primitive: " + cls.isPrimitive());
    }


    /*
     * 小结:
     *    JVM为每个加载的class及interface创建了对应的Class实例来保存class及interface的所有信息；
     *    获取一个class对应的Class实例后，就可以获取该class的所有信息；
     *      通过Class实例获取class信息的方法称为反射（Reflection）；
     *      JVM总是动态加载class，可以在运行期根据条件来控制加载class。
     */

}
