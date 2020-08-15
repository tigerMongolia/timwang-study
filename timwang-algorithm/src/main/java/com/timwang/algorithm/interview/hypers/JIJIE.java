package com.timwang.algorithm.interview.hypers;

/**
 * @author wangjun
 * @date 2020-08-13
 */
public class JIJIE {
    public static int maxValue(int[] numbers) {
        int min = numbers[0] < numbers[1] ? numbers[0] : numbers[1];
        int ans = numbers[1] - numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] - min > ans) ans = numbers[i] - min;
            if (numbers[i] < min) min = numbers[i];
        }
        return ans > 0 ? ans : 0;
    }

    public static void main(String[] args) {
//        int[] numbers = new int[]{1,8,3,10,20,-1};
        int[] numbers = new int[]{10,13,14,18,3,9};
        System.out.println(maxValue(numbers));
    }
}
