#### 一、Spring的Bean怎么动态加载

#### 二、Spring中的拦截器，它与filter有什么区别，它们的执行顺序，分别主要用来干什么

拦截器/过滤器/AOP

Filter过滤器：拦截Web访问url地址，这个比拦截器范围广，过滤器是大集合。是基于函数回调的

Interceptor拦截器：拦截url以action结尾或者没有后缀，拦截器是基于动态代理的

AOP拦截器：只能拦截Spring管理bean的访问，

#### 三、filter和拦截器是线程安全的吗，为什么

#### 4. Spring AOP实现原理

#### 5. Spring如何解决循环依赖

