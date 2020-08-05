

简介:

1.   Spring Boot是由Pivotal团队提供的,   Spring家族中的一个全新的框架，它用来简化Spring应用程序的创建和开发过程，也可以说Spring boot能简化我们之前采用SpringMVC+Spring+Mybatis框架进行开发的过程。

2.   在以往我们采用SpringMVC+Spring+Mybatis框架进行开发的时候，搭建和整合三大框架，我们需要做很好工作，比如配置web.xml，配置Spring，配置Mybatis,并将它们整合在一起等，而Spring boot框架对此开发过程进行了革命性的颠覆，抛弃了繁琐的xml配置过程，采用大量的默认配置简化我们的开发过程.

3.   所以采用Spring boot可以非常容易和快速的创建基于Spring框架的应用程序，它让编码变简单了，配置变简单了，部署变简单了，监控也变简单了。

   ​

------



## 一、SpringBoot特征：

- Create stand-alone Spring applications
- Embed Tomcat, Jetty or Undertow directly (no need to deploy WAR files)
- Provide opinionated 'starter' dependencies to simplify your build configuration
- Automatically configure Spring and 3rd party libraries whenever possible
- Provide production-ready features such as metrics, health checks and externalized configuration
- Absolutely no code generation and no requirement for XML configuration

## 二、SpringBoot版本：

**CURRENT**: 当前最新版本

**GA**:General Availability,正式发布的版本，官方推荐使用此版本。在国外都是用GA来说明release版本的。

**PRE**: 预览版,内部测试版. 主要是给开发人员和测试人员测试和找BUG用的，不建议使用；

**SNAPSHOT**: 快照版，可以稳定使用，且仍在继续改进版本。

