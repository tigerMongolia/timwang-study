一、什么是SpringBoot?

1.概念:Spring Boot是由Pivotal团队提供的全新框架，其设计目的是用来简化新Spring应用的初始搭建以及开发过程。
简化Spring应用开发的一个框架； 整个Spring技术栈的一个大整合； J2EE开发的一站式解决方案； 
微服务框架

2.特点:
        (1)快速搭建spring项目，提供默认配置简化项目配置
        (2)内置Tomcat, Jetty or Undertow容器
        (3)没有冗余代码生成和XML配置的要求
        (4)约定大于配置，不用写xml文件
        
        
二、快速搭建一个SpringBoot项目?
    方式一：1.创建一个maven项目，导入springboot相关的依赖：
        <parent>
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-starter-parent</artifactId>
             <version>2.0.6.RELEASE</version>
             <relativePath/>
        </parent>    
        <dependencies>         
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
            </dependency>    
         </dependencies>
    2.编写springboot入口类Application，写main方法
        @SpringBootApplication
        public class DemoApplication {
            public static void main(String[] args) {
                SpringApplication.run(DemoApplication.class, args);
            }
        }
    3.简化部署，将这个应用打成jar包，直接使用java -jar的命令进行执
        <build>
            <plugins>
                <plugin>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-maven-plugin</artifactId>
                </plugin>
            </plugins>
        </build>
    方式二：利用idea快速搭建一个springboot项目
    
    
 三、HelloWorld一探究竟?
    知识点1：父项目可以管理Spring Boot应用里面的所有依赖版本,以后我们导入依赖默认是不需要写版本,除非没有的需要声明版本号; 
    知识点2：启动器，Spring Boot将所有的功能场景都抽取出来,做成一个个的starters(启动器),只需要在项目里面引入这些starter,
            相关场景的所有依赖都会导入进来。要用什么功能就导入什么场景的启动器(具体的可以看spring官网Starters部分)
            spring-boot-starter-web可以不用引入，父项目中默认有这个依赖
    知识点3：Application类是springBoot项目的入口类，main方法是主方法；
            @SpringBootApplication 来标注一个主程序类，说明这是一个Spring Boot应用，该注解是一个组合注解。
     
     
