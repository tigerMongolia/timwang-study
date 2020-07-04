package com.tim.wang.sourcecode.loadblance;

import java.util.Map;
import java.util.Random;

/**
 * @author wangjun
 * @date 2020-07-03
 */
public class RandomLoadBlanceV2 {

    /**
     * 带权重的随机算法
     * 生成一个随机数
     * 权重类似一个线段上的点，例如5，1，2，3对应的线段就是
     * ____5_6__8__11
     * 就看小于11的随机数落在那个线段区间内，就是哪个ip
     * @return
     */
    public static String getIp() {
        Map<String, Integer> ipWeightList = ServerInfos.getIpWeightList();
        int sumWeight = ipWeightList.values().stream().mapToInt(Integer::intValue).sum();
        int randomWeight = new Random().nextInt(sumWeight);

        for (Map.Entry<String, Integer> entry : ipWeightList.entrySet()) {
            if (randomWeight < entry.getValue()) {
                return entry.getKey();
            }
            randomWeight -= entry.getValue();
        }
        return null;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(getIp());
        }
    }
}
