package com.tim.wang.sourcecode.reflection;

import java.lang.reflect.Field;

public class FieldDemo {

    public static void main(String[] args) throws Exception {
        Class stdClass = Student.class;

        /*
         *  Field getField(name)：根据字段名获取某个public的field（包括父类）
         */
//        System.out.println(stdClass.getField("birth")); // private
//        System.out.println(stdClass.getField("score"));  // public
//        System.out.println(stdClass.getField("age"));
//        System.out.println(stdClass.getField("name"));

        /*
         * Field getDeclaredField(name)：根据字段名获取当前类的某个field（不包括父类）
         */
//        System.out.println(stdClass.getDeclaredField("birth"));
//        System.out.println(stdClass.getDeclaredField("score"));
//        System.out.println(stdClass.getDeclaredField("age"));
//        System.out.println(stdClass.getDeclaredField("name"));

        System.out.println("-------------获取所有public的field（包括父类）---------------");
        /*
         * Field[] getFields()：获取所有public的field（包括父类）
         */
//        final Field[] fields = stdClass.getFields();
//        for(Field f : fields) {
//            System.out.println(f);
//            System.out.println(f.getName());
//            System.out.println(f.getType());
//            int m = f.getModifiers();
//            System.out.println(Modifier.isFinal(m));
//            System.out.println(Modifier.isPublic(m));
//            System.out.println(Modifier.isProtected(m));
//            System.out.println(Modifier.isPrivate(m));
//            System.out.println(Modifier.isStatic(m));
//        }

        System.out.println("--------获取当前类的所有field（不包括父类）-----------");
        /*
         * Field[] getDeclaredFields()：获取当前类的所有field（不包括父类）
         */
//        final Field[] declaredFields = stdClass.getDeclaredFields();
//        for(Field f : declaredFields) {
//            System.out.println(f);
//            System.out.println(f.getName());
//            System.out.println(f.getType());
//            int m = f.getModifiers();
//            System.out.println(Modifier.isFinal(m));
//            System.out.println(Modifier.isPublic(m));
//            System.out.println(Modifier.isProtected(m));
//            System.out.println(Modifier.isPrivate(m));
//            System.out.println(Modifier.isStatic(m));
//        }

//        System.out.println("--------------获取某个属性具体值-----------------");
        /*
         * 获取某个属性具体值
         */
        final Person person = new Person(20, "测试");

//        final Class<? extends Person> personClass = stdClass.getClass();
//        final Field ageField = stdClass.getField("age");
        final Field nameField = stdClass.getField("name");

//        ageField.setAccessible(true);
//        final Object age = ageField.get(person);
        final Object name = nameField.get(person);
//        System.out.println("age: " + age);
        System.out.println("name: " + name);








        /*
         * 小结:
         *      Java的反射API提供的Field类封装了字段的所有信息：
         *
         *        通过Class实例的方法可以获取Field实例：getField()，getFields()，getDeclaredField()，getDeclaredFields()；
         *
         *        通过Field实例可以获取字段信息：getName()，getType()，getModifiers()；
         *
         *        通过Field实例可以读取或设置某个对象的字段，如果存在访问限制，要首先调用setAccessible(true)来访问非public字段。
         *
         *        通过反射读写字段是一种非常规方法，它会破坏对象的封装。
         */
    }
}
