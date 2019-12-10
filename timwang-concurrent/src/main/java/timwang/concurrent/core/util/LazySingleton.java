package timwang.concurrent.core.util;

/**
 * @author wangjun
 * @date 2019-10-03
 */
public class LazySingleton {
    private LazySingleton() {
        System.out.println("LazySingleton is create");
    }

    private static LazySingleton instance = null;

    public static synchronized LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }
}
