package com.timwang.reflection;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author wangjun
 * @date 2020-04-06
 */
public class CglibProxy {
    static class UserDao {
        public void save() {
            System.out.println("保存数据");
        }
    }

    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        System.out.println(userDao.getClass());
        UserDao proxy = (UserDao) new ProxyFactory(userDao).getInstance();
        System.out.println(proxy.getClass());
        proxy.save();
    }

    static class ProxyFactory implements MethodInterceptor {
        /**
         * 维护一个目标对象
         */
        private Object target;

        public Object getInstance() {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(target.getClass());
            enhancer.setCallback(this);
            return enhancer.create();
        }

        public ProxyFactory(Object target) {
            this.target = target;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            System.out.println("开启事务");
            method.invoke(target, objects);
            System.out.println("关闭事务");
            return null;
        }
    }
}
