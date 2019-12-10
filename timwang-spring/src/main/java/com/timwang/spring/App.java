package com.timwang.spring;

import com.timwang.spring.tag.Application;
import com.timwang.spring.tag.Registry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author wangjun
 * @date 2018/12/8
 */
public class App {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Application application1 = (Application) context.getBean("app1");
        System.out.println(application1.toString());
        Registry registry1 =  (Registry) context.getBean("reg1");
        System.out.println(registry1.toString());
        System.out.println("Hello Ttdubbo!");
    }
}
