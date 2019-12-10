package com.timwang.pattern.core.pattern.b_strategy;

import java.io.Serializable;

/**
 * @author wangjun
 * @date 2019/6/2
 */
public class ProjectUserScore implements Serializable {
    private Integer userId;
    private Integer score;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
