package tim.wang.sourcecode.reflection;

import java.lang.reflect.Constructor;

import static java.lang.System.out;

/*
 * @author tim.wang
 * @date 2020/5/31 0:05
 */
public class ConstructorDemo {

    public static void main(String[] args) throws Exception {
        final Class<Person> personClass = Person.class;

        /*
         * 调用Class提供的newInstance()方法：
         */
        final Person person = personClass.newInstance();
        out.println(person.toString());


        /*
         * getConstructor(Class...)：获取某个public的Constructor；
         */
        out.println("-----------获取某个public的Constructor-------------");
        final Constructor<Person> cons2 = personClass.getConstructor(null);
        final Object o2 = cons2.newInstance();
        out.println(o2);

        /*
         * getDeclaredConstructor(Class...)：获取某个Constructor；
         */
        out.println("----------获取某个Constructor；------------");
        Constructor cons1 = personClass.getDeclaredConstructor(int.class, String.class);
        final Object o = cons1.newInstance(20, "测试");
        out.println(o);

        /*
         * getConstructors()：获取所有public的Constructor；
         */
        out.println("----------获取所有public的Constructor；---------");
        for (Constructor cons : personClass.getConstructors()) {
            out.println(cons);
        }

        /*
         * getDeclaredConstructors()：获取所有Constructor
         */
        out.println("----------获取所有Constructor---------");
        for (Constructor cons : personClass.getDeclaredConstructors()) {
            out.println(cons);
        }


        /*
         * 小结：
         *    Constructor对象封装了构造方法的所有信息；
         *
         *      通过Class实例的方法可以获取Constructor实例：getConstructor()，getConstructors()，getDeclaredConstructor()，getDeclaredConstructors()；
         *
         *      通过Constructor实例可以创建一个实例对象：newInstance(Object... parameters)； 通过设置setAccessible(true)来访问非public构造方法。
         */
    }
}
