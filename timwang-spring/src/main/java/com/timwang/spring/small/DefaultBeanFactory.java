package com.timwang.spring.small;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.util.ClassUtils;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.dom4j.io.SAXReader;

/**
 * BeanFactory的默认实现类
 *
 * @author wangjun
 * @date 2020-06-29
 */
public class DefaultBeanFactory implements BeanFactory {
    private static final String ID_ATTRIBUTE = "id";
    private static final String CLASS_ATTRIBUTE = "class";
    /**
     * 存放BeanDefinition
     */
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    public DefaultBeanFactory(String configFile) {
        loadBeanDefinition(configFile);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanId) {
        return beanDefinitionMap.get(beanId);
    }

    @Override
    public Object getBean(String beanId) {
        BeanDefinition bd = this.getBeanDefinition(beanId);
        if (bd == null) {
            throw new BeanCreationException("BeanDefinition does not exist");
        }
        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        String beanClassName =  bd.getBeanClassName();
        try {
            // 使用反射创建bean的实例，需要对象存在默认的无参构造方法
            Class<?> aClass = classLoader.loadClass(beanClassName);
            return aClass.newInstance();
        } catch (Exception e) {
            throw new BeanCreationException("Bean Definition does not exist");
        }
    }

    /**
     * 具体解析bean.xml的方法 使用dom4j
     * @param configFile configFile
     */
    private void loadBeanDefinition(String configFile) {
        ClassLoader cl = ClassUtils.getDefaultClassLoader();
        try (InputStream is = cl.getResourceAsStream(configFile)) {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(is);

            Element root = doc.getRootElement();
            Iterator elementIterator = root.elementIterator();
            while (elementIterator.hasNext()) {
                Element ele = (Element) elementIterator.next();
                String id = ele.attributeValue(ID_ATTRIBUTE);
                String beanClassName = ele.attributeValue(CLASS_ATTRIBUTE);
                BeanDefinition bd = new GenericBeanDefinition(id, beanClassName);
                this.beanDefinitionMap.put(id, bd);
            }
        } catch (Exception e) {
            throw new BeanDefinitionStoreException("IOException parsing XML document", e);
        }
    }
}
