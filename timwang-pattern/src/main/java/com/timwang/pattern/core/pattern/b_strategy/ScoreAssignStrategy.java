package com.timwang.pattern.core.pattern.b_strategy;

import java.util.Comparator;
import java.util.List;

/**
 * @author wangjun
 * @date 2019/6/2
 */
public class ScoreAssignStrategy implements AssignStrategy {

    @Override
    public Integer assign(List<ProjectUserScore> userScoreList) {
        userScoreList.sort(Comparator.comparing(ProjectUserScore::getScore).reversed());
        return userScoreList.get(0).getUserId();
    }
}
