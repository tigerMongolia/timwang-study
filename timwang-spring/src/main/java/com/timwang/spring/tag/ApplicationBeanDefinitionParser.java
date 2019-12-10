package com.timwang.spring.tag;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * @author wangjun
 * @date 2018/12/8
 */
public class ApplicationBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    private final Class<?> beanClass;

    private final boolean required;

    public ApplicationBeanDefinitionParser(Class<?> beanClass, boolean required) {
        this.beanClass = beanClass;
        this.required = required;
    }

    protected Class getBeanClass(Element element) {
        return Application.class;
    }

    protected void doParse(Element element, BeanDefinitionBuilder bean) {
        String id = element.getAttribute("id");
        String name = element.getAttribute("name");
        String version = element.getAttribute("version");
        if (StringUtils.hasText(id)) {
            bean.addPropertyValue("id", id);
        }
        if (StringUtils.hasText(name)) {
            bean.addPropertyValue("name", name);
        }
        if (StringUtils.hasText(version)) {
            bean.addPropertyValue("version", version);
        }
    }
}
