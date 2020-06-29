#### 一、几个问题

1. Spring中的bean是如何生成？
2. Spring提供了哪些扩展点可以整合第三方框架
3. Spring如何整合Mybatis的

#### 二、什么是Spring

Spring管理bean（Java中的对象），初始化的操作，



#### 三、Spring IoC容器

1. **资源组件：**

   Resource，对资源文件的描述，不同资源文件如xml、properties文件等，格式不同，最终都将被ResourceLoader加载获得相应的Resource对象；个人理解是。主配置文件.主配置作为所有配置的入口 

2. **资源加载组件：**

   ResourceLoader：加载xml、properties等各类格式文件，解析文件，并生成Resource对象。加载哪些解析过的配置资源

3. **Bean容器组件：**

   BeanFactory体系：IoC容器的核心，其他组件都是为它工作的（但不是仅仅为其服务）.核心

4. **Bean注册组件：**

   SingletonBeanRegister/AliasRegister：将BeanDefinition对象注册到BeanFactory（BeanDefinition Map）中去。注册对象所使用的容器

5. **Bean描述组件：**

   BeanDefinition体系，Spring内部对Bean描述的基本数据结构。负责描述BeanDefinition资源  将资源形式的bean转化为spring所期望的格式结构

6. **Bean构造组件：**

   BeanDefinitionReader体系，读取Resource并将其数据转换成一个个BeanDefinition对象。负责将一个个的资源 解析转化为BeanDefinition  为之后描述bean做准备

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gg9jtxriloj31330mi0w2.jpg" alt="image-20200205095809050" style="zoom:50%;float:left" />



#### 四、BeanFactory

##### 4.1 准备

- `bean-v1.xml`配置`bean`的信息
- `BeanDefinition`用于存放`bean`的定义
- `BeanFactory`获取bean`的信息，实例化`bean`
- `BeanFactoryTest`测试`BeanFactory`是否可用

##### 4.2 代码实现

1. bean-v1.xml

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans>
       <bean id="user" class="com.timwang.spring.small.User"/>
       <bean id="invalidBean" class="xxx.xxx"/>
   </beans>	
   ```

2. BeanDefinition

   `bean-v1.xml`中定义了每个`bean`，但这些信息我们该如何存储呢？ `spring`是通过`BeanDefinition`接口来描述`bean`的定义

   ```java
   package com.timwang.spring.small;
   
   /**
    * @author wangjun
    * @date 2020-06-29
    */
   public interface BeanDefinition {
       /**
        * 获取bean.xml中 bean的全名 如 "com.niocoder.service.v1.NioCoderService"
        * @return string
        */
       String getBeanClassName();
   }
   ```

   `GenericBeanDefinition`实现了`BeanDefinition`接口

   ```java
   package com.timwang.spring.small;
   
   /**
    * @author wangjun
    * @date 2020-06-29
    */
   public class GenericBeanDefinition implements BeanDefinition {
       private String id;
       private String beanClassName;
   
       public GenericBeanDefinition(String id, String beanClassName) {
           this.id = id;
           this.beanClassName = beanClassName;
       }
   
       @Override
       public String getBeanClassName() {
           return this.beanClassName;
       }
   
       public String getId() {
           return id;
       }
   }
   
   ```

3. BeanFactory

   我们已经使用`BeanDefinition`来描述`bean-v1.xml`的`bean`的定义,下面我们使用`BeanFactory`来获取`bean`的实例

   ```java
   package com.timwang.spring.small;
   
   /**
    * 创建bean的实例
    * @author wangjun
    * @date 2020-06-29
    */
   public interface BeanFactory {
       /**
        * 获取bean的定义
        * @param beanId beanId
        * @return BeanDefinition
        */
       BeanDefinition getBeanDefinition(String beanId);
   
       /**
        * 获取bean的实例
        * @param beanId beanId
        * @return bean示例
        */
       Object getBean(String beanId);
   }
   
   ```

   `DefaultBeanFactory`实现了`BeanFactory`接口

   ```java
   package com.timwang.spring.small;
   
   import org.dom4j.Document;
   import org.dom4j.Element;
   import org.springframework.beans.factory.BeanCreationException;
   import org.springframework.beans.factory.BeanDefinitionStoreException;
   import org.springframework.util.ClassUtils;
   
   import java.io.InputStream;
   import java.util.Iterator;
   import java.util.Map;
   import java.util.concurrent.ConcurrentHashMap;
   
   import org.dom4j.io.SAXReader;
   
   /**
    * BeanFactory的默认实现类
    *
    * @author wangjun
    * @date 2020-06-29
    */
   public class DefaultBeanFactory implements BeanFactory {
       private static final String ID_ATTRIBUTE = "id";
       private static final String CLASS_ATTRIBUTE = "class";
       /**
        * 存放BeanDefinition
        */
       private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
   
       public DefaultBeanFactory(String configFile) {
           loadBeanDefinition(configFile);
       }
   
       @Override
       public BeanDefinition getBeanDefinition(String beanId) {
           return beanDefinitionMap.get(beanId);
       }
   
       @Override
       public Object getBean(String beanId) {
           BeanDefinition bd = this.getBeanDefinition(beanId);
           if (bd == null) {
               throw new BeanCreationException("BeanDefinition does not exist");
           }
           ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
           String beanClassName =  bd.getBeanClassName();
           try {
               // 使用反射创建bean的实例，需要对象存在默认的无参构造方法
               Class<?> aClass = classLoader.loadClass(beanClassName);
               return aClass.newInstance();
           } catch (Exception e) {
               throw new BeanCreationException("Bean Definition does not exist");
           }
       }
   
       /**
        * 具体解析bean.xml的方法 使用dom4j
        * @param configFile configFile
        */
       private void loadBeanDefinition(String configFile) {
           ClassLoader cl = ClassUtils.getDefaultClassLoader();
           try (InputStream is = cl.getResourceAsStream(configFile)) {
               SAXReader reader = new SAXReader();
               Document doc = reader.read(is);
   
               Element root = doc.getRootElement();
               Iterator elementIterator = root.elementIterator();
               while (elementIterator.hasNext()) {
                   Element ele = (Element) elementIterator.next();
                   String id = ele.attributeValue(ID_ATTRIBUTE);
                   String beanClassName = ele.attributeValue(CLASS_ATTRIBUTE);
                   BeanDefinition bd = new GenericBeanDefinition(id, beanClassName);
                   this.beanDefinitionMap.put(id, bd);
               }
           } catch (Exception e) {
               throw new BeanDefinitionStoreException("IOException parsing XML document", e);
           }
       }
   }
   
   ```

4. BeanFactoryTest

   ```java
   package com.timwang.spring.small;
   
   /**
    * @author wangjun
    * @date 2020-06-29
    */
   public class BeanFactoryTest {
       public static void main(String[] args) {
           BeanFactoryTest beanFactoryTest = new BeanFactoryTest();
           beanFactoryTest.testGetBean();
       }
   
       public void testGetBean() {
           BeanFactory beanFactory = new DefaultBeanFactory("bean-v1.xml");
           BeanDefinition bd = beanFactory.getBeanDefinition("user");
           String beanClassName = bd.getBeanClassName();
           System.out.println(beanClassName);
       }
   }
   
   ```

   