package com.timwang.spring.tag;

/**
 * @author wangjun
 * @date 2018/12/8
 */
public class Application {
    private String id;
    private String name;
    private String version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Application [id=" + id + ", name=" + name + ", version=" + version + "]";
    }

}
