> 们在启动项目的时候，spring就会帮我们实例化好bean，那我们使用mybatis-spring的时候，编 写的maper是怎么交给spring容器的呢？这就是今天要探讨的问题。

#### 一、MyBatis

##### 1.1 MyBatis简介

MyBatis 是一款优秀的持久层框架，它支持自定义 SQL、存储过程以及高级映射。MyBatis 免除了几乎所有的 JDBC 代码以及设置参数和获取结果集的工作。MyBatis 可以通过简单的 XML 或注解来配置和映射原始类型、接口和 Java POJO（Plain Old Java Objects，普通老式 Java 对象）为数据库中的记录。

##### 1.2 MyBatis使用

###### 1.2.1 安装

###### 1.2.2 从 XML 中构建 SqlSessionFactory

###### 1.2.3 不使用 XML 构建 SqlSessionFactory

###### 1.2.4 从 SqlSessionFactory 中获取 SqlSession

###### 1.2.5 探究已映射的 SQL 语句

##### 1.3 MyBatis-Spring





#### 二、扫描阶段

我们在接入mybatis-spring的时候会在相应的配置类增加这样的注解

```java
@MapperScan(basePackages = "com.test.**.mapper")
```





https://mybatis.org/mybatis-3/zh/getting-started.html

http://lihuia.com/mybatis/

http://lihuia.com/mybatis/

https://juejin.im/post/5db3bce6e51d452a36791ef0#heading-0

https://my.oschina.net/xiaolyuh/blog/3134267

https://www1350.github.io/hexo/post/4df53b51.html