四、springBoot小课堂?
    知识点1：bean自动扫描。
            springboot默认扫描和Application入口类同包或者子包下面的类，如果想要改变默认配置，可以在
            入口类上面加一个注解@ComponentScan(basePackages = {"com.clinks.boot.demo","com.clinks.test.demo"};
            但是要注意的是，如果修改了默认配置，那么原先的默认扫描Application类同包或者子包下类就不生效了，需要自己手动配置了。
    知识点2：springboot整合jsp。
            springboot推荐的视图是Thymeleaf,但我们更习惯用jsp视图，那怎么办呢？
            1) 添加jsp的maven支持：
                <dependency>
                    <groupId>org.apache.tomcat.embed</groupId>
                    <artifactId>tomcat-embed-jasper</artifactId>
                </dependency>
                <dependency>
                    <groupId>javax.servlet</groupId>
                    <artifactId>javax.servlet-api</artifactId>
                </dependency>
            2) 修改配置文件：
                spring.mvc.view.prefix=/WEB-INF/jsp/
                spring.mvc.view.suffix=.jsp
            3) 创建文件目录：webapp/WEB-INF/jsp/index.jsp
            4) 启动项目，整合成功
    知识点3: springboot整合SwaggerUI
            1) 添加依赖
                <dependency>
                    <groupId>io.springfox</groupId>
                    <artifactId>springfox-swagger2</artifactId>
                </dependency>
                <dependency>
                    <groupId>io.springfox</groupId>
                    <artifactId>springfox-swagger-ui</artifactId>
                </dependency>
            2) 配置类
            在config文件夹下面创建类SwaggerConfig
            @Configuration
            @EnableSwagger2
            public class SwaggerConfig {
                @Bean
                public Docket api() {
                    return new Docket(DocumentationType.SWAGGER_2)
                            .select()  // 选择那些路径和api会生成document
                            .apis(RequestHandlerSelectors.any()) // 对所有api进行监控(basePackage("com.clinks.controller")指定监控包)
                            .paths(PathSelectors.any()) // 对所有路径进行监控
                            .build();
                }
            }
            3) RestFul风格接口文档
               @GetMapping/@PostMapping/@PutMapping/@DeleteMapping/@ApiModel/@ApiModelProperty
               请求地址：localhost:8080/swagger-ui.html
            4) 还可以整合swagger-bootstrap-ui
               <dependency>
                   <groupId>io.springfox</groupId>
                   <artifactId>springfox-swagger2</artifactId>
                   <version>2.6.1</version>
               </dependency>
               <dependency>
                   <groupId>com.github.xiaoymin</groupId>
                   <artifactId>swagger-bootstrap-ui</artifactId>
                   <version>1.8.2</version>
               </dependency>
               请求地址：localhost:8080/doc.html
    知识点4：springboot整合jdbc
            1) 添加jdbc依赖
                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring‐boot‐starter‐jdbc</artifactId>
                </dependency>
                <dependency>
                    <groupId>mysql</groupId>
                    <artifactId>mysql‐connector‐java</artifactId>
                </dependency>
            2) 配置数据源，默认DataSource
                spring.datasource.username=root
                spring.datasource.password=123456
                spring.datasource.url=jdbc:mysql://192.168.15.22:3306/jdbc
                spring.datasource.driver-class-name=com.mysql.jdbc.Driver
    知识点5：springboot整合mybatis
            1) 添加依赖
                <dependency>
                    <groupId>org.mybatis.spring.boot</groupId>
                    <artifactId>mybatis‐spring‐boot‐starter</artifactId>
                </dependency>
            2) 配置扫描mapper
               扫描xml文件：mybatis.mapper-locations=classpath:mapper/*.xml
               扫描mapper接口：@MapperScan(basePackages = "com.clinks.mapper")
                              或者接口加上注解@Mapper  
                              
 五、springBoot整合dubbo?
     1) 添加依赖
        <!--dubbo 依赖-->
        <dependency>
            <groupId>io.dubbo.springboot</groupId>
            <artifactId>spring-boot-starter-dubbo</artifactId>
            <version>1.0.0</version>
        </dependency>
        <!--zookeeper依赖 -->
        <dependency>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
            <version>3.4.8</version>
        </dependency>
        <dependency>
            <groupId>com.101tec</groupId>
            <artifactId>zkclient</artifactId>
            <version>0.10</version>
        </dependency>
     2) yml配置文件
     服务提供者的配置，注意scan扫描的位置，凡是要配置接口实现类的包都要扫描到
         1.provider相关配置
             #spring.application.name=dubbo-provider
             server:
               port: 8100
             #应用名称
             spring:
               dubbo: 
                 application:
             #注册在注册中心的名称，唯一标识，请勿重复
                   id: dubbo-provider
                   name: auth-branch
             #注册中心地址，zookeeper集群，启动输出可以看见链接了多个
             #单zookeeper服务：zookeeper://127.0.0.1:2181
                 registry: 
                   address: zookeeper://127.0.0.1:2181?backup=127.0.0.1:2180,127.0.0.1:2182
             #暴露服务方式
                 protocol:
                   id: dubbo
             #默认名称，勿修改
                   name: dubbo
             #暴露服务端口 （默认是20880，修改端口，不同的服务提供者端口不能重复） 
                   port: 20881
                   status : server
             #调用dubbo组建扫描的项目路径
                 scan: com.demo.branch.impl
         2.consumer相关配置：    
             #spring.application.name=dubbo-consumer
             server: 
               context-path: /auth
               port: 8102
             #应用名称
             spring: 
               dubbo:
                 application:
                   name: auth-consumer
             #注册中心地址
                 protocol:
                   name: dubbo
                 registry:
                   address: zookeeper://127.0.0.1:2181?backup=127.0.0.1:2180,127.0.0.1:2182
             #调用dubbo组建扫描的项目路径
                 scan: com.demo.controller
             #telnet端口
                 qos:
                   port: 22223
             #检查服务是否可用默认为true，不可用时抛出异常，阻止spring初始化，为方便部署，可以改成false
                 consumer: 
                   check: false
     3) 加上dubbo注解 
        阿里的service注解还提供了其他参数，包括版本号等：
        @Service(version = "1.0.0",
                              application = "${dubbo.application.id}",
                              protocol = "${dubbo.protocol.id}",
                              registry = "${dubbo.registry.id}")
        @Reference的注解也包含了版本号及绑定的id
        @Reference(version = "1.0.0",
                               application = "${dubbo.application.id}",
                               url = "dubbo://localhost:12345")
    
        
                       
    
    

     
        
