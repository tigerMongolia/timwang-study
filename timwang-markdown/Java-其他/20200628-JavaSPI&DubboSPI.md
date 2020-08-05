####  一、SPI简介

> SPI 全称为 (Service Provider Interface) ，是JDK内置的一种服务提供发现机制。 目前有不少框架用它来做服务的扩展发现， 简单来说，它就是一种动态替换发现的机制， 举个例子来说， 有个接口，想运行时动态的给它添加实现，你只需要添加一个实现，而后，把新加的实现，描述给JDK知道就行啦（通过改一个文本文件即可）我们经常遇到的就是java.sql.Driver接口，其他不同厂商可以针对同一接口做出不同的实现，mysql和postgresql都有不同的实现提供给用户，而Java的SPI机制可以为某个接口寻找服务实现

SPI 机制的思想来源正是：开闭原则，对扩展开放，对修改关闭。对于一个特定的程序，如 Java 的 java.sql.Driver，就是一个接口，Mysql 的 Driver 实现就是通过 SPI 机制被程序加载的。不难想到，其他 Driver 的实现也可以依靠 SPI 机制去实现。

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gg8d7uxz52j30gq037dfp.jpg" alt="image-20200205095809050" style="zoom:80%;float:left" />

类图中，接口对应定义的抽象SPI接口；实现方实现SPI接口；调用方依赖SPI接口。

SPI接口的定义在调用方，在概念上更依赖调用方；组织上位于调用方所在的包中；实现位于独立的包中。

当接口属于实现方的情况，实现方提供了接口和实现，这个用法很常见，属于API调用。我们可以引用接口来达到调用某实现类的功能。

#### 二、Java SPI

当服务的提供者提供了一种接口的实现之后，需要在classpath下的META-INF/services/目录里创建一个以服务接口命名的文件，这个文件里的内容就是这个接口的具体的实现类。当其他的程序需要这个服务的时候，就可以通过查找这个jar包（一般都是以jar包做依赖）的META-INF/services/中的配置文件，配置文件中有接口的具体实现类名，可以根据这个类名进行加载实例化，就可以使用该服务了。JDK中查找服务实现的工具类是：java.util.ServiceLoader。

##### 2.1 SPI接口

```java
package tim.wang.sourcecode.spi;

/**
 * 定义一个接口和多个实现
 * @author wangjun
 * @date 2020-06-28
 */
public interface Robot {
    void sayHello();
}
```

定义了一个机器人接口，有一个sayHello方法

##### 2.2 SPI具体实现

```java
package tim.wang.sourcecode.spi;

/**
 * 擎天柱实现
 * @author wangjun
 * @date 2020-06-28
 */
public class OptimusPrime implements Robot{
    @Override
    public void sayHello() {
        System.out.println("hello, I am Optimus Prime.");
    }
}

package tim.wang.sourcecode.spi;

/**
 * 大黄蜂 实现
 * @author wangjun
 * @date 2020-06-28
 */

public class Bumblebee implements Robot{
    @Override
    public void sayHello() {
        System.out.println("Hello, I am Bumblebee");
    }
}

```

#####2.3 增加META-INF目录文件

Resource下面创建META-INF/services 目录里创建一个以服务接口命名的文件

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1gg8dsr8ufmj30g603idfp.jpg" alt="image-20200205095809050" style="zoom:80%;float:left" />

```properties
tim.wang.sourcecode.spi.Bumblebee
tim.wang.sourcecode.spi.OptimusPrime
```

##### 2.4 增加RobotService

```java
package tim.wang.sourcecode.spi;

import java.util.Optional;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

/**
 * @author wangjun
 * @date 2020-06-28
 */
public class RobotService {
    public Robot getRobot() {
        ServiceLoader<Robot> serializers = ServiceLoader.load(Robot.class);
        final Optional<Robot> serializer = StreamSupport.stream(serializers.spliterator(), false)
                .findFirst();
        return serializer.orElse(new OptimusPrime());
    }

    public static void main(String[] args) {
        RobotService robotService = new RobotService();
        Robot robot = robotService.getRobot();
        robot.sayHello();
    }
}
```

