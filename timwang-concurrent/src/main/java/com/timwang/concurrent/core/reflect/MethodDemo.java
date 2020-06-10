import java.lang.reflect.Method;

public class MethodDemo {
    public static void main(String[] args) throws Exception {
        Class stdClass = Student.class;

        System.out.println("------------获取某个public的Method（包括父类）---------------");
        /**
         * Method getMethod(name, Class...)：获取某个public的Method（包括父类）
          */
//        System.out.println(stdClass.getMethod("learn"));
//        System.out.println(stdClass.getMethod("work"));
//        System.out.println(stdClass.getMethod("drink"));
//        System.out.println(stdClass.getMethod("say", String.class));









        System.out.println("----------获取当前类的某个Method（不包括父类）--------------");
        /**
         * Method getDeclaredMethod(name, Class...)：获取当前类的某个Method（不包括父类）
          */
//        System.out.println(stdClass.getDeclaredMethod("learn"));
//        System.out.println(stdClass.getDeclaredMethod("work"));
//        System.out.println(stdClass.getDeclaredMethod("drink"));
//        System.out.println(stdClass.getDeclaredMethod("say", String.class));









        System.out.println("------------获取所有public的Method（包括父类）--------------");
        /**
         * Method[] getMethods()：获取所有public的Method（包括父类）
          */
//        for(Method method : stdClass.getMethods()) {
//            System.out.println(method);
//        }







        System.out.println("-----------获取当前类的所有Method（不包括父类）---------------");
        /**
         * Method[] getDeclaredMethods()：获取当前类的所有Method（不包括父类）
          */
        for(Method method : stdClass.getDeclaredMethods()) {
            final int modifiers = method.getModifiers();
            System.out.println(method);
        }








        System.out.println("--------------public方法调用-------------------");
        /**
         *  invoke public方法调用
//         */
//        final Method say = stdClass.getMethod("say", String.class);
//        say.invoke(new Student(), "helloworld");











//        System.out.println("--------------非public方法调用-------------------");
        /**
         *  invoke public方法调用
         */
//        final Method work = stdClass.getDeclaredMethod("work");
//        work.setAccessible(true);
//        work.invoke(new Student());













//        System.out.println("--------------static方法调用-------------------");
//        /**
//         *  invoke static方法调用
//         */
//        final Method eat = stdClass.getDeclaredMethod("eat");
//        eat.invoke(null);










//
//        System.out.println("--------------多态方法调用-------------------");
//        /**
//         *  invoke 多态方法调用
//         */
//        final Method hello = Person.class.getDeclaredMethod("hello");
//        hello.invoke(new Student());
//
//        Person p = new Student();
//        p.hello();












        /**
         * 小结:
         *      Java的反射API提供的Method对象封装了方法的所有信息：
         *
         *      通过Class实例的方法可以获取Method实例：getMethod()，getMethods()，getDeclaredMethod()，getDeclaredMethods()；
         *
         *      通过Method实例可以获取方法信息：getName()，getReturnType()，getParameterTypes()，getModifiers()；
         *
         *      通过Method实例可以调用某个对象的方法：Object invoke(Object instance, Object... parameters)；
         *
         *      通过设置setAccessible(true)来访问非public方法；
         *
         *      通过反射调用方法时，仍然遵循多态原则。
         */

    }
}
