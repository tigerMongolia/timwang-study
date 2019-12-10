package com.timwang.spring.tag;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * @author wangjun
 * @date 2018/12/8
 */
public class RegistryBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class getBeanClass(Element element) {
        return Registry.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder bean) {
        String id = element.getAttribute("id");
        String address = element.getAttribute("address");
        String port = element.getAttribute("port");
        if (StringUtils.hasText(id)) {
            bean.addPropertyValue("id", id);
        }
        if (StringUtils.hasText(address)) {
            bean.addPropertyValue("address", address);
        }
        if (StringUtils.hasText(port)) {
            bean.addPropertyValue("port", Integer.valueOf(port));
        }
    }
}

