package com.timwang.spring.tag;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author wangjun
 * @date 2018/12/8
 */
public class TtdubboNamespaceHandler extends NamespaceHandlerSupport {
    public void init() {
        registerBeanDefinitionParser("application", new ApplicationBeanDefinitionParser(Application.class, true));
        registerBeanDefinitionParser("registry", new RegistryBeanDefinitionParser());
    }

}