package com.timwang.spring.mybatis.service;

import com.timwang.spring.mybatis.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wangjun
 * @date 2020-06-30
 */
@Component
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void test() {
        userMapper.selectById(1);
    }
}
