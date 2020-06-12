package com.timwang.interview.hypers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangjun
 * @date 2020-03-02
 */
public class FirstOne {

    /**
      * 给定任意一个词的序列，如: n-gram models are widely used in natural language processing.
      * 1-gram输出为：{n-gram, models, are, widely, used, in, natural, language, processing}
      * 2-gram输出为：{(n-gram, models), (models, are), (are, widely), (widely, used), (used, in) ... }
      * 3-gram输出为：{(n-gram, models, are), (models, are, widely), (are, widely, used) ... }
      * 其他的N-gram的输出以此类推，请完成N-gram输出的实现算法：
      * seq为输入序列，且默认输入满足以下条件
      * 1. seq.size()>= 1
      * 2. N >=1 且 N <= seq.size()
      */
    public static void  ngram(List<String> seq,int N) {
        int times = seq.size() / N;
        int left = seq.size() % N;
        List<String> result = new ArrayList<>(times + 1);
        for (int i = 0; i < times; i++) {
            List<String> itemResults = new ArrayList<>(N);
            for (int j = 0; j < N; j++) {
                itemResults.add(seq.get(i + j));
            }
            result.add(convertTOStr(itemResults, "(", ")"));
        }
        if (left == 0) {
            System.out.println(convertTOStr(result, "{", "}"));
            return;
        }
        List<String> leftStrings = seq.subList(seq.size() - left, seq.size());
        result.add(convertTOStr(leftStrings, "(", ")"));
        System.out.println(convertTOStr(result, "{", "}"));
    }

    private static String convertTOStr(List<String> strList, String left, String right) {
        if (strList == null || strList.size() == 0) {
            return "";
        }
        StringBuilder result = new StringBuilder(left);
        for (int i = 0; i < strList.size(); i++) {
            if (i == strList.size() - 1) {
                result.append(strList.get(i));
                continue;
            }
            result.append(strList.get(i)).append(", ");
        }
        result.append(right);
        return result.toString();
    }

    public static void main(String[] args) {
        String[] res = new String[]{"n-gram", "models", "are", "widely", "used", "in", "natural", "language", "processing"};
        List<String> items = Arrays.asList(res);
        ngram(items, 2);
        ngram(items, 1);
        ngram(items, 3);
        ngram(items, 5);
        ngram(items, 9);
    }
}
