package com.timwang.pattern.core.pattern.b_strategy;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author wangjun
 * @date 2019/6/2
 */
public class MainApp {
    public static void main(String[] args) {
        Context context = new Context(new RandomAssignStrategy());
        System.out.println("random assign result is : " + context.executeStrategy(getParams()));
        context = new Context(new ScoreAssignStrategy());
        System.out.println("score assign result is : " + context.executeStrategy(getParams()));
    }

    private static List<ProjectUserScore> getParams() {
        ProjectUserScore one = new ProjectUserScore();
        one.setUserId(47);
        one.setScore(30);
        ProjectUserScore two = new ProjectUserScore();
        two.setUserId(51);
        two.setScore(20);
        ProjectUserScore three = new ProjectUserScore();
        three.setUserId(63);
        three.setScore(100);
        return Lists.newArrayList(one, two, three);
    }
}
