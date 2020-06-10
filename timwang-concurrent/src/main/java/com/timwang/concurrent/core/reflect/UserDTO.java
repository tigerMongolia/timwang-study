package com.timwang.concurrent.core.reflect;

import java.io.Serializable;

/**
 * @author wangjun
 * @date 2020-06-10
 */
public class UserDTO implements Serializable {
    private Integer id;
    private String account;

    public UserDTO(Integer id, String account) {
        this.id = id;
        this.account = account;
    }

    public UserDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
