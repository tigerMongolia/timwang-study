package timwang.concurrent.core.model;

/**
 * @author wangjun
 * @date 2019-10-03
 */
public class BadBlock2OnInteger implements Runnable {
    static int i = 0;
    public static synchronized void increase() {
        i++;
    }
    
    @Override
    public void run() {
        for (int j = 0; j < 10000000; j++) {
            increase();
        }
    }

    public static void main(String[] args) throws Exception{
        Thread one = new Thread(new BadBlock2OnInteger());
        Thread two = new Thread(new BadBlock2OnInteger());
        one.start();
        two.start();
        one.join();
        two.join();
        System.out.println(i);
    }
}
