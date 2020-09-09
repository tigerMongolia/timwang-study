#### 1. Spring的Bean怎么动态加载

#### 2. Spring中的拦截器，它与filter有什么区别，它们的执行顺序，分别主要用来干什么

拦截器/过滤器/AOP

Filter过滤器：拦截Web访问url地址，这个比拦截器范围广，过滤器是大集合。是基于函数回调的

Interceptor拦截器：拦截url以action结尾或者没有后缀，拦截器是基于动态代理的

AOP拦截器：只能拦截Spring管理bean的访问，

#### 3. filter和拦截器是线程安全的吗，为什么

#### 4. Spring AOP实现原理

#### 5. Spring如何解决循环依赖

#### 6. BeanFactory 接口和 ApplicationContext 接口不同点是什么？

#### 7. 介绍一下 Spring 的事务的了解？

#### 8. 介绍一下 Spring 的事务实现方式？

#### 9. Spring框架的七大模块

- Spring Core：框架的最基础部分，提供 IoC 容器，对 bean 进行管理。
- Spring Context：继承BeanFactory，提供上下文信息，扩展出JNDI、EJB、电子邮件、国际化等功能。
- Spring DAO：提供了JDBC的抽象层，还提供了声明性事务管理方法。
- Spring ORM：提供了JPA、JDO、Hibernate、MyBatis 等ORM映射层.
- Spring AOP：集成了所有AOP功能
- Spring Web：提供了基础的 Web 开发的上下文信息，现有的Web框架，如JSF、Tapestry、Structs等，提供了集成
- Spring Web MVC：提供了 Web 应用的 Model-View-Controller 全功能实现。

#### 10. Bean定义5种作用域

- singleton（单例） 
- prototype（原型） 
- request 
- session 
- global session