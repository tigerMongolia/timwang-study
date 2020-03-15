package com.timwang.other;

import com.google.common.collect.Lists;
import scala.Int;

import java.util.List;

/**
 * @author wangjun
 * @date 2020-03-13
 */
public class FindTargetNumber {

    public static void main(String[] args) {
        List<Integer> params = Lists.newArrayList(11,4,4,7,15);
        List<Integer> result = findResult(params, 14);
        System.out.println(result);
    }

    public static List<Integer> findResult(List<Integer> numbers, Integer target) {
        for (int i = 0; i < numbers.size(); i++) {
            for (int j = i + 1; j < numbers.size(); j++) {
                if (numbers.get(i) + numbers.get(j) == target) {
                    return Lists.newArrayList(i, j);
                }
            }
        }
        return null;
    }

}
