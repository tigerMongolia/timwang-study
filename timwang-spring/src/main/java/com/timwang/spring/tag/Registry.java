package com.timwang.spring.tag;

/**
 * @author wangjun
 * @date 2018/12/8
 */
public class Registry {
    private String id;
    private String address;
    private Integer port;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public Integer getPort() {
        return port;
    }
    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "Registry [id=" + id + ", address=" + address + ", port=" + port + "]";
    }
}
