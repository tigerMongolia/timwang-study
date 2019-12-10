package com.timwang.leetcode.core.stack;

import java.util.*;

/**
 * @author wangjun
 * @date 2019-10-30
 */
public class APractice2 {
    /**
     * 给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串，判断字符串是否有效。
     *
     * 有效字符串需满足：
     *
     * 左括号必须用相同类型的右括号闭合。
     * 左括号必须以正确的顺序闭合。
     * 注意空字符串可被认为是有效字符串。
     *
     * 示例 1:
     *
     * 输入: "()"
     * 输出: true
     *
     * 示例 2:
     *
     * 输入: "()[]{}"
     * 输出: true
     *
     * 示例 3:
     *
     * 输入: "(]"
     * 输出: false
     *
     * 示例 4:
     *
     * 输入: "([)]"
     * 输出: false
     *
     * 示例 5:
     *
     * 输入: "{[]}"
     * 输出: true
     *
     * @param s str
     * @return boolean
     */
    public static boolean isValid(String s) {
        Stack<String> stack = new Stack<>();
        Map<String, String> blockMap = getBlockMap();
        if (s == null || s.isEmpty()) {
            return true;
        }
        for (int i = 0; i < s.length(); i++) {
            String item = String.valueOf(s.charAt(i));
            if (blockMap.containsKey(item)) {
                String topElement = stack.empty() ? "#" : stack.pop();
                if (!topElement.equals(blockMap.get(item))) {
                    return false;
                }
            } else {
                stack.push(item);
            }
        }
        return stack.isEmpty();
    }

    private static Map<String, String> getBlockMap() {
        Map<String, String> blockMap = new HashMap<>(3);
        blockMap.put(")", "(");
        blockMap.put("]", "[");
        blockMap.put("}", "{");
        return blockMap;
    }

    public static void main(String[] args) {
        System.out.println(isValid(""));
    }
}
