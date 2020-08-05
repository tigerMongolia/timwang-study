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

###### 1.2.6 对命名空间的一点补充

在之前版本的 MyBatis 中，**命名空间（Namespaces）**的作用并不大，是可选的。 但现在，随着命名空间越发重要，你必须指定命名空间。

命名空间的作用有两个，一个是利用更长的全限定名来将不同的语句隔离开来，同时也实现了你上面见到的接口绑定。就算你觉得暂时用不到接口绑定，你也应该遵循这里的规定，以防哪天你改变了主意。 长远来看，只要将命名空间置于合适的 Java 包命名空间之中，你的代码会变得更加整洁，也有利于你更方便地使用 MyBatis。

**命名解析：**为了减少输入量，MyBatis 对所有具有名称的配置元素（包括语句，结果映射，缓存等）使用了如下的命名解析规则。

- 全限定名（比如 “com.mypackage.MyMapper.selectAllThings）将被直接用于查找及使用。
- 短名称（比如 “selectAllThings”）如果全局唯一也可以作为一个单独的引用。 如果不唯一，有两个或两个以上的相同名称（比如 “com.foo.selectAllThings” 和 “com.bar.selectAllThings”），那么使用时就会产生“短名称不唯一”的错误，这种情况下就必须使用全限定名。

##### 1.3 作用域（Scope）和生命周期

依赖注入框架可以创建线程安全的、基于事务的 SqlSession 和映射器，并将它们直接注入到你的 bean 中，因此可以直接忽略它们的生命周期。

###### 1.3.1 SqlSessionFactoryBuilder

这个类可以被实例化、使用和丢弃，一旦创建了 SqlSessionFactory，就不再需要它了。 因此 SqlSessionFactoryBuilder 实例的最佳作用域是方法作用域（也就是局部方法变量）。 你可以重用 SqlSessionFactoryBuilder 来创建多个 SqlSessionFactory 实例，但最好还是不要一直保留着它，以保证所有的 XML 解析资源可以被释放给更重要的事情。

###### 1.3.2 SqlSessionFactory

SqlSessionFactory 一旦被创建就应该在应用的运行期间一直存在，没有任何理由丢弃它或重新创建另一个实例。 使用 SqlSessionFactory 的最佳实践是在应用运行期间不要重复创建多次，多次重建 SqlSessionFactory 被视为一种代码“坏习惯”。因此 SqlSessionFactory 的最佳作用域是应用作用域。 有很多方法可以做到，最简单的就是使用单例模式或者静态单例模式。

###### 1.3.3 SqlSession

每个线程都应该有它自己的 SqlSession 实例。SqlSession 的实例不是线程安全的，因此是不能被共享的，所以它的最佳的作用域是请求或方法作用域。 绝对不能将 SqlSession 实例的引用放在一个类的静态域，甚至一个类的实例变量也不行。 也绝不能将 SqlSession 实例的引用放在任何类型的托管作用域中，比如 Servlet 框架中的 HttpSession。 如果你现在正在使用一种 Web 框架，考虑将 SqlSession 放在一个和 HTTP 请求相似的作用域中。 换句话说，每次收到 HTTP 请求，就可以打开一个 SqlSession，返回一个响应后，就关闭它。 这个关闭操作很重要，为了确保每次都能执行关闭操作，你应该把这个关闭操作放到 finally 块中。 下面的示例就是一个确保 SqlSession 关闭的标准模式：

```java
try (SqlSession session = sqlSessionFactory.openSession()) {
  // 你的应用逻辑代码
}
```

###### 1.3.4 映射器实例

映射器是一些绑定映射语句的接口。映射器接口的实例是从 SqlSession 中获得的。虽然从技术层面上来讲，任何映射器实例的最大作用域与请求它们的 SqlSession 相同。但方法作用域才是映射器实例的最合适的作用域。 也就是说，映射器实例应该在调用它们的方法中被获取，使用完毕之后即可丢弃。 映射器实例并不需要被显式地关闭。尽管在整个请求作用域保留映射器实例不会有什么问题，但是你很快会发现，在这个作用域上管理太多像 SqlSession 的资源会让你忙不过来。 因此，最好将映射器放在方法作用域内。就像下面的例子一样：

```java
try (SqlSession session = sqlSessionFactory.openSession()) {
  BlogMapper mapper = session.getMapper(BlogMapper.class);
  // 你的应用逻辑代码
}
```

##### 1.4 MyBatis-Spring

###### 1.4.1 MyBatis-Spring应用

我们在接入mybatis-spring的时候会在相应的配置类增加这样的注解

```java
@MapperScan(basePackages = "com.test.**.mapper")
```

用MyBatis-Spring会将MyBatis整合到Spring当中，我这里用的版本Spring 5.2.1，MyBatis  3.5.3，MyBatis-Spring 2.0.3，满足官方的要求，用的注解而不是XML

具体的使用，需要配置两样东西，一个是SqlSessionFactory，用来创建SqlSession的Factory，SqlSession用来对接数据库；另一个是数据映射器Mapper接口

1、在MyBatis-Spring中，可以通过SqlSessionFactoryBean来创建SqlSessionFactory，这里还需要一个DataSource，和JDBC等其它DataSource一样即可

```java
@Bean
public SqlSessionFactory sqlSessionFactory() throws Exception {
    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
    factoryBean.setDataSource(dataSource());
    return factoryBean.getObject();
}
```

大致流程SqlSessionFactoryBean->getObject()->afterPropertiesSet()->buildSqlSessionFactory()->Configuration->SqlSessionFactory

