package com.timwang.other;

/**
 * @author wangjun
 * @date 2020-03-13
 */
public class FindFloorNumbers {

    public static int findSolutionNumbers(int n, int times, int step, int stepTotal) {
        if (step + stepTotal == n) {
            return times;
        }
        return 0;
    }

}