##### 2.5 SPI的用途

数据库DriverManager、Spring、ConfigurableBeanFactory等都用到了SPI机制，这里以数据库DriverManager为例，看一下其实现的内幕。

DriverManager是jdbc里管理和注册不同数据库driver的工具类。针对一个数据库，可能会存在着不同的数据库驱动实现。我们在使用特定的驱动实现时，不希望修改现有的代码，而希望通过一个简单的配置就可以达到效果。 在使用mysql驱动的时候，会有一个疑问，DriverManager是怎么获得某确定驱动类的？我们在运用Class.forName("com.mysql.jdbc.Driver")加载mysql驱动后，就会执行其中的静态代码把driver注册到DriverManager中，以便后续的使用。

在JDBC4.0之前，连接数据库的时候，通常会用`Class.forName("com.mysql.jdbc.Driver")`这句先加载数据库相关的驱动，然后再进行获取连接等的操作。而JDBC4.0之后不需要`Class.forName`来加载驱动，直接获取连接即可，这里使用了Java的SPI扩展机制来实现。

在java中定义了接口java.sql.Driver，并没有具体的实现，具体的实现都是由不同厂商来提供的。

##### 2.6 Mysql DriverManager实现

我们怎么去确定使用哪个数据库连接的驱动呢？这里就涉及到使用Java的SPI扩展机制来查找相关驱动的东西了，关于驱动的查找其实都在DriverManager中，DriverManager是Java中的实现，用来获取数据库连接，在DriverManager中有一个静态代码块如下：

```java
static {
	loadInitialDrivers();
	println("JDBC DriverManager initialized");
}
```

可以看到其内部的静态代码块中有一个`loadInitialDrivers`方法，`loadInitialDrivers`用法用到了上文提到的spi工具类`ServiceLoader`:

```java
    public Void run() {

        ServiceLoader<Driver> loadedDrivers = ServiceLoader.load(Driver.class);
        Iterator<Driver> driversIterator = loadedDrivers.iterator();

        /* Load these drivers, so that they can be instantiated.
         * It may be the case that the driver class may not be there
         * i.e. there may be a packaged driver with the service class
         * as implementation of java.sql.Driver but the actual class
         * may be missing. In that case a java.util.ServiceConfigurationError
         * will be thrown at runtime by the VM trying to locate
         * and load the service.
         *
         * Adding a try catch block to catch those runtime errors
         * if driver not available in classpath but it's
         * packaged as service and that service is there in classpath.
         */
        try{
            while(driversIterator.hasNext()) {
                driversIterator.next();
            }
        } catch(Throwable t) {
        // Do nothing
        }
        return null;
    }

```

遍历使用SPI获取到的具体实现，实例化各个实现类。在遍历的时候，首先调用`driversIterator.hasNext()`方法，这里会搜索classpath下以及jar包中所有的META-INF/services目录下的java.sql.Driver文件，并找到文件中的实现类的名字，此时并没有实例化具体的实现类。

##### 2.7 SPI解决的问题场景描述

在我们设计一套API供别人调用的时候，如果**同一个功能**的要求特别多，或者同一个接口要面对很复杂的业务场景，这个时候我们该怎么办呢？

- 其一：我们可以规范不同的系统调用，也就是传递一个系统标识；
- 其二：我们在内部编码的时候可以使用不同的条件判断语句进行处理；
- 其三：我们可以写几个策略类来来应对这个复杂的业务逻辑，比如同一个功能的实现，A实现类与B实现类的逻辑一点也不一样，但是目标是一样的，这个时候使用策略类是毋庸置疑的？

#### 三、Spring SPI

Spring中使用的类是SpringFactoriesLoader，在org.springframework.core.io.support包中

