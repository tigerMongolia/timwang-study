package com.timwang.spring.small;

/**
 * 创建bean的实例
 * @author wangjun
 * @date 2020-06-29
 */
public interface BeanFactory {
    /**
     * 获取bean的定义
     * @param beanId beanId
     * @return BeanDefinition
     */
    BeanDefinition getBeanDefinition(String beanId);

    /**
     * 获取bean的实例
     * @param beanId beanId
     * @return bean示例
     */
    Object getBean(String beanId);
}