[springBoot](https://spring.io/projects/spring-boot#learn)

![1556370374(1)](E:\熙邻\学习总结\2019.05-SpringBoot\image\1556370374(1).png)



2.0.9版本:   maven:3.2+  tomcat:8.5

## 三、SpringBoot  application starters:

Springboot提供了一些主流的整合包如下，但是一些非主流的整合就由整合方自己提供，例如springboot和dubbo的整合是由apache提供整合包。

![1557046570(1)](E:\熙邻\学习总结\2019.05-SpringBoot\image\1557046570(1).png)



## 四、Dubbo背景：

![dubbo-architecture-roadmap](E:\熙邻\学习总结\2019.05-SpringBoot\image\dubbo-architecture-roadmap.jpg)

#### 单一应用架构

当网站流量很小时，只需一个应用，将所有功能都部署在一起，以减少部署节点和成本。此时，用于简化增删改查工作量的数据访问框架(ORM)是关键。

#### 垂直应用架构

当访问量逐渐增大，单一应用增加机器带来的加速度越来越小，将应用拆成互不相干的几个应用，以提升效率。此时，用于加速前端页面开发的Web框架(MVC)是关键。

#### 分布式服务架构

当垂直应用越来越多，应用之间交互不可避免，将核心业务抽取出来，作为独立的服务，逐渐形成稳定的服务中心，使前端应用能更快速的响应多变的市场需求。此时，用于提高业务复用及整合的分布式服务框架(RPC)是关键。

#### 流动计算架构

当服务越来越多，容量的评估，小服务资源的浪费等问题逐渐显现，此时需增加一个调度中心基于访问压力实时管理集群容量，提高集群利用率。此时，用于提高机器利用率的资源调度和治理中心(SOA)是关键。



## 五、SpringBoot整合Dubbo:

[参考文档1：com.alibaba](https://github.com/alibaba/dubbo-spring-boot-starter)

[参考文档2：apache dubbo](https://github.com/apache/incubator-dubbo-spring-boot-project)



SpringBoot整合dubbo版本要求:

| Dubbo Spring Boot                        | Dubbo  | Spring Boot |
| ---------------------------------------- | ------ | ----------- |
| [0.2.1.RELEASE](https://github.com/apache/incubator-dubbo-spring-boot-project/tree/0.2.x) | 2.6.5+ | 2.x         |
| [0.1.2.RELEASE](https://github.com/apache/incubator-dubbo-spring-boot-project/tree/0.1.x) | 2.6.5+ | 1.x         |



步骤一：创建一个项目boot-dubbo，在父模块pom.xml引入基本配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>
    <groupId>com.clinks</groupId>
    <artifactId>boot-dubbo-parent</artifactId>
    <version>1.0-SNAPSHOT</version>
    <!-- 在这里设置打包类型为pom，作用是为了实现多模块项目 -->
    <packaging>pom</packaging>


    <!-- 第一步：添加Springboot的parent -->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.9.RELEASE</version>
    </parent>

    <modules>
        <module>../boot-dubbo-dl</module>
        <module>../boot-dubbo-service</module>
      <module>../boot-dubbo-web</module>
    </modules>

    <!-- 版本属性 -->
    <properties>
        <!--springBoot整合dubbo依赖-->
        <dubbo.boot.version>0.2.1.RELEASE</dubbo.boot.version>
        <dubbo.version>2.6.5</dubbo.version>
    </properties>

</project>
```



步骤二：创建三个子模块：boot-dubbo-api、boot-dubbo-provider、boot-dubbo-web

boot-dubbo-api作为接口模块，打成jar包可被另外两个模块引用，所有的服务接口声明都可写在该模块；

boot-dubbo-provider作为服务提供方，接口实现类可写在该模块，并被注册到注册中心，可供其他模块调用；

boot-dubbo-web作为服务消费方，可以rpc远程调用服务。



步骤三：boot-dubbo-dl部分pom.xml:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>
    <artifactId>boot-dubbo-dl</artifactId>
    <packaging>jar</packaging>

    <parent>
        <artifactId>boot-dubbo-parent</artifactId>
        <groupId>com.clinks</groupId>
        <version>1.0-SNAPSHOT</version>
        <relativePath>../boot-dubbo-parent/pom.xml</relativePath>
    </parent>

    <dependencies>
        <!-- jar依赖 -->
        <dependency>
            <groupId>com.clinks</groupId>
            <artifactId>boot-dubbo-service</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

        <!-- 核心SpringBoot-starter依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <!-- springBoot-dubbo依赖 -->
        <dependency>
            <groupId>com.alibaba.boot</groupId>
            <artifactId>dubbo-spring-boot-starter</artifactId>
            <version>${dubbo.boot.version}</version>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>dubbo</artifactId>
            <version>${dubbo.version}</version>
        </dependency>

        <!--使用redis做注册中心-->
        <dependency>
            <groupId>redis.clients</groupId>
            <artifactId>jedis</artifactId>
            <version>2.9.0</version>
        </dependency>
    </dependencies>
</project>
```



步骤四、配置文件

```xml
----xml方式----
<dubbo:application name="agent-hr-dl"/>
<dubbo:registry address="${dubbo.address}" username="test" password="${redis.password}"/>
<dubbo:consumer retries="0"  filter="cat_filter"/>
<dubbo:service interface="com.clinks.agent.hr.service.IAgentKpiService" ref="agentKpiServiceImpl"/>

----注解方式----
dubbo.application.name=dubbo-consumer-annotation
dubbo.registry.address=redis://139.196.28.237:6641
dubbo.registry.username=test
dubbo.registry.password=Toodc600
dubbo.consumer.timeout=3000
dubbo.consumer.registries=0

---javaConfig方式

```



步骤五：核心启动类Application

```java
@SpringBootApplication
@EnableDubbo
public class ProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }
}
```



步骤六：boot-dubbo-web部分pom.xml：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.clinks</groupId>
        <artifactId>boot-dubbo-parent</artifactId>
        <version>1.0-SNAPSHOT</version>
        <relativePath>../boot-dubbo-parent</relativePath>
    </parent>

    <groupId>com.clinks</groupId>
    <artifactId>boot-dubbo-web</artifactId>
    <version>1.0-SNAPSHOT</version>

    <dependencies>
        <!-- jar依赖 -->
        <dependency>
            <groupId>com.clinks</groupId>
            <artifactId>boot-dubbo-service</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

        <!-- 核心SpringBoot-starter依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <!-- springBoot-dubbo依赖 -->
        <dependency>
            <groupId>com.alibaba.boot</groupId>
            <artifactId>dubbo-spring-boot-starter</artifactId>
            <version>${dubbo.boot.version}</version>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>dubbo</artifactId>
            <version>${dubbo.version}</version>
        </dependency>
        <!--web模块-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!--test模块-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>4.3.7.RELEASE</version>
        </dependency>
        <!--redis-->
        <dependency>
            <groupId>redis.clients</groupId>
            <artifactId>jedis</artifactId>
            <version>2.9.0</version>
        </dependency>
    </dependencies>
</project>
```



## 六、核心注解篇

**注解一：@SpringBootApplication**：**

组合注解：

@Configuration ：是一个类注解，标明这是一个配置类，将启动类声明成一个配置类

一般与@Bean合用，作用相当于<beans><bean></bean></beans>

@ComponentScan  ：指定扫描包

Springboot默认扫描同包以及子包下注解，如@Controller,@Service,@Configuration

@EnableAutoConfiguration ： 开启 Spring 应用程序上下文的自动配置

原理简单说明：



**注解二：@EnableDubbo**

1. `@EnableDubbo` 注解是 `@EnableDubboConfig` 和 `@DubboComponentScan`两者组合的便捷表达方式。与注解驱动相关的是 `@DubboComponentScan`。
2. 通过 `@EnableDubbo` 可以在指定的包名下（通过 `scanBasePackages`），或者指定的类中（通过 `scanBasePackageClasses`）扫描 Dubbo 的服务提供者（以 `@Service` 标注）以及 Dubbo 的服务消费者（以 `Reference` 标注）。
3. 扫描到 Dubbo 的服务提供方和消费者之后，对其做相应的组装并初始化，并最终完成服务暴露或者引用的工作。

**注解三、@Service**

通过 `@Service` 上提供的属性，可以进一步的定制化 Dubbo 的服务提供方：

```
package org.apache.dubbo.config.annotation;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE}) // #1
@Inherited
public @interface Service {
    Class<?> interfaceClass() default void.class; // #2
    String interfaceName() default ""; // #3
    String version() default ""; // #4
    String group() default ""; // #5
    boolean export() default true; // #6
    boolean register() default true; // #7
    
    String application() default ""; // #8
    String module() default ""; // #9
    String provider() default ""; // #10
    String[] protocol() default {}; // #11
    String monitor() default ""; // #12
    String[] registry() default {}; // #13
}

```

其中比较重要的有：

1. @Service 只能定义在一个类上，表示一个服务的具体实现
2. interfaceClass：指定服务提供方实现的 interface 的类
3. interfaceName：指定服务提供方实现的 interface 的类名
4. version：指定服务的版本号
5. group：指定服务的分组
6. export：是否暴露服务
7. registry：是否向注册中心注册服务
8. application：应用配置
9. module：模块配置
10. provider：服务提供方配置
11. protocol：协议配置
12. monitor：监控中心配置
13. registry：注册中心配置

**注解四、@Reference**

通过 `@Reference` 上提供的属性，可以进一步的定制化 Dubbo 的服务消费方：

```
package org.apache.dubbo.config.annotation;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE}) // #1
public @interface Reference {
    Class<?> interfaceClass() default void.class; // #2
    String interfaceName() default ""; // #3
    String version() default ""; // #4
    String group() default ""; // #5
    String url() default ""; // #6
    
    String application() default ""; // #7
    String module() default ""; // #8
    String consumer() default ""; // #9
    String protocol() default ""; // #10
    String monitor() default ""; // #11
    String[] registry() default {}; // #12
}

```

其中比较重要的有：

1. @Reference 可以定义在类中的一个字段上，也可以定义在一个方法上，甚至可以用来修饰另一个 annotation，表示一个服务的引用。通常 @Reference 定义在一个字段上
2. interfaceClass：指定服务的 interface 的类
3. interfaceName：指定服务的 interface 的类名
4. version：指定服务的版本号
5. group：指定服务的分组
6. url：通过指定服务提供方的 URL 地址直接绕过注册中心发起调用
7. application：应用配置
8. module：模块配置
9. consumer：服务消费方配置
10. protocol：协议配置
11. monitor：监控中心配置
12. registry：注册中心配置

## 七、SpringBoot源码解析 

**启动流程：**

 **1、创建SpringApplication对象** 

```java
initialize(sources);
private void initialize(Object[] sources) {    
  //保存主配置类     
  if (sources != null && sources.length > 0) {         this.sources.addAll(Arrays.asList(sources));     
  } 
  
  //判断当前是否一个web应用     
  this.webEnvironment = deduceWebEnvironment(); 
  
  //从类路径下找到META‐INF/spring.factories配置的所有ApplicationContextInitializer；然后保存起来 
  setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class);     
  //从类路径下找到ETA‐INF/spring.factories配置的所有ApplicationListener
  setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));   
                  
  //从多个配置类中找到有main方法的主配置类
  this.mainApplicationClass = deduceMainApplicationClass(); 
 }
                  
