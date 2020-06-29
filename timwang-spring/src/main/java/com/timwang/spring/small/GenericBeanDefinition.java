package com.timwang.spring.small;

/**
 * @author wangjun
 * @date 2020-06-29
 */
public class GenericBeanDefinition implements BeanDefinition {
    private String id;
    private String beanClassName;

    public GenericBeanDefinition(String id, String beanClassName) {
        this.id = id;
        this.beanClassName = beanClassName;
    }

    @Override
    public String getBeanClassName() {
        return this.beanClassName;
    }

    public String getId() {
        return id;
    }
}
