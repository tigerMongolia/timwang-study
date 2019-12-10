package timwang.concurrent.core.parallel_compute;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * @author wangjun
 * @date 2019-10-03
 */
public class Div implements Runnable {
    public static BlockingDeque<Msg> blockingDeque = new LinkedBlockingDeque<Msg>();

    @Override
    public void run() {
        while (true) {
            Msg msg = null;
            try {
                TimeUnit.SECONDS.sleep(1);
                msg = blockingDeque.take();
                msg.i = msg.i / 2;
                System.out.println(msg.orgStr + "=" + msg.i);
                System.out.println(System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
