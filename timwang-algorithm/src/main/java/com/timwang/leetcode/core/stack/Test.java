package com.timwang.leetcode.core.stack;

/**
 * @author wangjun
 * @date 2020-02-24
 */
public class Test {
    public static Character mostFrequentLetter(String string) {
        int max = string.toLowerCase().charAt(0) - 'a';
        int[] num = new int[26];
        for (int i = 0; i < num.length; i++) {
            num[i] = 0;
        }
        for (int i = 0; i < string.toLowerCase().length(); i++) {
            int x = string.charAt(i) - 97;
            if (x < 0) {
                continue;
            }
            num[x]++;
            if (num[x] > num[max]) {
                max = x;
            }
        }
        return (char) (97 + max);
    }

    public static void main(String[] args) {
        Character ashdaskjchzxkjcbkczmxcn = mostFrequentLetter("_123_");
        System.out.println(ashdaskjchzxkjcbkczmxcn);
    }
}
