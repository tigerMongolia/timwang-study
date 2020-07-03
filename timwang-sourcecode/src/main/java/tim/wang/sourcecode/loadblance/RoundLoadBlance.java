package tim.wang.sourcecode.loadblance;

import java.util.List;

/**
 * @author wangjun
 * @date 2020-07-02
 */
public class RoundLoadBlance {
    private static int index = 0;

    /**
     * 简单的轮询负载均衡算法，不带权重，index++即可
     * @return ip
     */
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
