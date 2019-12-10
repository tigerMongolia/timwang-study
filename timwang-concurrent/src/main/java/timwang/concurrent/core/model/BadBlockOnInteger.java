package timwang.concurrent.core.model;

/**
 * @author wangjun
 * @date 2019-10-06
 */
public class BadBlockOnInteger implements Runnable {
    public Integer i = 0;
    static BadBlockOnInteger instance = new BadBlockOnInteger();

    @Override
    public void run() {
        for (int j = 0; j < 10000000; j++) {
            synchronized (i) {
                i++;
            }
        }
    }

    public static void main(String[] args) throws Exception{
        BadBlockOnInteger onInteger = new BadBlockOnInteger();
        Thread one = new Thread(onInteger);
        Thread two = new Thread(onInteger);
        one.start();
        two.start();
        one.join();
        two.join();
        System.out.println(onInteger.i);
    }
}
