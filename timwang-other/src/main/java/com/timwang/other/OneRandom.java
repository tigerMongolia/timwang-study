package com.timwang.other;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomUtils;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author wangjun
 * @date 2020-03-13
 */
public class OneRandom {

    public static void main(String[] args) {

        List<Integer> result = Lists.newArrayList(1,2,3,4,5);
        Collections.shuffle(result);
        System.out.println(result);

        List<Integer> random = random(10);
        System.out.println(random);
    }

    public static List<Integer> random(int n) {
        List<Integer> result = Lists.newArrayList();

        for (int i = 1; i <= n; i++) {
            result.add(randomOne(result, n));
        }
        return result;
    }


    public static int randomOne(List<Integer> list, int n) {
        int random = new Random().nextInt(n) + 1;
        if (list.contains(random)) {
            return randomOne(list, n);
        }
        return random;

    }
}
