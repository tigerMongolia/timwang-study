package com.timwang.leetcode.core.stack;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author wangjun
 * @date 2019-10-29
 */
public class APractice1 {
    /**
     * 有效括号字符串为空 ("")、"(" + A + ")" 或 A + B，其中 A 和 B 都是有效的括号字符串，+ 代表字符串的连接。例如，""，"()"，"(())()" 和 "(()(()))" 都是有效的括号字符串。
     * 如果有效字符串 S 非空，且不存在将其拆分为 S = A+B 的方法，我们称其为原语（primitive），其中 A 和 B 都是非空有效括号字符串。
     * 给出一个非空有效字符串 S，考虑将其进行原语化分解，使得：S = P_1 + P_2 + ... + P_k，其中 P_i 是有效括号字符串原语。
     * 对 S 进行原语化分解，删除分解中每个原语字符串的最外层括号，返回 S 。
     *
     * 示例 1：
     * 输入："(()())(())"
     * 输出："()()()"
     * 解释：
     * 输入字符串为 "(()())(())"，原语化分解得到 "(()())" + "(())"，
     * 删除每个部分中的最外层括号后得到 "()()" + "()" = "()()()"。
     *
     *
     *  示例 2：
     * 输入："(()())(())(()(()))"
     * 输出："()()()()(())"
     * 解释：
     * 输入字符串为 "(()())(())(()(()))"，原语化分解得到 "(()())" + "(())" + "(()(()))"，
     * 删除每隔部分中的最外层括号后得到 "()()" + "()" + "()(())" = "()()()()(())"。
     *
     *
     * 示例 3：
     *
     * 输入："()()"
     * 输出：""
     * 解释：
     * 输入字符串为 "()()"，原语化分解得到 "()" + "()"，
     * 删除每个部分中的最外层括号后得到 "" + "" = ""。
     *
     * 提示：
     *
     * S.length <= 10000
     * S[i] 为 "(" 或 ")"
     * S 是一个有效括号字符串
     *
     * @see https://leetcode-cn.com/problems/remove-outermost-parentheses/
     */
    public String removeOuterParentheses(String str) {
        Stack<String> stack = new Stack<>();
        StringBuilder result = new StringBuilder();
        int startNum = 0;
        for (int i = startNum ; i < str.length(); i++) {
            String one = String.valueOf(str.charAt(i));
            if ("(".equals(one)) {
                stack.push(one);
            }
            if (")".equals(one)) {
                stack.pop();
            }
            if (stack.empty()) {
                result.append(str, startNum + 1, i);
                startNum = i + 1;
            }
        }
        return result.toString();
    }

}
