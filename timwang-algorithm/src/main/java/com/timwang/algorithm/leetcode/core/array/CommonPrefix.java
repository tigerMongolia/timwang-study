package com.timwang.algorithm.leetcode.core.array;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangjun
 * @date 2020-08-12
 */
public class CommonPrefix {
    public String longestCommonPrefix(String[] strs) {
            if (strs == null || strs.length == 0) {
            return "";
        }
        int minLen = findMinLen(strs);
        StringBuilder result = new StringBuilder("");
        for (int i = 0; i < minLen; i++) {
            char charCommonPrefix = getCommonPrefix(strs, i);
            if (0 != charCommonPrefix) {
                result.append(charCommonPrefix);
            } else {
                break;
            }
        }
        return result.toString();
    }


    private char getCommonPrefix(String[] strs, int i) {
        int len = strs.length;
        for (String item : strs) {
            if (item.length() <= i) {
                return 0;
            }
            if (item.charAt(i) == strs[0].charAt(i)) {
                len--;
            }

        }
        return len == 0 ? strs[0].charAt(i) : 0;
    }

    private int findMinLen(String[] strs) {
        int min = strs[0].length();
        for (String item : strs) {
            if (item.length() < min) {
                min = item.length();
            }
        }
        return min;
    }

}
