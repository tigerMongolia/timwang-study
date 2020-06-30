package com.timwang.spring.mybatis.util;

import com.timwang.spring.mybatis.dao.UserMapper;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author wangjun
 * @date 2020-06-30
 */
@Component
public class MybatisFactoryBean implements FactoryBean {

    @Override
    public Object getObject() throws Exception {
        UserMapper userMapper = (UserMapper) Proxy.newProxyInstance(
            MybatisFactoryBean.class.getClassLoader(),
            new Class[]{UserMapper.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 动态代理返回toString这些方法的默认
                if (Object.class.equals(method.getDeclaringClass())) {
                    return method.invoke(this, args);
                }
                System.out.println("=====");
                return null;
            }
        });
        return userMapper;
    }

    @Override
    public Class<?> getObjectType() {
        return UserMapper.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
