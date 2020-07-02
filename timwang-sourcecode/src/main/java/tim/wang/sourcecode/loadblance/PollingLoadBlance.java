package tim.wang.sourcecode.loadblance;

import java.util.List;

/**
 * @author wangjun
 * @date 2020-07-02
 */
public class PollingLoadBlance {
    private static int index = 0;

    public static String getIp() {
        List<String> ipList = ServerInfos.getIpList();
        if (index > ipList.size() - 1) {
            index = 0;
        }
        return ipList.get(index++);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(getIp());
        }
    }
}