1. SpringFactoriesLoader 会扫描 classpath 中的 META-INF/spring.factories文件。
2. SpringFactoriesLoader 会加载并实例化 META-INF/spring.factories 中的制定类型
3. META-INF/spring.factories 内容必须是 properties 的Key-Value形式，多值以逗号隔开。

##### 3.1 使用

和 SPI 不同，由于 SpringFactoriesLoader 中的配置文件格式是 `properties` 文件，因此，不需要要像 SPI 中那样为每个服务都创建一个文件， 而是选择直接把所有服务都扔到 `META-INF/spring.factories` 文件中。

```xml
com.fsx.serviceloader.IService=com.fsx.serviceloader.HDFSService,com.fsx.serviceloader.LocalService

// 若有非常多个需要换行 可以这么写
// 前面是否顶头没关系（Spring在4.x版本修复了这个bug）
com.fsx.serviceloader.IService=\
    com.fsx.serviceloader.HDFSService,\
    com.fsx.serviceloader.LocalService
```

```java
 public static void main(String[] args) throws IOException {
        List<IService> services = SpringFactoriesLoader.loadFactories(IService.class, Main.class.getClassLoader());
        List<String> list = SpringFactoriesLoader.loadFactoryNames(IService.class, Main.class.getClassLoader());
        System.out.println(list); //[com.fsx.serviceloader.HDFSService, com.fsx.serviceloader.LocalService]
        System.out.println(services); //[com.fsx.serviceloader.HDFSService@794cb805, com.fsx.serviceloader.LocalService@4b5a5ed1]
    }
```

使用细节：

- `spring.factories`内容的key**不只能是接口**，也可以是抽象类、具体的类。但是有个原则：`=`后面必须是key的实现类（子类）
- key还可以是注解，比如`SpringBoot`中的的key：`org.springframework.boot.autoconfigure.EnableAutoConfiguration`，它就是一个注解
- 文件的格式需要保证正确，否则会返回`[]`（不会报错）
- `=`右边必须不是抽象类，必须能够实例化。且有空的构造函数~
- `loadFactories`依赖方法`loadFactoryNames`。`loadFactoryNames`方法只拿全类名，`loadFactories`拿到全类名后会立马实例化
- **此处特别注意**：**`loadFactories`实例化完成所有实例后，会调用`AnnotationAwareOrderComparator.sort(result)`排序，所以它是支持`Ordered`接口排序的，这个特点特别的重要。**

##### 3.2 原理剖析

因为Spring的这个配置文件和上面的不一样，它的名字是固定的`spring.factories`，里面的内容是key-value形式，因此一个文件里可以定义N多个键值对。**我认为它比源生JDK的SPI是更加灵活些的~**

它主要暴露了两个方法：`loadFactories`和`loadFactoryNames`

```java
// @since 3.2
public final class SpringFactoriesLoader {
	...
	public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/spring.factories";
	...
	// 核心方法如下：
	private static Map<String, List<String>> loadSpringFactories(@Nullable ClassLoader classLoader) {
		MultiValueMap<String, String> result = cache.get(classLoader);
		if (result != null) {
			return result;
		}

		try {
			// 读取到资源文件，遍历
			Enumeration<URL> urls = (classLoader != null ? classLoader.getResources(FACTORIES_RESOURCE_LOCATION) : ClassLoader.getSystemResources(FACTORIES_RESOURCE_LOCATION));
			result = new LinkedMultiValueMap<>();

			
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				// 此处使用的是URLResource把这个资源读进来~~~
				UrlResource resource = new UrlResource(url);
				
				// 可以看到，最终它使用的还是PropertiesLoaderUtils，只能使键值对的形式哦~~~ 当然xml也是被支持的
				
				Properties properties = PropertiesLoaderUtils.loadProperties(resource);
				for (Map.Entry<?, ?> entry : properties.entrySet()) {
					String factoryClassName = ((String) entry.getKey()).trim();
		
					// 使用逗号,分隔成数组，遍历   名称就出来了~~~
					for (String factoryName : StringUtils.commaDelimitedListToStringArray((String) entry.getValue())) {
						result.add(factoryClassName, factoryName.trim());
					}
				}
			}
			cache.put(classLoader, result);
			return result;
		} catch (IOException ex) {
			throw new IllegalArgumentException("Unable to load factories from location [" +
					FACTORIES_RESOURCE_LOCATION + "]", ex);
		}
	}
}
```

