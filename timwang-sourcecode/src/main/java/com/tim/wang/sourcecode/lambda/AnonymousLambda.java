package com.tim.wang.sourcecode.lambda;

import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;

/**
 * @author wangjun
 * @date 2020-07-25
 */
public class AnonymousLambda {
    public static void noArgsLambda() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("thread run");
            }
        }).start();

        new Thread(() -> System.out.println("thread run")).start();
        new Thread(() -> {
            System.out.println("thread run 1");
            System.out.println("thread run 2");
        }).start();
    }

    public static void hasArgsLambda() {
        List<String> strings = Arrays.asList("I", "Love", "You");
        Collections.sort(strings, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return 0;
            }
        });

        Collections.sort(strings, (s1, s2) -> {
            return 0;
        });
    }

    public static void lambda() {
        Runnable runnable = () -> System.out.println("---");
        ActionListener listener = event -> System.out.println("button clicked");
        BinaryOperator<Long> add = (Long x, Long y) -> x + y;
        BinaryOperator<Long> add2 = (x, y) -> x + y;
    }
}
