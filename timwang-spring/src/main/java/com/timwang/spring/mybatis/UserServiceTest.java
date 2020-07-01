package com.timwang.spring.mybatis;

import com.timwang.spring.mybatis.AppConfig;
import com.timwang.spring.mybatis.dao.UserMapper;
import com.timwang.spring.mybatis.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author wangjun
 * @date 2020-06-30
 */
public class UserServiceTest {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

        Object mybatisFactoryBean = applicationContext.getBean("mybatisFactoryBean");
        System.out.println(mybatisFactoryBean);

        UserService userService = applicationContext.getBean("userService", UserService.class);
        System.out.println(userService);
        userService.test();
    }
}