#### 四、Spring-Boot-Starter

SpringBoot的 **@SpringBootApplication** 注解，里面还有一个 **@EnableAutoConfiguration** 注解，开启自动配置的。

可以发现这个自动配置注解在另一个工程，而这个工程里也有个**spring.factories**文件，如下图：

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1ggo0tmvup6j30d80c3q3j.jpg" alt="image-20200205095809050" style="zoom:80%;float:left" />

我们知道在SpringBoot的目录下有个spring.factories文件，里面都是一些接口的具体实现类，可以由SpringFactoriesLoader加载。

那么同样的道理，spring-boot-autoconfigure模块也能动态加载了。看一下其中的内容：

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1ggo1gm8smej30le0gyq4p.jpg" alt="image-20200205095809050" style="zoom:80%;float:left" />

为了证明这一点，我们看一下 **dubbo-spring-boot-starter** 的结构

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1ggo1gm8smej30le0gyq4p.jpg" alt="image-20200205095809050" style="zoom:80%;float:left" />

它的关键在于引入了一个 autoconfigure 依赖。这个里面配置了Spring的 **spring.factories** **其中只有一个配置**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.demo</groupId>
    <artifactId>custom-spring-boot-autoconfigure</artifactId>
    <version>1.0-SNAPSHOT</version>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-autoconfigure</artifactId>
            <version>2.1.6.RELEASE</version>
        </dependency>
    </dependencies>

</project>
```

```java
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(name = "org.springframework.web.servlet.DispatcherServlet")
public class CustomAutoConfiguration {

    @Bean
    public CustomConfig customConfig(){
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!第三方自定义的配置");
        return new CustomConfig();
    }
}
```

```java
/**
 * 自定义配置
 */
public class CustomConfig {

    private String value = "Default";
}
```

**spring.factories**

```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=com.demo.CustomAutoConfiguration
```

#### 五、Dubbo SPI

我们首先通过 ExtensionLoader 的 getExtensionLoader 方法获取一个 ExtensionLoader 实例，然后再通过 ExtensionLoader 的 getExtension 方法获取拓展类对象。这其中，getExtensionLoader 方法用于从缓存中获取与拓展类对应的 ExtensionLoader，若缓存未命中，则创建一个新的实例。该方法的逻辑比较简单，本章就不进行分析了。下面我们从 ExtensionLoader 的 getExtension 方法作为入口，对拓展类对象的获取过程进行详细的分析。

- 对Dubbo进行扩展，不需要改动Dubbo的源码
- 自定义的Dubbo的扩展点实现，是一个普通的Java类，Dubbo没有引入任何Dubbo特有的元素，对代码侵入性几乎为零。
- 将扩展注册到Dubbo中，只需要在ClassPath中添加配置文件。使用简单。而且不会对现有代码造成影响。符合开闭原则。
- dubbo的扩展机制设计默认值：@SPI("dubbo") 代表默认的spi对象
- Dubbo的扩展机制支持IoC,AoP等高级功能
- Dubbo的扩展机制能很好的支持第三方IoC容器，默认支持Spring Bean，可自己扩展来支持其他容器，比如Google的Guice。
- 切换扩展点的实现，只需要在配置文件中修改具体的实现，不需要改代码。使用方便。

#### 六、ServiceLoader类分析

ServiceLoader.class是一个工具类,根据META-INF/services/xxxInterfaceName下面的文件名,加载具体的实现类.

从load(Search.class)进去,我们来扒一下这个类,下面主要是贴代码,分析都在代码注释内.

1. 可以看到,里面并没有很多逻辑,主要逻辑都交给了LazyIterator这类

```java
 /*
 *入口, 获取一下当前类的类加载器,然后调用下一个静态方法
 */
 public static <S> ServiceLoader<S> load(Class<S> service) {
     ClassLoader cl = Thread.currentThread().getContextClassLoader();
     return ServiceLoader.load(service, cl);
 }
 /*
 *这个也没有什么逻辑,直接调用构造方法
 */
 public static <S> ServiceLoader<S> load(Class<S> service, ClassLoader loader)
 {
     return new ServiceLoader<>(service, loader);
 }
 /**
 * 也没有什么逻辑,直接调用reload
 */
 private ServiceLoader(Class<S> svc, ClassLoader cl) {
     service = Objects.requireNonNull(svc, "Service interface cannot be null");
     loader = (cl == null) ? ClassLoader.getSystemClassLoader() : cl;
     acc = (System.getSecurityManager() != null) ? AccessController.getContext() : null;
     reload();
 }
 /**
 * 直接实例化一个懒加载的迭代器
 */
 public void reload() {
     providers.clear();
     lookupIterator = new LazyIterator(service, loader);
 }