2、映射器是一个接口，定义一个Mapper接口，一个简单的查询方法，@Select指定具体的SQL

```java
public interface UserMapper {
    
    @Select("select * from user_info where id = #{id}")
    User getUserById(@Param("id") int id);
}
```

3、对应ORM实体类

```java
public class User {
    
    private int id;
    private String name;
    private String phone;
    private int age;
    
    @Override
    public String toString() {
        return "id=" + id + ", name=" + name + ", phone=" + phone + ", age=" + age;
    }
}
```

4、映射器的注册，通过MapperFactoryBean可以将映射器注册到Spring

```java
import com.lihuia.mybatis.mapper.UserMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Copyright (C), 2018-2019
 * FileName: MyBatisConfig
 * Author:   lihui
 * Date:     2019/11/15
 */

@PropertySource(value = {"classpath:config/db.properties"})
@Configuration
public class MyBatisConfig {
  	@Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;
    
    @Bean
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource());
        return factoryBean.getObject();
    }
    
  	@Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
    
    @Bean
    public MapperFactoryBean<UserMapper> userMapper() throws Exception {
        MapperFactoryBean<UserMapper> factoryBean = new MapperFactoryBean<>(UserMapper.class);
        factoryBean.setSqlSessionFactory(sqlSessionFactory());
        return factoryBean;
    }

}
```

5、测试类

```java
@ContextConfiguration(classes = {MyBatisConfig.class})
@Slf4j
public class UserTest extends AbstractTestNGSpringContextTests {

    @Resource
    private JdbcTemplate jdbcTemplate;
    
    @Resource
    private UserMapper userMapper;
    
    
    @Test(description = "测试JDBC")
    public void jdbcTest() {
        String sql = "select * from user_info";
        System.out.println(jdbcTemplate.queryForList(sql));
    }
    
    @Test(description = "测试MyBatis")
    public void myBatisTest() {
        log.info(userMapper.getUserById(1).toString());
    }
}
```

如果是直接通过注入Bean的方式注入UserMapper，那么假如有一大堆的映射器，一个一个的注册注入十分麻烦，因此就和@ComponentScan注解一样有一个扫描映射器的注解@MapperScan，大致如下

```java
@PropertySource(value = {"classpath:config/db.properties"})
@Configuration
@MapperScan("com.lihuia.mybatis.mapper")
public class MyBatisConfig {
```

###### 1.4.2  @MapperScan注解

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(MapperScannerRegistrar.class)
@Repeatable(MapperScans.class)
public @interface MapperScan {
```

里面的@Import注解，可以导入一个类，定义如下

```java
/**
 * A {@link ImportBeanDefinitionRegistrar} to allow annotation configuration of MyBatis mapper scanning. Using
 * an @Enable annotation allows beans to be registered via @Component configuration, whereas implementing
 * {@code BeanDefinitionRegistryPostProcessor} will work for XML configuration.
 *
 * @author Michael Lanyon
 * @author Eduardo Macarron
 * @author Putthiphong Boonphong
 *
 * @see MapperFactoryBean
 * @see ClassPathMapperScanner
 * @since 1.2.0
 */
public class MapperScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
```

根据注释里可以看到ClassPathMapperScanner，继承了ClassPathBeanDefinitionScanner类是Spring提供的一个用于扫描Bean定义配置的基础类，这里覆盖了基类的doScan()方法

```java
/**
 * Calls the parent search that will search and register all the candidates. Then the registered objects are post
 * processed to set them as MapperFactoryBeans
 */
@Override
public Set<BeanDefinitionHolder> doScan(String... basePackages) {
  Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

  if (beanDefinitions.isEmpty()) {
    LOGGER.warn(() -> "No MyBatis mapper was found in '" + Arrays.toString(basePackages)
        + "' package. Please check your configuration.");
  } else {
    processBeanDefinitions(beanDefinitions);
  }

  return beanDefinitions;
}
```

DEBUG一下，可以看到这个就是扫描Mapper接口的方法，返回@MapperScan注解的value

![](https://tva1.sinaimg.cn/large/007S8ZIlgy1ghcvb0zigoj318e0u0gpy.jpg)

#### 二、参考链接

https://juejin.im/post/5d8e06b06fb9a04e1c07d87b

https://mybatis.org/mybatis-3/zh/getting-started.html

http://lihuia.com/mybatis/

https://juejin.im/post/5db3bce6e51d452a36791ef0#heading-0

https://my.oschina.net/xiaolyuh/blog/3134267

https://www1350.github.io/hexo/post/4df53b51.html

[https://cofcool.github.io/tech/2018/06/20/mybatis-sourcecode-1#21-%E9%85%8D%E7%BD%AE%E7%B1%BB](https://cofcool.github.io/tech/2018/06/20/mybatis-sourcecode-1#21-配置类)

https://objcoding.com/2018/06/12/mybatis-spring/

[http://www.songshuiyang.com/2018/12/18/backend/framework/mybatis/sourceCodeAnalysis/Mybatis%E6%BA%90%E7%A0%81(%E5%8D%81%E4%B9%9D)Spring%20Mybatis%E9%9B%86%E6%88%90%E4%B9%8B%E5%9F%BA%E4%BA%8E%E6%B3%A8%E8%A7%A3%E7%9A%84%E9%85%8D%E7%BD%AE%E5%8E%9F%E7%90%86%E8%A7%A3%E6%9E%90/](http://www.songshuiyang.com/2018/12/18/backend/framework/mybatis/sourceCodeAnalysis/Mybatis源码(十九)Spring Mybatis集成之基于注解的配置原理解析/)