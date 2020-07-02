package com.timwang.algorithm.interview.hypers;

import com.mongodb.util.JSON;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangjun
 * @date 2020-07-02
 */
public class FindDuringMax {
    public static FindResult findMaxResult(List<Integer> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        Integer max = Integer.MIN_VALUE;
        Integer minI = 0;
        Integer maxI = 0;
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(j) - list.get(i) > max) {
                    max = list.get(j) - list.get(i);
                    minI = i;
                    maxI = j;
                }
            }
        }
        FindResult result = new FindResult();
        result.setMinIndex(minI);
        result.setMaxIndex(maxI);
        result.setSubMax(max);
        return result;
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1); list.add(4);
        list.add(30); list.add(2); list.add(40);
        FindResult maxResult = findMaxResult(list);
        System.out.println(maxResult.getSubMax());
        System.out.println(maxResult.getMinIndex());
        System.out.println(maxResult.getMaxIndex());
    }
}