```

2. LazyIterator这个迭代器只需要关心hasNext()和next(), hasNext()里面又只是单纯地调用hasNextService(). 不用说, next()里面肯定也只是单纯地调用了nextService();

```java
 private boolean hasNextService() {
     if (nextName != null) {
         // nextName不为空,说明加载过了,而且服务不为空 
         return true;
     }
     // configs就是所有名字为PREFIX + service.getName()的资源
     if (configs == null) {
         try {
             // PREFIX是 /META-INF/services
             // service.getName() 是接口的全限定名称
             String fullName = PREFIX + service.getName();
             // loader == null, 说明是bootstrap类加载器
             if (loader == null)
                 configs = ClassLoader.getSystemResources(fullName);
             else
                 // 通过名字加载所有文件资源
                 configs = loader.getResources(fullName);
             } catch (IOException x) {
                 fail(service, "Error locating configuration files", x);
             }
     }
     //遍历所有的资源,pending用于存放加载到的实现类
     while ((pending == null) || !pending.hasNext()) {
             if (!configs.hasMoreElements()) {
                 //遍历完所有的文件了,直接返回
                 return false;
             }
             
             // parse方法主要调用了parseLine,功能:
             // 1. 分析每个PREFIX + service.getName() 目录下面的所有文件
             // 2. 判断每个文件是否是合法的java类的全限定名称,如果是就add到pending变量中
             pending = parse(service, configs.nextElement());
     }
     // 除了第一次进来,后面每次调用都是直接到这一步了
     nextName = pending.next();
     return true;
 }

```

3. 再来看看nextService干了啥

```java
 private S nextService() {
     // 校验一下
     if (!hasNextService())
             throw new NoSuchElementException();
     String cn = nextName;
     nextName = null;
     Class<?> c = null;
     try {
         // 尝试一下是否能加载该类
         c = Class.forName(cn, false, loader);
     } catch (ClassNotFoundException x) {
         fail(service,"Provider " + cn + " not found");
     }
     // 是不是service的子类,或者同一个类
     if (!service.isAssignableFrom(c)) {
         fail(service,"Provider " + cn  + " not a subtype");
     }
     try {
         // 实例化这个类, 然后向上转一下
         S p = service.cast(c.newInstance());
         // 缓存起来,避免重复加载
         providers.put(cn, p);
         return p;
     } catch (Throwable x) {
         fail(service,"Provider " + cn + " could not be instantiated",x);
     }
     throw new Error();          // This cannot happen
 }

```

https://cloud.tencent.com/developer/article/1497777

https://juejin.im/post/5d9998d5f265da5bab5bc0f2#heading-5

https://www.cnblogs.com/itplay/p/9927892.html

https://www.cnblogs.com/LUA123/p/12460869.html

https://dubbo.apache.org/zh-cn/docs/source_code_guide/dubbo-spi.html

https://www.cnblogs.com/GrimMjx/p/10970643.html

https://my.oschina.net/j4love/blog/1813040