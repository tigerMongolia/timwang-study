package tim.wang.sourcecode.loadblance;

import java.util.Map;

/**
 * @author wangjun
 * @date 2020-07-03
 */
public class RoundLoadBlanceV2 {
    private static int index = 0;
    /**
     * 轮询负载均衡算法，带权重
     * 也类似随机的randomloadblancev2
     * 即把权重变成一个线段，然后下标从0一直移动到队尾
     * @return ip
     */
    public static String getIp() {
        Map<String, Integer> ipWeightList = ServerInfos.getIpWeightList();
        int sumWeight = ipWeightList.values().stream().mapToInt(Integer::intValue).sum();
        if (index > sumWeight) {
            index = 0;
        }
        int currentIndex = index;
        for (Map.Entry<String, Integer> entry : ipWeightList.entrySet()) {
            if (currentIndex <= entry.getValue()) {
                index++;
                return entry.getKey();
            }
            currentIndex -= entry.getValue();
        }
        return null;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 30; i++) {
            System.out.println(getIp());
        }
    }
}
