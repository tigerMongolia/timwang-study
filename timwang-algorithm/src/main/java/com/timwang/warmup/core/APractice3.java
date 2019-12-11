package com.timwang.warmup.core;

/**
 * @author wangjun
 * @date 2019-12-11
 */
public class APractice3 {
    private static final int TOTAL_TONS_OF_COALS = 3000;
    private static final int TOTAL_DISTANCE = 1000;
    private static final int PER_TRAIN_TONS = 1000;

    /**
     * 你是山西的一个煤老板，你在矿区开采了有3000吨煤需要运送到市场上去卖，从你的矿区到市场有1000公里，
     * 你手里有一列烧煤的火车，这个火车最多只能装1000吨煤，且其能耗比较大——每一公里需要耗一吨煤。
     * 请问，作为一个懂编程的煤老板的你，你会怎么运送才能运最多的煤到集市？
     *
     * 从动态规划的角度求最优解：
     * 假设起始运送货物量为t，重点路程为s，火车容量为c，可以运抵终点的最多货物量为函数 F(t, s)。
     * 3种基本情况：
     * （1）t < s：货物量不足以运送到此距离，所以F(t, s) = 0;
     * （2）s < t < c：火车一次就可以装完货物，所以F(t, s) = t - s;
     * （3）2s < c 使得火车一次无法运完，但可以采用往返的方式多次运输，这种情况下方式就是减少总共往返的次数，
     *     也就是直接运到终点而不在中间卸货，所以F(t, s) = (t / c - 1) * (c - 2s) - (c - s)
     *
     *  x = (t / c - 1) ==> 总次数
     *  y = (c - 2s) => 一次来回火车所剩余煤的量
     *  z = (c - s) => 最后一次火车去往终点所剩余的量
     *  x * y - z = 运抵终点的最多货物量
     *
     * 可得递归式：F(t, s) = max{ F( F(t, i), s - i)} (1 <= i < s)
     * @param args args
     */
    public static void main(String[] args) {
        int arrNum = TOTAL_TONS_OF_COALS / PER_TRAIN_TONS;
        double[] anMidPoint = new double[arrNum];
        double nTotalDistance = 0;
        for (int i = 0; i < (arrNum - 1); i++) {
            // 每一次
            anMidPoint[i] = PER_TRAIN_TONS / (2 * (arrNum - i) - 1);
            nTotalDistance += anMidPoint[i];
        }
        // 最后一次
        anMidPoint[arrNum - 1] = TOTAL_DISTANCE - nTotalDistance;

        System.out.println(String.format("总煤量 = %s T", TOTAL_TONS_OF_COALS));
        System.out.println(String.format("总运输距离 = %s KM", TOTAL_DISTANCE));
        System.out.println(String.format("运送来回次数 = %s 次" , anMidPoint.length));
        System.out.println(String.format("可以运抵终点的最多货物量为 = %s T" , (PER_TRAIN_TONS - anMidPoint[arrNum - 1])));
    }
}