```



**2、运行run()方法**

```java
public ConfigurableApplicationContext run(String... args) {    StopWatch stopWatch = new StopWatch();    
   stopWatch.start();
   ConfigurableApplicationContext context = null;    
   FailureAnalyzers analyzers = null;    
   configureHeadlessProperty(); 
                                                           
   //获取SpringApplicationRunListeners；从类路径下META‐INF/spring.factories
                                                                  		SpringApplicationRunListeners listeners = getRunListeners(args);
                                                                      
    //回调所有的获取SpringApplicationRunListener.starting()方法    
    listeners.starting();   
     try {       
       //封装命令行参数
       ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);       
       
       //准备环境       
       ConfigurableEnvironment environment = prepareEnvironment(listeners,applicationArguments); 
       //创建环境完成后回调SpringApplicationRunListener.environmentPrepared()；表示环境准备完成
       
       Banner printedBanner = printBanner(environment);                
       
       //创建ApplicationContext；决定创建web的ioc还是普通的ioc 
       context = createApplicationContext();               analyzers = new FailureAnalyzers(context);        
       //准备上下文环境;将environment保存到ioc中；而且applyInitializers()；        //applyInitializers()：回调之前保存的所有的ApplicationContextInitializer的initialize方法        //回调所有的SpringApplicationRunListener的contextPrepared()；             
      
       prepareContext(context, environment, listeners, applicationArguments, printedBanner);        
       //prepareContext运行完成以后回调所有的SpringApplicationRunListener的contextLoaded（）；        
       //刷新容器；ioc容器初始化（如果是web应用还会创建嵌入式的Tomcat）；Spring注解版       
       //扫描，创建，加载所有组件的地方；（配置类，组件，自动配置）      
       refreshContext(context);       
       
       //从ioc容器中获取所有的ApplicationRunner和CommandLineRunner进行回调        //ApplicationRunner先回调，CommandLineRunner再回调 
       afterRefresh(context, applicationArguments);  
       
       //所有的SpringApplicationRunListener回调finished方法 
       listeners.finished(context, null);       
       stopWatch.stop();       
       if (this.logStartupInfo) {         
         new StartupInfoLogger(this.mainApplicationClass)                .logStarted(getApplicationLog(), stopWatch);      
       }        
       //整个SpringBoot应用启动完成以后返回启动的ioc容器；       
       return context;    
     }   catch (Throwable ex) {      
       handleRunFailure(context, listeners, analyzers, ex); 
       throw new IllegalStateException(ex);    
     }
}

