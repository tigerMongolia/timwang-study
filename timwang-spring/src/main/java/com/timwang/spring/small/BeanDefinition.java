package com.timwang.spring.small;

/**
 * @author wangjun
 * @date 2020-06-29
 */
public interface BeanDefinition {
    /**
     * 获取bean.xml中 bean的全名 如 "com.niocoder.service.v1.NioCoderService"
     * @return string
     */
    String getBeanClassName();
}
