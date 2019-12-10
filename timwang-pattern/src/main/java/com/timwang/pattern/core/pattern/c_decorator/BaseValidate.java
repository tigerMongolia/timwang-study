package com.timwang.pattern.core.pattern.c_decorator;

import java.util.List;

/**
 * @author wangjun
 * @date 2019/6/2
 */
public abstract class BaseValidate {
    private List<String> invalidInfoList;

    public abstract List<String> getErrorList();
}
