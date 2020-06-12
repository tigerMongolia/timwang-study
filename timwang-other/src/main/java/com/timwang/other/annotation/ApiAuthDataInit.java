package com.timwang.other.annotation;

import com.google.common.collect.Lists;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author wangjun
 * @date 2019-12-13
 */
@Component
@Configuration
public class ApiAuthDataInit implements ApplicationContextAware {
    public static List<String> checkApis = Lists.newArrayList();
    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        Map<String, Object> beanMap = ctx.getBeansWithAnnotation(Controller.class);
        if (beanMap != null) {
            for (Object bean : beanMap.values()) {
                Class<?> clz = bean.getClass();
                Method[] methods = clz.getMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(EnableAuth.class)) {
                        String uri = getApiUri(clz, method);
                        checkApis.add(uri);
                    }
                }
            }
        }
    }
    private String getApiUri(Class<?> clz, Method method) {
        StringBuilder uri = new StringBuilder();
        uri.append(clz.getAnnotation(RequestMapping.class).value()[0]);
        if (method.isAnnotationPresent(RequestMapping.class)) {
            uri.append(method.getAnnotation(RequestMapping.class).value()[0]);
        }
        return uri.toString();
    }
}