```

  **3. 生命周期流程：**

```java
// 使用SpringFactoriesLoader在应用的classpath中查找并加载所有可用的ApplicationContextInitializer。
// 使用SpringFactoriesLoader在应用的classpath中查找并加载所有可用的ApplicationListener。     
    初始化SpringApplication时候收集各种回调接口

// SpringFactoriesLoader可以查找到并加载的SpringApplicationRunListener
SpringApplicationRunListeners listeners = this.getRunListeners(args);

// 说明SpringBoot开始执行了
listeners.starting();  

// 创建并且配置SpringBoot使用环境
this.prepareEnvironment(listeners );  {  listeners.environmentPrepared();  // 说明SpringBoot环境已经配置好  }

// 创建Spring容器
context = this.createApplicationContext()

// 配置Spring容器上下文
 this.prepareContext(context, environment, listeners, applicationArguments, printedBanner); {
       
        this.applyInitializers(context); {  this.getInitializers() .initialize(context)  }
        listeners.contextPrepared(context);  {  this.listeners(). contextPrepared(context)   // 在创建和准备好ApplicationContext之后，但在加载源之前调用。 }
        listeners.contextLoaded(context);  {   this.listeners().contextLoaded(context)  //在加载应用程序上下文后但刷新之前调用 }

}

// 刷新Spring容器，通过反射生成bean
this.refreshContext(context);

this.afterRefresh(); 

//上下文已刷新，应用程序已启动，但尚未调用commandlinerunner和applicationrunner
listeners.started(context);

// 查找当前ApplicationContext中是否注册有CommandLineRunner，如果有，则遍历执行它们。
this.callRunners(context, applicationArguments);  {  ApplicationRunner;   CommandLineRunner;   }



```



# 八、SpringBoot自动配置

SpringBoot为我们做的自动配置，确实方便快捷，但是对于新手来说，如果不大懂SpringBoot内部启动原理，以后难免会吃亏。

![img](https://img2018.cnblogs.com/blog/1112095/201811/1112095-20181115161924186-928667393.png)

 

### 入口类

我们开发任何一个Spring Boot项目，都会用到如下的启动类

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

从上面代码可以看出，Annotation定义（@SpringBootApplication）和类定义（SpringApplication.run）最为耀眼，所以要揭开SpringBoot的神秘面纱，我们要从这两位开始就可以了。

### SpringBootApplication背后的秘密

```java
@Target(ElementType.TYPE)            // 注解的适用范围，其中TYPE用于描述类、接口（包括包注解类型）或enum声明
@Retention(RetentionPolicy.RUNTIME)  // 注解的生命周期，保留到class文件中（三个生命周期）
@Documented                          // 表明这个注解应该被javadoc记录
@Inherited                           // 子类可以继承该注解
@SpringBootConfiguration             // 继承了Configuration，表示当前是注解类
@EnableAutoConfiguration             // 开启springboot的注解功能，springboot的四大神器之一，其借助@import的帮助
@ComponentScan(excludeFilters = {    // 扫描路径设置（具体使用待确认）
        @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
        @Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication {
...
}
```

虽然定义使用了多个Annotation进行了原信息标注，但实际上重要的只有三个Annotation：

- @Configuration（@SpringBootConfiguration点开查看发现里面还是应用了@Configuration）
- @EnableAutoConfiguration
- @ComponentScan

所以，如果我们使用如下的SpringBoot启动类，整个SpringBoot应用依然可以与之前的启动类功能对等：

```java
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

每次写这3个比较累，所以写一个@SpringBootApplication方便点。接下来分别介绍这3个Annotation。

### @Configuration

这里的@Configuration对我们来说不陌生，它就是JavaConfig形式的Spring Ioc容器的配置类使用的那个@Configuration，SpringBoot社区推荐使用基于JavaConfig的配置形式，所以，这里的启动类标注了@Configuration之后，本身其实也是一个IoC容器的配置类。
举几个简单例子回顾下，XML跟config配置方式的区别：



- - 表达形式层面
    基于XML配置的方式是这样：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd"
       default-lazy-init="true">
    <!--bean定义-->
</beans>
```

而基于JavaConfig的配置方式是这样：

```java
@Configuration
public class MockConfiguration{
    //bean定义
}
```

任何一个标注了@Configuration的Java类定义都是一个JavaConfig配置类。



- - 注册bean定义层面
    基于XML的配置形式是这样：

```xml
<bean id="mockService" class="..MockServiceImpl">
    ...
</bean>
```

而基于JavaConfig的配置形式是这样的：

```java
@Configuration
public class MockConfiguration{
    @Bean
    public MockService mockService(){
        return new MockServiceImpl();
    }
}
```

任何一个标注了@Bean的方法，其返回值将作为一个bean定义注册到Spring的IoC容器，方法名将默认成该bean定义的id。



- - 表达依赖注入关系层面
    为了表达bean与bean之间的依赖关系，在XML形式中一般是这样：

```xml
<bean id="mockService" class="..MockServiceImpl">
    <propery name ="dependencyService" ref="dependencyService" />
</bean>

<bean id="dependencyService" class="DependencyServiceImpl"></bean>
```

而基于JavaConfig的配置形式是这样的：



```java
@Configuration
public class MockConfiguration{
    @Bean
    public MockService mockService(){
        return new MockServiceImpl(dependencyService());
    }
    
    @Bean
    public DependencyService dependencyService(){
        return new DependencyServiceImpl();
    }java
}
```



如果一个bean的定义依赖其他bean,则直接调用对应的JavaConfig类中依赖bean的创建方法就可以了。

### @ComponentScan

@ComponentScan这个注解在Spring中很重要，它对应XML配置中的元素，@ComponentScan的功能其实就是自动扫描并加载符合条件的组件（比如@Component和@Repository等）或者bean定义，最终将这些bean定义加载到IoC容器中。

我们可以通过basePackages等属性来细粒度的定制@ComponentScan自动扫描的范围，如果不指定，则默认Spring框架实现会从声明@ComponentScan所在类的package进行扫描。

### @EnableAutoConfiguration

个人感觉@EnableAutoConfiguration这个Annotation最为重要，所以放在最后来解读，大家是否还记得Spring框架提供的各种名字为@Enable开头的Annotation定义？比如@EnableScheduling、@EnableCaching、@EnableMBeanExport等，@EnableAutoConfiguration的理念和做事方式其实一脉相承，简单概括一下就是，借助@Import的支持，收集和注册特定场景相关的bean定义。

- @EnableScheduling是通过@Import将Spring调度框架相关的bean定义都加载到IoC容器。
- @EnableMBeanExport是通过@Import将JMX相关的bean定义加载到IoC容器。

而@EnableAutoConfiguration也是借助@Import的帮助，将所有符合自动配置条件的bean定义加载到IoC容器，仅此而已！

@EnableAutoConfiguration作为一个复合Annotation,其自身定义关键信息如下：



```java
@SuppressWarnings("deprecation")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import(EnableAutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration {
    ...
}
```



#### **两个比较重要的注解：**

- @AutoConfigurationPackage：自动配置包
- @Import: 导入自动配置的组件

#### AutoConfigurationPackage注解：



```java
1     static class Registrar implements ImportBeanDefinitionRegistrar, DeterminableImports {
2 
3         @Override
4         public void registerBeanDefinitions(AnnotationMetadata metadata,
5                 BeanDefinitionRegistry registry) {
6             register(registry, new PackageImport(metadata).getPackageName());
7         }
```



它其实是注册了一个Bean的定义。

**new PackageImport(metadata).getPackageName()**，它其实返回了当前主程序类的 *同级以及子级 *    的包组件。

**![img](https://img2018.cnblogs.com/blog/1335795/201810/1335795-20181025170845186-273562917.jpg)**

以上图为例，DemoApplication是和demo包同级，但是demo2这个类是DemoApplication的父级，和example包同级

也就是说，DemoApplication启动加载的Bean中，并不会加载demo2，这也就是为什么，我们要把DemoApplication放在项目的最高级中。

#### Import(AutoConfigurationImportSelector.class)注解：

![img](https://img2018.cnblogs.com/blog/1112095/201811/1112095-20181115152043749-1596939041.png)

可以从图中看出 ** ****AutoConfigurationImportSelector 继承了**** DeferredImportSelector 继承了 ImportSelector**

ImportSelector有一个方法为：**selectImports。**



```java
@Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        if (!isEnabled(annotationMetadata)) {
            return NO_IMPORTS;
        }
        AutoConfigurationMetadata autoConfigurationMetadata = AutoConfigurationMetadataLoader
                .loadMetadata(this.beanClassLoader);
        AnnotationAttributes attributes = getAttributes(annotationMetadata);
        List<String> configurations = getCandidateConfigurations(annotationMetadata,
                attributes);
        configurations = removeDuplicates(configurations);
        Set<String> exclusions = getExclusions(annotationMetadata, attributes);
        checkExcludedClasses(configurations, exclusions);
        configurations.removeAll(exclusions);
        configurations = filter(configurations, autoConfigurationMetadata);
        fireAutoConfigurationImportEvents(configurations, exclusions);
        return StringUtils.toStringArray(configurations);
    }
```



可以看到第九行，它其实是去加载  **public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/spring.factories";**外部文件。这个外部文件，有很多自动配置的类。如下：

![img](https://img2018.cnblogs.com/blog/1335795/201810/1335795-20181025172502394-39889528.jpg)

其中，最关键的要属@Import(EnableAutoConfigurationImportSelector.class)，借助EnableAutoConfigurationImportSelector，@EnableAutoConfiguration可以帮助SpringBoot应用将所有符合条件的@Configuration配置都加载到当前SpringBoot创建并使用的IoC容器。就像一只“八爪鱼”一样。

 ![img](https://img2018.cnblogs.com/blog/1112095/201811/1112095-20181116083851258-1000510503.png)

 

### **自动配置幕后英雄：SpringFactoriesLoader详解**

借助于Spring框架原有的一个工具类：SpringFactoriesLoader的支持，@EnableAutoConfiguration可以智能的自动配置功效才得以大功告成！

SpringFactoriesLoader属于Spring框架私有的一种扩展方案，其主要功能就是从指定的配置文件META-INF/spring.factories加载配置。



```java
public abstract class SpringFactoriesLoader {
    //...
    public static <T> List<T> loadFactories(Class<T> factoryClass, ClassLoader classLoader) {
        ...
    }


    public static List<String> loadFactoryNames(Class<?> factoryClass, ClassLoader classLoader) {
        ....
    }
}
```



配合@EnableAutoConfiguration使用的话，它更多是提供一种配置查找的功能支持，即根据@EnableAutoConfiguration的完整类名org.springframework.boot.autoconfigure.EnableAutoConfiguration作为查找的Key,获取对应的一组@Configuration类

![img](http://mmbiz.qpic.cn/mmbiz_jpg/pcT41pYWuUHL6ZWFVCvMDEpd6z50YV0BdBhOVrpZuOXYxgx1N9DsgeIJT0v022JyiaubzEYhuvg5tY1zqUGrtNA/0?wx_fmt=jpeg)



上图就是从SpringBoot的autoconfigure依赖包中的META-INF/spring.factories配置文件中摘录的一段内容，可以很好地说明问题。

所以，@EnableAutoConfiguration自动配置的魔法骑士就变成了：**从classpath中搜寻所有的META-INF/spring.factories配置文件，并将其中org.springframework.boot.autoconfigure.EnableutoConfiguration对应的配置项通过反射（Java Refletion）实例化为对应的标注了@Configuration的JavaConfig形式的IoC容器配置类，然后汇总为一个并加载到IoC容器。**

#### ****SpringBoot的启动原理基本算是讲完了，为了方便记忆，我根据上面的分析画了张图

![img](https://img2018.cnblogs.com/blog/1112095/201811/1112095-20181115230505205-305138350.png)

 

## 深入探索SpringApplication执行流程

SpringApplication的run方法的实现是我们本次旅程的主要线路，该方法的主要流程大体可以归纳如下：

1） 如果我们使用的是SpringApplication的静态run方法，那么，这个方法里面首先要创建一个SpringApplication对象实例，然后调用这个创建好的SpringApplication的实例方法。在SpringApplication实例初始化的时候，它会提前做几件事情：

```java
public static ConfigurableApplicationContext run(Object[] sources, String[] args) {
        return new SpringApplication(sources).run(args);
    }
```

- 根据classpath里面是否存在某个特征类（org.springframework.web.context.ConfigurableWebApplicationContext）来决定是否应该创建一个为Web应用使用的ApplicationContext类型。
- 使用SpringFactoriesLoader在应用的classpath中查找并加载所有可用的ApplicationContextInitializer。
- 使用SpringFactoriesLoader在应用的classpath中查找并加载所有可用的ApplicationListener。
- 推断并设置main方法的定义类。



```java
@SuppressWarnings({ "unchecked", "rawtypes" })
    private void initialize(Object[] sources) {
        if (sources != null && sources.length > 0) {
            this.sources.addAll(Arrays.asList(sources));
        }
        this.webEnvironment = deduceWebEnvironment();
        setInitializers((Collection) getSpringFactoriesInstances(
                ApplicationContextInitializer.class));
        setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
        this.mainApplicationClass = deduceMainApplicationClass();
    }
```



2） SpringApplication实例初始化完成并且完成设置后，就开始执行run方法的逻辑了，方法执行伊始，首先遍历执行所有通过SpringFactoriesLoader可以查找到并加载的SpringApplicationRunListener。调用它们的started()方法，告诉这些SpringApplicationRunListener，“嘿，SpringBoot应用要开始执行咯！”。



```java
public ConfigurableApplicationContext run(String... args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ConfigurableApplicationContext context = null;
        FailureAnalyzers analyzers = null;
        configureHeadlessProperty();
        SpringApplicationRunListeners listeners = getRunListeners(args);
        listeners.starting();
        try {
            ApplicationArguments applicationArguments = new DefaultApplicationArguments(
                    args);
            ConfigurableEnvironment environment = prepareEnvironment(listeners,
                    applicationArguments);
            Banner printedBanner = printBanner(environment);
            context = createApplicationContext();
            analyzers = new FailureAnalyzers(context);
            prepareContext(context, environment, listeners, applicationArguments,
                    printedBanner);
　　　　　　　// 核心点：会打印springboot的启动标志，直到server.port端口启动
            refreshContext(context);
            afterRefresh(context, applicationArguments);
            listeners.finished(context, null);
            stopWatch.stop();
            if (this.logStartupInfo) {
                new StartupInfoLogger(this.mainApplicationClass)
                        .logStarted(getApplicationLog(), stopWatch);
            }
            return context;
        }
        catch (Throwable ex) {
            handleRunFailure(context, listeners, analyzers, ex);
            throw new IllegalStateException(ex);
        }
    }
```



3） 创建并配置当前Spring Boot应用将要使用的Environment（包括配置要使用的PropertySource以及Profile）。



```java
private ConfigurableEnvironment prepareEnvironment(
            SpringApplicationRunListeners listeners,
            ApplicationArguments applicationArguments) {
        // Create and configure the environment
        ConfigurableEnvironment environment = getOrCreateEnvironment();
        configureEnvironment(environment, applicationArguments.getSourceArgs());
        listeners.environmentPrepared(environment);
        if (!this.webEnvironment) {
            environment = new EnvironmentConverter(getClassLoader())
                    .convertToStandardEnvironmentIfNecessary(environment);
        }
        return environment;
    }
```



4） 遍历调用所有SpringApplicationRunListener的environmentPrepared()的方法，告诉他们：“当前SpringBoot应用使用的Environment准备好了咯！”。

```java
public void environmentPrepared(ConfigurableEnvironment environment) {
        for (SpringApplicationRunListener listener : this.listeners) {
            listener.environmentPrepared(environment);
        }
    }
```

5） 如果SpringApplication的showBanner属性被设置为true，则打印banner。



```java
private Banner printBanner(ConfigurableEnvironment environment) {
        if (this.bannerMode == Banner.Mode.OFF) {
            return null;
        }
        ResourceLoader resourceLoader = this.resourceLoader != null ? this.resourceLoader
                : new DefaultResourceLoader(getClassLoader());
        SpringApplicationBannerPrinter bannerPrinter = new SpringApplicationBannerPrinter(
                resourceLoader, this.banner);
        if (this.bannerMode == Mode.LOG) {
            return bannerPrinter.print(environment, this.mainApplicationClass, logger);
        }
        return bannerPrinter.print(environment, this.mainApplicationClass, System.out);
    }
```



6） 根据用户是否明确设置了applicationContextClass类型以及初始化阶段的推断结果，决定该为当前SpringBoot应用创建什么类型的ApplicationContext并创建完成，然后根据条件决定是否添加ShutdownHook，决定是否使用自定义的BeanNameGenerator，决定是否使用自定义的ResourceLoader，当然，最重要的，将之前准备好的Environment设置给创建好的ApplicationContext使用。

 

7） ApplicationContext创建好之后，SpringApplication会再次借助Spring-FactoriesLoader，查找并加载classpath中所有可用的ApplicationContext-Initializer，然后遍历调用这些ApplicationContextInitializer的initialize（applicationContext）方法来对已经创建好的ApplicationContext进行进一步的处理。



```java
@SuppressWarnings({ "rawtypes", "unchecked" })
    protected void applyInitializers(ConfigurableApplicationContext context) {
        for (ApplicationContextInitializer initializer : getInitializers()) {
            Class<?> requiredType = GenericTypeResolver.resolveTypeArgument(
                    initializer.getClass(), ApplicationContextInitializer.class);
            Assert.isInstanceOf(requiredType, context, "Unable to call initializer.");
            initializer.initialize(context);
        }
    }
```



8） 遍历调用所有SpringApplicationRunListener的contextPrepared()方法。



```java
private void prepareContext(ConfigurableApplicationContext context,
            ConfigurableEnvironment environment, SpringApplicationRunListeners listeners,
            ApplicationArguments applicationArguments, Banner printedBanner) {
        context.setEnvironment(environment);
        postProcessApplicationContext(context);
        applyInitializers(context);
        listeners.contextPrepared(context);
        if (this.logStartupInfo) {
            logStartupInfo(context.getParent() == null);
            logStartupProfileInfo(context);
        }

        // Add boot specific singleton beans
        context.getBeanFactory().registerSingleton("springApplicationArguments",
                applicationArguments);
        if (printedBanner != null) {
            context.getBeanFactory().registerSingleton("springBootBanner", printedBanner);
        }

        // Load the sources
        Set<Object> sources = getSources();
        Assert.notEmpty(sources, "Sources must not be empty");
        load(context, sources.toArray(new Object[sources.size()]));
        listeners.contextLoaded(context);
    }
```



9） 最核心的一步，将之前通过@EnableAutoConfiguration获取的所有配置以及其他形式的IoC容器配置加载到已经准备完毕的ApplicationContext。



```java
private void prepareAnalyzer(ConfigurableApplicationContext context,
            FailureAnalyzer analyzer) {
        if (analyzer instanceof BeanFactoryAware) {
            ((BeanFactoryAware) analyzer).setBeanFactory(context.getBeanFactory());
        }
    }
```



10） 遍历调用所有SpringApplicationRunListener的contextLoaded()方法。

```java
public void contextLoaded(ConfigurableApplicationContext context) {
        for (SpringApplicationRunListener listener : this.listeners) {
            listener.contextLoaded(context);
        }
    }
```

11） 调用ApplicationContext的refresh()方法，完成IoC容器可用的最后一道工序。



```java
private void refreshContext(ConfigurableApplicationContext context) {
        refresh(context);
        if (this.registerShutdownHook) {
            try {
                context.registerShutdownHook();
            }
            catch (AccessControlException ex) {
                // Not allowed in some environments.
            }
        }
    }
```



12） 查找当前ApplicationContext中是否注册有CommandLineRunner，如果有，则遍历执行它们。



```java
private void callRunners(ApplicationContext context, ApplicationArguments args) {
        List<Object> runners = new ArrayList<Object>();
        runners.addAll(context.getBeansOfType(ApplicationRunner.class).values());
        runners.addAll(context.getBeansOfType(CommandLineRunner.class).values());
        AnnotationAwareOrderComparator.sort(runners);
        for (Object runner : new LinkedHashSet<Object>(runners)) {
            if (runner instanceof ApplicationRunner) {
                callRunner((ApplicationRunner) runner, args);
            }
            if (runner instanceof CommandLineRunner) {
                callRunner((CommandLineRunner) runner, args);
            }
        }
    }
```



13） 正常情况下，遍历执行SpringApplicationRunListener的finished()方法、（如果整个过程出现异常，则依然调用所有SpringApplicationRunListener的finished()方法，只不过这种情况下会将异常信息一并传入处理）
去除事件通知点后，整个流程如下：

```java
public void finished(ConfigurableApplicationContext context, Throwable exception) {
        for (SpringApplicationRunListener listener : this.listeners) {
            callFinishedListener(listener, context, exception);
        }
    }
```



# ![img](https://img2018.cnblogs.com/blog/1112095/201811/1112095-20181116083818373-417729577.png)

##  *自定义*事件监听机制 

`SpringApplicationRunListener` 接口规定了 `SpringBoot` 的生命周期，在各个生命周期广播相应的事件，调用实际的 `ApplicationListener` 类。通过对 `SpringApplicationRunListener` 的分析，也可以对 `SpringBoot` 的整个启动过程的理解会有很大帮助。

```java
public interface SpringApplicationRunListener {
	//当run方法首次启动时立即调用。可用于非常早期的初始化。
	void starting();
	//在准备好环境后，但在创建ApplicationContext之前调用。
	void environmentPrepared(ConfigurableEnvironment environment);
	//在创建和准备好ApplicationContext之后，但在加载源之前调用。
	void contextPrepared(ConfigurableApplicationContext context);
	//在加载应用程序上下文后但刷新之前调用
	void contextLoaded(ConfigurableApplicationContext context);
	//上下文已刷新，应用程序已启动，但尚未调用commandlinerunner和applicationrunner。
	void started(ConfigurableApplicationContext context);
	//在运行方法完成之前立即调用，此时应用程序上下文已刷新，
	//并且所有commandlinerunner和applicationrunner都已调用。
	//2.0 才有
	void running(ConfigurableApplicationContext context);
	//在运行应用程序时发生故障时调用。2.0 才有
	void failed(ConfigurableApplicationContext context, Throwable exception);
}
```

![1558960957(1)](E:\熙邻\学习总结\2019.05-SpringBoot\image\1558960957(1).png)

```xml
ApplicationStartingEvent在运行开始时但在任何处理之前发送，侦听器和初始值设定项的注册除外。

ApplicationEnvironmentPreparedEvent在上下文中要使用的环境已知但在创建上下文之前发送。

ApplicationPreparedEvent将在刷新开始之前，但在加载bean定义之后发送。

ApplicationStartedEvent在刷新上下文之后，但在调用任何应用程序和命令行运行程序之前发送。

ApplicationreadyEvent在调用任何应用程序和命令行运行程序之后，将发送。它表示应用程序已准备好服务请求。

ApplicationFailedEvent如果启动时出现异常，则发送。
```

ApplicationContextInitializer

```java
package web.listeners;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author dongmei.tan
 * @date 2019/5/26 17:09
 */
public class HelloApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        System.out.println("HelloApplicationContextInitializer...initialize...");
    }
}


```

SpringApplicationRunListener

```java
package web.listeners;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author dongmei.tan
 * @date 2019/5/26 17:16
 */
public class HelloSpringApplicationRunListener implements SpringApplicationRunListener {

    public HelloSpringApplicationRunListener(SpringApplication application, String[] args) {

    }

    @Override
    public void starting() {
        System.out.println("SpringApplicationRunListener...starting.. ");
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        System.out.println("SpringApplicationRunListener...environmentPrepared..; ==environment" + environment);
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        System.out.println("SpringApplicationRunListener...contextPrepared..==> " + context);
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        System.out.println("SpringApplicationRunListener...starting..==> " + context);
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        System.out.println("SpringApplicationRunListener...started..==> " + context);
    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        System.out.println("SpringApplicationRunListener...running..==> " + context);
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        System.out.println("SpringApplicationRunListener...failed..==> " + context);
    }
}

```


配置（META-INF/spring.factories）

```xml
org.springframework.context.ApplicationContextInitializer=\
web.listeners.HelloApplicationContextInitializer

# Application Listeners
org.springframework.boot.SpringApplicationRunListener=\
web.listeners.HelloSpringApplicationRunListener

```

    只需要放在ioc容器中
   ApplicationRunner

  CommandLineRunner

```
如果在SpringApplication启动后需要运行一些特定的代码，可以实现ApplicationRunner或CommandLineRunner接口。两个接口以相同的方式工作，并提供一个单独的运行方法，该方法在SpringApplication.Run（…）完成之前调用。

commandLineRunner接口以简单的字符串数组形式提供对应用程序参数的访问，而applicationRunner使用前面讨论过的applicationArguments接口。下面的示例显示了带有run方法的commandlinerunner：
```



```java
package web.listeners;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author dongmei.tan
 * @date 2019/5/26 17:21
 */
@Component
public class HelloApplicationRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("ApplicationRunner...run..");
    }
}

```

```java
package web.listeners;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author dongmei.tan
 * @date 2019/5/26 17:22
 */
@Component
public class HelloCommandLineRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("CommandLineRunner...run...");
    }
}

```



