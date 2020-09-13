#### Spring的Bean怎么动态加载

1. 使用 BeanDefinitionBuilder注册bean（BeanDefinitionBuilder.rootBeanDefinition），添加属性&注册bean，beanFactory.registerBeanDefinition("testBean", b1.getBeanDefinition());
2. 使用 BeanFactoryPostProcessor注册bean。BeanFactoryPostProcessor允许自定义BeanDefinition，使用的GenericBeanDefinition，然后再注册入DefaultListableBeanFactory。
3. 使用GenericBeanDefinition

#### Spring中的拦截器，它与filter有什么区别，它们的执行顺序，分别主要用来干什么

拦截器/过滤器/AOP

Filter过滤器：拦截Web访问url地址，这个比拦截器范围广，过滤器是大集合。是基于函数回调的

Interceptor拦截器：拦截url以action结尾或者没有后缀，拦截器是基于动态代理的

AOP拦截器：只能拦截Spring管理bean的访问，

#### filter和拦截器是线程安全的吗，为什么

Filter 是在 Servlet 容器启动时就初始化的，因此可以认为是以单例对象存在的，如果一个请求线程对其中的成员变量修改的话，会影响到其他的请求线程，因此认为是多线程不安全的。

#### AOP 核心概念

1. 切面（aspect）：类是对物体特征的抽象，切面就是对横切关注点的抽象
2. 横切关注点：对哪些方法进行拦截，拦截后怎么处理，这些关注点称之为横切关注点。
3. 连接点（joinpoint）：被拦截到的点，因为 Spring 只支持方法类型的连接点，所以在Spring 中连接点指的就是被拦截到的方法，实际上连接点还可以是字段或者构造器。
4. 切入点（pointcut）：对连接点进行拦截的定义
5. 通知（advice）：所谓通知指的就是指拦截到连接点之后要执行的代码，通知分为前置. 后置. 异常. 最终. 环绕通知五类。
6. 目标对象：代理的目标对象
7. 织入（weave）：将切面应用到目标对象并导致代理对象创建的过程
8. 引入（introduction）：在不修改代码的前提下，引入可以在运行期为类动态地添加方法或字段。

#### 解释一下AOP

传统oop开发代码逻辑自上而下的，这个过程中会产生一些横切性问题，这些问题与我们主业务逻辑关系不大，会散落在代码的各个地方，造成难以维护，aop思想就是把业务逻辑与横切的问题进行分离，达到解耦的目的，提高代码重用性和开发效率

#### AOP 主要应用场景有：

- 记录日志
- 监控性能
- 权限控制
- 事务管理

#### Spring AOP实现原理

- @EnableAspectJAutoProxy给容器（beanFactory）中注册一个AnnotationAwareAspectJAutoProxyCreator对象；
- AnnotationAwareAspectJAutoProxyCreator对目标对象进行代理对象的创建，对象内部，是封装JDK和CGlib两个技术，实现动态代理对象创建的（创建代理对象过程中，会先创建一个代理工厂，获取到所有的增强器（通知方法），将这些增强器和目标类注入代理工厂，再用代理工厂创建对象）；
- 代理对象执行目标方法，得到目标方法的拦截器链，利用拦截器的链式机制，依次进入每一个拦截器进行执行

#### AOP使用哪种动态代理?

- 当bean的是实现中存在接口或者是Proxy的子类，---jdk动态代理；不存在接口，spring会采用CGLIB来生成代理对象；
- JDK 动态代理主要涉及到 java.lang.reflect 包中的两个类：Proxy 和 InvocationHandler。
- Proxy 利用 InvocationHandler（定义横切逻辑） 接口动态创建 目标类的代理对象。

#### Spring如何解决循环依赖

怎么检测是否存在循环依赖？Bean在创建的时候可以给该Bean打标，如果递归调用回来发现正在创建中的话，即说明了循环依赖了。

Spring中循环依赖场景有：
- 构造器的循环依赖
- 属性的循环依赖
- singletonObjects：第一级缓存，里面放置的是实例化好的单例对象；earlySingletonObjects：第二级缓存，里面存放的是提前曝光的单例对象；singletonFactories：第三级缓存，里面存放的是要被实例化的对象的对象工厂
- 创建bean的时候Spring首先从一级缓存singletonObjects中获取。如果获取不到，并且对象正在创建中，就再从二级缓存earlySingletonObjects中获取，如果还是获取不到就从三级缓存singletonFactories中取（Bean调用构造函数进行实例化后，即使属性还未填充，就可以通过三级缓存向外提前暴露依赖的引用值（提前曝光），根据对象引用能定位到堆中的对象，其原理是基于Java的引用传递），取到后从三级缓存移动到了二级缓存完全初始化之后将自己放入到一级缓存中供其他使用，
- 因为加入singletonFactories三级缓存的前提是执行了构造器，所以构造器的循环依赖没法解决。
- 构造器循环依赖解决办法：在构造函数中使用@Lazy注解延迟加载。在注入依赖时，先注入代理对象，当首次使用时再创建对象说明：一种互斥的关系而非层次递进的关系，故称为三个Map而非三级缓存的缘由 完成注入；

#### BeanFactory 接口和 ApplicationContext 接口不同点是什么？

