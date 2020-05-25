package com.timwang.leetcode.core.stack;

/**
 * @author wangjun
 * @date 2020-04-07
 * @see https://leetcode-cn.com/problems/trapping-rain-water/
 */
public class RainCode {
    public static void main(String[] args) {
//        Solution solution = new Solution();
//        int trap = solution.trap(new int[]{0,3,0,1,1,0,1,3,2,1,2,1});
//        System.out.println(trap);

        Solution2 solution2 = new Solution2();
        int trap = solution2.trap(new int[]{0,1,0,2,1,0,1,3,2,1,2,1});
        System.out.println(trap);
    }



    static class Solution2 {
        /**
         * 解题思路：找到最高的，从左边到最高点，从右边到最高点
         * 复杂度，O(n)
         */
        public int trap(int[] height) {
            if (height == null || height.length == 0) {
                return 0;
            }
            int maxHeightItem = findMaxHeight(height);
            int result = 0;
            int temp = 0;
            int maxLeft = height[0];
            int maxRight = height[height.length - 1];
            for (int i = 1; i <= maxHeightItem; i++) {
                if (maxLeft > height[i]) {
                    temp += (maxLeft - height[i]);
                }
                if (height[i] >= maxLeft) {
                    maxLeft = height[i];
                    result += temp;
                    temp = 0;
                }
            }
            for (int i = height.length - 1; i >= maxHeightItem; i--) {
                if (maxRight > height[i]) {
                    temp += (maxRight - height[i]);
                }
                if (height[i] >= maxRight) {
                    maxRight = height[i];
                    result += temp;
                    temp = 0;
                }
            }
            return result;
        }

        private int findMaxHeight(int[] height) {
            int maxItem = 0;
            int max = 0;
            for (int i = 0; i < height.length; i++) {
                if (height[i] >= max) {
                    maxItem = i;
                    max = height[i];
                }
            }
            return maxItem;
        }


    }

    static class Solution {
        public int trap(int[] height) {
            int maxHeight = 0;
            for (int item : height) {
                if (item >= maxHeight) {
                    maxHeight = item;
                }
            }

            int total = 0;
            for (int floor = 0; floor < maxHeight; floor++) {
                total += findThisHeightTotal(floor, height);
            }
            return total;
        }

        private int findThisHeightTotal(int floor, int[] height) {
            int thisFloorTotal = 0;
            int result = 0;
            int nowIndex = 0;
            for (; nowIndex < height.length; nowIndex++) {
                if (height[nowIndex] <= floor) {
                    continue;
                }
                for (int j = nowIndex + 1; j < height.length; j++) {
                    if (height[j] - floor < 0) {
                        thisFloorTotal++;
                        continue;
                    }
                    if (thisFloorTotal >= 0) {
                        nowIndex = j - 1;
                    }
                    if (height[j] - floor > 0   ) {
                        result = thisFloorTotal;
                    }
                }
            }
            return result;
        }
    }
}
