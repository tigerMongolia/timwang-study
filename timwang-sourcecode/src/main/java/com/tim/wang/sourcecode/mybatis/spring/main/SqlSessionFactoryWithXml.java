package com.tim.wang.sourcecode.mybatis.spring.main;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.util.ClassUtils;

import java.io.InputStream;

/**
 * @author wangjun
 * @date 2020-08-01
 */
public class SqlSessionFactoryWithXml {
    public static void main(String[] args) {
        String resource = "mybatis-config.xml";
        ClassLoader defaultClassLoader = ClassUtils.getDefaultClassLoader();
        if (defaultClassLoader == null) {
            return;
        }
        InputStream inputStream = defaultClassLoader.getResourceAsStream(resource);
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(inputStream);
        System.out.println(build);
    }
}
