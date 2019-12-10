package timwang.concurrent.core.util;

/**
 * @author wangjun
 * @date 2019-10-03
 */
public class StaticSingleton {
    private static final int one = 1;
    private StaticSingleton() {
        System.out.println("StaticSingle is create");
    }

    static {
        System.out.println("???");
    }

    private static class SingletonHolder {
        private static StaticSingleton instance = new StaticSingleton();
        private static int two = 2;

        public SingletonHolder() {
            System.out.println("==========");
        }

        static {
            System.out.println("--------");
        }
    }

    public static StaticSingleton getInstance() {
        return SingletonHolder.instance;
    }

    public static void main(String[] args) {
        System.out.println(one);
        System.out.println(SingletonHolder.two);
    }
}
