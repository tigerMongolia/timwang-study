package com.timwang.algorithm.interview.hypers;

import java.io.Serializable;

/**
 * @author wangjun
 * @date 2020-07-02
 */
public class FindResult implements Serializable {
    private Integer minIndex;
    private Integer maxIndex;
    private Integer subMax;

    public Integer getMinIndex() {
        return minIndex;
    }

    public void setMinIndex(Integer minIndex) {
        this.minIndex = minIndex;
    }

    public Integer getMaxIndex() {
        return maxIndex;
    }

    public void setMaxIndex(Integer maxIndex) {
        this.maxIndex = maxIndex;
    }

    public Integer getSubMax() {
        return subMax;
    }

    public void setSubMax(Integer subMax) {
        this.subMax = subMax;
    }
}
