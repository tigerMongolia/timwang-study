package com.timwang.annotation;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author wangjun
 * @date 2019-12-13
 */

@StandardResultRestControllerApi(value="/Combination",tags = "自定义组合注解", description = "组合注解优化代码")
public class CombinationController {
}