- BeanFactory：BeanFactory是spring中比较原始，比较古老的Factory。因为比较古老，所以BeanFactory无法支持spring插件，例如：AOP、Web应用等功能。
- ApplicationContext：ApplicationContext是BeanFactory的子类，因为古老的BeanFactory无法满足不断更新的spring的需求，于是ApplicationContext就基本上代替了BeanFactory的工作，以一种更面向框架的工作方式以及对上下文进行分层和实现继承，并在这个基础上对功能进行扩展：
  - 统一的资源文件访问方式。
  - 提供在监听器中注册bean的事件。
  - 同时加载多个配置文件。

#### 介绍一下 Spring 的事务的了解？

Spring框架为事务管理提供一套统一的抽象，带来的好处有：
1. 跨不同事务API的统一的编程模型，无论你使用的是jdbc、jta、jpa、hibernate。
2. 支持声明式事务
3. 简单的事务管理API
4. 能与Spring的数据访问抽象层完美集成

说明：Spring的事物管理是用AOP实现的

#### 介绍一下 Spring 的事务实现方式？

https://zhuanlan.zhihu.com/p/54067384

https://www.cnblogs.com/leeSmall/p/10306672.html

#### Spring框架的七大模块

- Spring Core：框架的最基础部分，提供 IoC 容器，对 bean 进行管理。
- Spring Context：继承BeanFactory，提供上下文信息，扩展出JNDI、EJB、电子邮件、国际化等功能。
- Spring DAO：提供了JDBC的抽象层，还提供了声明性事务管理方法。
- Spring ORM：提供了JPA、JDO、Hibernate、MyBatis 等ORM映射层.
- Spring AOP：集成了所有AOP功能
- Spring Web：提供了基础的 Web 开发的上下文信息，现有的Web框架，如JSF、Tapestry、Structs等，提供了集成
- Spring Web MVC：提供了 Web 应用的 Model-View-Controller 全功能实现。

#### Bean定义5种作用域

- singleton（单例） 
- prototype（原型） 
- request 
- session 
- global session

#### Spring ioc初始化流程?

resource定位 即寻找用户定义的bean资源，由 ResourceLoader通过统一的接口Resource接口来完成 beanDefinition载入 BeanDefinitionReader读取、解析Resource定位的资源 成BeanDefinition 载入到ioc中（通过HashMap进行维护BD） BeanDefinition注册 即向IOC容器注册这些BeanDefinition， 通过BeanDefinitionRegistery实现

#### BeanDefinition加载流程?

定义BeanDefinitionReader解析xml的document BeanDefinitionDocumentReader解析document成beanDefinition

#### DI依赖注入流程? （实例化，处理Bean之间的依赖关系）

过程在Ioc初始化后，依赖注入的过程是用户第一次向IoC容器索要Bean时触发
- 如果设置lazy-init=true，会在第一次getBean的时候才初始化bean， lazy-init=false，会容器启动的时候直接初始化（singleton bean）；
- 调用BeanFactory.getBean（）生成bean的；
- 生成bean过程运用装饰器模式产生的bean都是beanWrapper（bean的增强）；

#### 依赖注入怎么处理bean之间的依赖关系?

其实就是通过在beanDefinition载入时，如果bean有依赖关系，通过占位符来代替，在调用getbean时候，如果遇到占位符，从ioc里获取bean注入到本实例来

#### Bean的生命周期?

- 实例化Bean：Ioc容器通过获取BeanDefinition对象中的信息进行实例化，实例化对象被包装在BeanWrapper对象中
- 设置对象属性（DI）：通过BeanWrapper提供的设置属性的接口完成属性依赖注入；
- 注入Aware接口（BeanFactoryAware， 可以用这个方式来获取其它 Bean，ApplicationContextAware）：Spring会检测该对象是否实现了xxxAware接口，并将相关的xxxAware实例注入给bean
- BeanPostProcessor：自定义的处理（分前置处理和后置处理）
- InitializingBean和init-method：执行我们自己定义的初始化方法
- 使用
- destroy：bean的销毁

IOC：控制反转：将对象的创建权，由Spring管理. DI（依赖注入）：在Spring创建对象的过程中，把对象依赖的属性注入到类中。

#### Spring的IOC注入方式

构造器注入 setter方法注入 注解注入 接口注入

#### Spring 中使用了哪些设计模式？

- 工厂模式：spring中的BeanFactory就是简单工厂模式的体现，根据传入唯一的标识来获得bean对象；
- 单例模式：提供了全局的访问点BeanFactory；
- 代理模式：AOP功能的原理就使用代理模式（1、JDK动态代理。2、CGLib字节码生成技术代理。）
- 装饰器模式：依赖注入就需要使用BeanWrapper；
- 观察者模式：spring中Observer模式常用的地方是listener的实现。如ApplicationListener。
- 策略模式：Bean的实例化的时候决定采用何种方式初始化bean实例（反射或者CGLIB动态字节码生成）

#### springMVC流程：

1. 用户请求发送给DispatcherServlet，DispatcherServlet调用HandlerMapping处理器映射器；
2. HandlerMapping根据xml或注解找到对应的处理器，生成处理器对象返回给DispatcherServlet；
3. DispatcherServlet会调用相应的HandlerAdapter；
4. HandlerAdapter经过适配调用具体的处理器去处理请求，生成ModelAndView返回给DispatcherServlet
5. DispatcherServlet将ModelAndView传给ViewReslover解析生成View返回给DispatcherServlet；
6. DispatcherServlet根据View进行渲染视图；
   ->DispatcherServlet->HandlerMapping->Handler ->DispatcherServlet->HandlerAdapter处理handler->ModelAndView ->DispatcherServlet->ModelAndView->ViewReslover->View ->DispatcherServlet->返回给客户