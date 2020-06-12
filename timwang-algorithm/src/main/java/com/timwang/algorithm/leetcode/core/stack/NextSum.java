package com.timwang.leetcode.core.stack;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author wangjun
 * @date 2020-03-17
 */
public class NextSum {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            String input = sub(in.nextLine());
            System.out.println(input);
            System.out.println(input.length() * (input.length() + 1) / 2);
        }
    }

    private static String sub(String str) {
        StringBuilder result = new StringBuilder();
        List<Character> list = new ArrayList<>();
        char[] cs = str.toCharArray();
        for (char c : cs) {
            if (!list.contains(c)) {
                result.append(c);
                list.add(c);
            }
        }
        return result.toString();
    }
}
