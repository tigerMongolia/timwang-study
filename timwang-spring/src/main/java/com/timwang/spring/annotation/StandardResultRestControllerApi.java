package com.timwang.spring.annotation;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wangjun
 * @date 2019-12-13
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller()
@ResponseBody
@RequestMapping
@Api()
public @interface StandardResultRestControllerApi {
    /**
     * 定义映射路径URL
     */
    @AliasFor(annotation = RequestMapping.class, value = "path")
    String[] value() default {};

    /**
     *定义spring类名称
     */
    @AliasFor(annotation = Controller.class, value = "value")
    String name() default "";
    /**
     *定义Api类tags属性
     */
    @AliasFor(annotation = Api.class, attribute = "tags")
    String[] tags() default "";

    /**
     *定义Api类description属性
     */
    @AliasFor(annotation = Api.class, attribute = "description")
    String description() default "";
}
