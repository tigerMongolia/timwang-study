package com.timwang.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 被标注为'@CostTime'注解的方法执行耗时的代理方法.
 * <p>实现了cglib中的`MethodInterceptor`的方法拦截接口.</p>
 *
 * @author tim.wang
 */
public class CostTimeProxy implements MethodInterceptor {

    private static final Logger log = LoggerFactory.getLogger(CostTimeProxy.class);

    private Enhancer enhancer = new Enhancer();

    /**
     * 获取代理类.
     *
     * @param cls 代理类的class
     * @return 代理类实例
     */
    public Object getProxy(Class cls) {
        enhancer.setSuperclass(cls);
        enhancer.setCallback(this);
        return enhancer.create();
    }

    /**
     * 拦截方法,判断是否有'@CostTime'的注解，如果有则拦截执行.
     *
     * @param o 对象
     * @param method 方法
     * @param args 参数
     * @param methodProxy 代理方法
     * @return 对象
     * @throws Throwable 问题
     */
    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        // 判断该方法上是否有 CostTime 注解
        if (!method.isAnnotationPresent(CostTime.class)) {
            return methodProxy.invokeSuper(o, args);
        }
        // 获取注解信息
        CostTime costTime = method.getAnnotation(CostTime.class);
        long limitTime = costTime.value();

        // 记录方法执行前后的耗时时间，并做差，判断是否需要打印方法执行耗时
        long startTime = System.currentTimeMillis();
        Object result = methodProxy.invokeSuper(o, args);
        long diffTime = System.currentTimeMillis() - startTime;
        if (limitTime <= 0 || (diffTime >= limitTime)) {
            String methodName = method.getName();
            // 打印耗时的信息
            log.warn("【CostTime监控】通过注解监控方法'{}'的执行耗时为:{}", methodName, diffTime);
        }
        return result;
    }

}