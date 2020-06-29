package com.timwang.spring.small;

/**
 * @author wangjun
 * @date 2020-06-29
 */
public class BeanFactoryTest {
    public static void main(String[] args) {
        BeanFactoryTest beanFactoryTest = new BeanFactoryTest();
        beanFactoryTest.testGetBean();
    }

    public void testGetBean() {
        BeanFactory beanFactory = new DefaultBeanFactory("bean-v1.xml");
        BeanDefinition bd = beanFactory.getBeanDefinition("user");
        String beanClassName = bd.getBeanClassName();
        System.out.println(beanClassName);
    }
}
