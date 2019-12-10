> 基于Spring开发——自定义标签及其解析

​    https://blog.csdn.net/zzg1229059735/article/details/82669955

### 1.前言

​	在spring中使用过dubbo的人应该都配置过类似

```java
<dubbo:service interface="com.alibaba.dubbo.demo.DemoService" ref="demoService" />
<dubbo:reference id="demoService" interface="com.alibaba.dubbo.demo.DemoService" />
```

​	在spring官方文档中42.1点介绍中有详细说明怎么自定义标签，简单的来说自定义标签就是：

1. 编写一个xml架构来描述自定义的元素
2. 编写一个自定义的handler实现NamespaceHandler接口
3. 编写一个或者多个自定义的解析器实现BeanDefinitionParser接口 
4. 注册到Spring容器

### 2.自定义标签用途

​	自定义标签可以说是spring提供的最大、最强的Hook（钩子），通俗的讲就是给后续小伙伴开发组件，提供一个标准公共可拔插”接口”，大家可以理解为手机充电器的工业标准口，为了方便各个充电器生产厂家生产，而制定的标准。 

### 3.自定义标签使用

1. 根据需要编写xsd文件。

2. 编写标签对应的实体类。（一个标签对应一个实体类）

3. 编写标签的解析类。（一个标签对应一个解析类）

4. 编写命名空间处理类，(spring的NamespaceHandler）这里我们的是TtdubboNamespaceHandler

5. 编写spring.handlers和spring.schemas串联起所有部件，(spring.handlers和spring.schemas都放在“META-INF”文件夹下）

   基本自定义标签需要实现两个关键接口：NamespaceHandlerSupport，BeanDefinitionParser **spring.handlers文件作用**：具体处理命名空间的handle
   key：是你的命名空间，其他xml中通过引入这个命名空间进行使用该元素描述对象
   value：指向的是你的handle

   **spring.schemas文件作用：**具体处理命名空间的约束
   把具体的约束文件指向本地的META-INF/ttdubbo.xsd，因为好像约束文件默认好像去网上搜索。

   > 实现一个ttdubbo

   1. simpleType，complexType,  simpleContent和complexContent标签

      ```html
      <!-- -----------------simpleType ------------------------>
      <!-- 定义一个简单类型, 用来规定和约束具有纯文本内容的元素(不含子元素即为具有纯文本内容的元素)或属性的值. -->
      <xsd:element name="description" type="xsd:string"/> // 规定一个元素纯文本内容的类型
      <xsd:element name="worker">
      	<xsd:complexType>
      		<xsd:attribute name="id" type="idType"/>
      	</xsd:complexType>
      </xsd:element>
       
      <xsd:simpleType name="idType">
      	<xsd:restriction base="xsd:integer"/>
      </xsd:simpleType> 
      
      <!-- 定义了一个"worker"元素,并规定它具有一个"id"属性,这个属性的类型为"idType", 这是我自定义的一个simpleType,这个simpleType中规定了属性值的类型. -->
      
      <!-- 当然也可以直接将"attribute"的"type"属性设置为"xsd:integer",但我这样写是为了让读者更直观的看出simpleType确实可以规定一个元素属性的数据类型. -->
      
      <!-- -----------------simpleType ------------------------>
      
      <!-- -----------------complexType ------------------------>
      <!--  complexType 元素定义复杂类型。复杂类型的元素是包含其他元素和/或属性的 XML 元素。-->
      
      <xsd:element name="student">
      	<xsd:complexType>
      		<xsd:sequence>
      			<xsd:element name="id" type="xsd:int"></xsd:element>
      			<xsd:element name="name" type="xsd:string"></xsd:element>
      			<xsd:element name="gender" type="xsd:string"></xsd:element>
      			<xsd:element name="addr" type="xsd:string"></xsd:element>
      		</xsd:sequence>
      		<xsd:attribute name="class" type="xsd:string"/>
      	</xsd:complexType>
      </xsd:element>
      <!-- 这个complexType为student元素定义了一个复杂类型, 这个类型中包含了四个子元素:id,name,gender,addr,并且使用了sequence指示器指定了这四个子元素的顺序. 还包含了一个class属性.-->
      
      <!-- -----------------complexType ------------------------>
      ```

### 4.自定义标签如何被spring解析

> https://www.jianshu.com/p/3667a285feba

1. **我们经常说的spring容器到底指什么，以及如何存储我们配置bean属性？** 

   基础容器BeanFactory，扩展容器ApplicationContext，他们解析配置文件的过程基本相同。存储结构为Map<beanName, BeanDefinition>实现为ConcurrentHashMap，也就是说我们最终解析出来的xml文件的会存储到这个数据结构中，这就是我们的容器。

2. **我们解析出配置问文件用哪个对象来接收存储？**

   我们知道在抽象对象世界里everything is object.所以我们将bean抽象成了对象，它的顶层接口就是BeanDefinition,而子类包括了ChildBeanDefinition，RootBeanDefinition ，GenericBeanDefinition，默认文件解析入Map为GenericBeanDefinition.

3. **Spring基础标签有哪些？** 

   Import, alias, bean, beans

4. **Spring配置文件的解析入口在哪？** 

   这里介绍两个重要的接口及其实现类 
   `BeanDefinitionReader`与`BeanDefinitionDocumentReader` 
   对应实现类`XmlBeanDefinitionReader`、`DefaultBeanDefinitionDocumentReader` 

5. **自定义标签的解析过程** 

   ![] (https://img-blog.csdn.net/2018091223374385?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3p6ZzEyMjkwNTk3MzU=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

