package com.timwang.pattern.core.pattern.b_strategy;

import java.util.List;

/**
 * @author wangjun
 * @date 2019/6/2
 */
public interface AssignStrategy {

    Integer assign(List<ProjectUserScore> userScoreList);

}
