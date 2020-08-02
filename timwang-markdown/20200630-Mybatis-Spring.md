> 我们在启动项目的时候，spring就会帮我们实例化好bean，那我们使用mybatis-spring的时候，编 写的maper是怎么交给spring容器的呢？这就是今天要探讨的问题。

#### 一、MyBatis

##### 1.1 MyBatis简介

MyBatis 是一款优秀的持久层框架，它支持自定义 SQL、存储过程以及高级映射。MyBatis 免除了几乎所有的 JDBC 代码以及设置参数和获取结果集的工作。MyBatis 可以通过简单的 XML 或注解来配置和映射原始类型、接口和 Java POJO（Plain Old Java Objects，普通老式 Java 对象）为数据库中的记录。

![](https://tva1.sinaimg.cn/large/007S8ZIlgy1ghb3jzny8qj31m60u0juf.jpg)

##### 1.2 MyBatis使用

###### 1.2.1 安装

```xml
<dependency>
  <groupId>org.mybatis</groupId>
  <artifactId>mybatis</artifactId>
  <version>x.x.x</version>
</dependency>
```

###### 1.2.2 从 XML 中构建 SqlSessionFactory

​		每个基于 MyBatis 的应用都是以一个 SqlSessionFactory 的实例为核心的。SqlSessionFactory 的实例可以通过 SqlSessionFactoryBuilder 获得。而 SqlSessionFactoryBuilder 则可以从 XML 配置文件或一个预先配置的 Configuration 实例来构建出 SqlSessionFactory 实例。

​		从 XML 文件中构建 SqlSessionFactory 的实例非常简单，建议使用类路径下的资源文件进行配置。 但也可以使用任意的输入流（InputStream）实例，比如用文件路径字符串或 file:// URL 构造的输入流。MyBatis 包含一个名叫 Resources 的工具类，它包含一些实用方法，使得从类路径或其它位置加载资源文件更加容易。

```java
/**
 * @author wangjun
 * @date 2020-08-01
 */
public class SqlSessionFactoryWithXml {
    public static void main(String[] args) {
        String resource = "mybatis-config.xml";
        ClassLoader defaultClassLoader = ClassUtils.getDefaultClassLoader();
        if (defaultClassLoader == null) {
            return;
        }
        InputStream inputStream = defaultClassLoader.getResourceAsStream(resource);
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(inputStream);
        System.out.println(build);
    }
}
```

XML 配置文件中包含了对 MyBatis 系统的核心设置，包括获取数据库连接实例的数据源（DataSource）以及决定事务作用域和控制方式的事务管理器（TransactionManager）。后面会再探讨 XML 配置文件的详细内容，这里先给出一个简单的示例：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/zp"/>
                <property name="username" value="root"/>
                <property name="password" value="123456"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <mapper resource="mappers/NewsMapper.xml"/>
    </mappers>
</configuration>
```

###### 1.2.3 不使用 XML 构建 SqlSessionFactory

如果你更愿意直接从 Java 代码而不是 XML 文件中创建配置，或者想要创建你自己的配置建造器，MyBatis 也提供了完整的配置类，提供了所有与 XML 文件等价的配置项。

```java
/**
 * @author wangjun
 * @date 2020-08-01
 */
public class SqlSessionFactoryWithoutXml {
    public static void main(String[] args) {
        DataSource dataSource = DataSourceFactory.getDataSource();
        JdbcTransactionFactory jdbcTransactionFactory = new JdbcTransactionFactory();
        Environment development = new Environment("development", jdbcTransactionFactory, dataSource);
        Configuration configuration = new Configuration(development);
        configuration.addMapper(NewsMapper.class);
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(configuration);
        System.out.println(build);
    }
}
```

注意该例中，configuration 添加了一个映射器类（mapper class）。映射器类是 Java 类，它们包含 SQL 映射注解从而避免依赖 XML 文件。不过，由于 Java 注解的一些限制以及某些 MyBatis 映射的复杂性，要使用大多数高级映射（比如：嵌套联合映射），仍然需要使用 XML 配置。有鉴于此，如果存在一个同名 XML 配置文件，MyBatis 会自动查找并加载它

###### 1.2.4 从 SqlSessionFactory 中获取 SqlSession

既然有了 SqlSessionFactory，顾名思义，我们可以从中获得 SqlSession 的实例。SqlSession 提供了在数据库执行 SQL 命令所需的所有方法。你可以通过 SqlSession 实例来直接执行已映射的 SQL 语句。例如：

例如：

```java
try (SqlSession session = sqlSessionFactory.openSession()) {
  BlogMapper mapper = session.getMapper(BlogMapper.class);
  Blog blog = mapper.selectBlog(101);
}
```

###### 1.2.5 探究已映射的 SQL 语句

![](https://tva1.sinaimg.cn/large/007S8ZIlgy1ghb5es9v94j30hh0d4dfw.jpg)

在上面提到的例子中，一个语句既可以通过 XML 定义，也可以通过注解定义。我们先看看 XML 定义语句的方式，事实上 MyBatis 提供的所有特性都可以利用基于 XML 的映射语言来实现，这使得 MyBatis 在过去的数年间得以流行。如果你用过旧版本的 MyBatis，你应该对这个概念比较熟悉。 但相比于之前的版本，新版本改进了许多 XML 的配置，后面我们会提到这些改进。这里给出一个基于 XML 映射语句的示例，它应该可以满足上个示例中 SqlSession 的调用。

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.tim.wang.sourcecode.mybatis.spring.mapper.NewsMapper">

    <select id="selectIds" resultType="java.lang.Integer">
        SELECT ID FROM t_news LIMIT 0,1
    </select>

</mapper>
```



##### 1.3 MyBatis-Spring

#### 二、扫描阶段

我们在接入mybatis-spring的时候会在相应的配置类增加这样的注解

```java
@MapperScan(basePackages = "com.test.**.mapper")
```



https://juejin.im/post/5d8e06b06fb9a04e1c07d87b

https://mybatis.org/mybatis-3/zh/getting-started.html

http://lihuia.com/mybatis/

https://juejin.im/post/5db3bce6e51d452a36791ef0#heading-0

https://my.oschina.net/xiaolyuh/blog/3134267

https://www1350.github.io/hexo/post/4df53b51.html