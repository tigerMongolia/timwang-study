## 一、什么是异常

在使用计算机语言进行项目开发的过程中，即使程序员把代码写得尽善尽美，在系统的运行过程中仍然会遇到一些问题，因为很多问题不是靠代码能够避免的，比如：客户输入数据的格式，读取文件是否存在，网络是否始终保持通畅等等。

异常是程序中的一些错误，但并不是所有的错误都是异常，并且错误有时候是可以避免的。

比如说，你的代码少了一个分号，那么运行出来结果是提示是错误 java.lang.Error；如果你用System.out.println(11/0)，那么你是因为你用0做了除数，会抛出 java.lang.ArithmeticException 的异常。

## 二、异常的分类

**异常发生的原因**有很多，通常包含以下几大类：

- 用户输入了非法数据。
- 要打开的文件不存在。
- 网络通信时连接中断，或者JVM内存溢出。

这些异常有的是因为用户错误引起，有的是程序错误引起的，还有其它一些是因为物理错误引起的。-

要理解Java异常处理是如何工作的，你需要掌握以下**三种类型的异常（Throwable）**：

- **编译时异常**：最具代表的检查性异常是用户错误或问题引起的异常，这是程序员无法预见的。例如要打开一个不存在文件时，一个异常就发生了，这些异常在编译时不能被简单地忽略。
- **运行时异常：** 运行时异常是可能被程序员避免的异常。与检查性异常相反，运行时异常可以在编译时被忽略。
- **错误：** 错误不是异常，而是脱离程序员控制的问题。错误在代码中通常被忽略。例如，当栈溢出时，一个错误就发生了，它们在编译也检查不到的。




1.编译时异常

l是指编译器要求必须处置的异常。即程序在运行时由于外界因素造成的一般性异常。编译器要求java程序必须捕获或声明所有编译时异常。

2.运行时异常

l是指编译器不要求强制处置的异常。一般是指编程时的逻辑错误，是程序员应该积极避免其出现的异常。java.lang.RuntimeException类及它的子类都是运行时异常。

l对于这类异常，可以不作处理，因为这类异常很普遍，若全处理可能会对程序的可读性和运行效率产生影响。

3.Error类

Error类一般是指与虚拟机相关的问题，如系统崩溃，虚拟机错误，内存空间不足，方法调用栈溢等。对于这类错误的导致的应用程序中断，仅靠程序本身无法恢复和预防，遇到这样的错误，建议让程序终止。

l对于这类异常，如果程序不处理，可能会带来意想不到的结果。

![异常分类](E:\熙邻\学习总结\异常\image\异常分类.jpg)



所有的异常类是从 java.lang.Exception 类继承的子类。

Exception 类是 Throwable 类的子类。除了Exception类外，Throwable还有一个子类Error 。

Java 程序通常不捕获错误。错误一般发生在严重故障时，它们在Java程序处理的范畴之外。

Error 用来指示运行时环境发生的错误。

例如，JVM 内存溢出。一般地，程序不会从错误中恢复。



## 三、Java 内置异常类

Java 语言定义了一些异常类在 java.lang 标准包中。

标准运行时异常类的子类是最常见的异常类。由于 java.lang 包是默认加载到所有的 Java 程序的，所以大部分从运行时异常类继承而来的异常都可以直接使用。

Java 根据各个类库也定义了一些其他的异常，下面的表中列出了 Java 的**运行时异常类**。

| **异常**                             | **描述**                                   |
| ---------------------------------- | ---------------------------------------- |
| **ArithmeticException**            | 当出现异常的运算条件时，抛出此异常。例如，一个整数"除以零"时，抛出此类的一个实例。 |
| **ArrayIndexOutOfBoundsException** | 用非法索引访问数组时抛出的异常。如果索引为负或大于等于数组大小，则该索引为非法索引。 |
| ArrayStoreException                | 试图将错误类型的对象存储到一个对象数组时抛出的异常。               |
| **ClassCastException**             | 当试图将对象强制转换为不是实例的子类时，抛出该异常。               |
| IllegalArgumentException           | 抛出的异常表明向方法传递了一个不合法或不正确的参数。               |
| IllegalMonitorStateException       | 抛出的异常表明某一线程已经试图等待对象的监视器，或者试图通知其他正在等待对象的监视器而本身没有指定监视器的线程。 |
| IllegalStateException              | 在非法或不适当的时间调用方法时产生的信号。换句话说，即 Java 环境或 Java 应用程序没有处于请求操作所要求的适当状态下。 |
| IllegalThreadStateException        | 线程没有处于请求操作所要求的适当状态时抛出的异常。                |
| **IndexOutOfBoundsException**      | 指示某排序索引（例如对数组、字符串或向量的排序）超出范围时抛出。         |
| NegativeArraySizeException         | 如果应用程序试图创建大小为负的数组，则抛出该异常。                |
| **NullPointerException**           | 当应用程序试图在需要对象的地方使用 `null` 时，抛出该异常         |
| **NumberFormatException**          | 当应用程序试图将字符串转换成一种数值类型，但该字符串不能转换为适当格式时，抛出该异常。 |
| SecurityException                  | 由安全管理器抛出的异常，指示存在安全侵犯。                    |
| StringIndexOutOfBoundsException    | 此异常由 `String` 方法抛出，指示索引或者为负，或者超出字符串的大小。  |
| UnsupportedOperationException      | 当不支持请求的操作时，抛出该异常。                        |

下面的表中列出了 Java 定义在 java.lang 包中的**编译时异常类**。

| **异常**                     | **描述**                                   |
| -------------------------- | ---------------------------------------- |
| **ClassNotFoundException** | 应用程序试图加载类时，找不到相应的类，抛出该异常。                |
| CloneNotSupportedException | 当调用 `Object` 类中的 `clone` 方法克隆对象，但该对象的类无法实现 `Cloneable` 接口时，抛出该异常。 |
| IllegalAccessException     | 拒绝访问一个类的时候，抛出该异常。                        |
| InstantiationException     | 当试图使用 `Class` 类中的 `newInstance` 方法创建一个类的实例，而指定的类对象因为是一个接口或是一个抽象类而无法实例化时，抛出该异常。 |
| InterruptedException       | 一个线程被另一个线程中断，抛出该异常。                      |
| **NoSuchFieldException**   | 请求的变量不存在                                 |
| **NoSuchMethodException**  | 请求的方法不存在                                 |

------

## 异常方法

下面的列表是 Throwable 类的主要方法:

| **序号** | **方法及说明**                                |
| ------ | ---------------------------------------- |
| 1      | **public String getMessage()**返回关于发生的异常的详细信息。这个消息在Throwable 类的构造函数中初始化了。 |
| 2      | **public Throwable getCause()**返回一个Throwable 对象代表异常原因。 |
| 3      | **public String toString()**使用getMessage()的结果返回类的串级名字。 |
| 4      | **public void printStackTrace()**打印toString()结果和栈层次到System.err，即错误输出流。 |
| 5      | **public StackTraceElement [] getStackTrace()**返回一个包含堆栈层次的数组。下标为0的元素代表栈顶，最后一个元素代表方法调用堆栈的栈底。 |
| 6      | **public Throwable fillInStackTrace()**用当前的调用栈层次填充Throwable 对象栈层次，添加到栈层次任何先前信息中。 |

## 四、常见的异常:  NullPointException

在我们的系统中对于异常的处理并不是特别友好，导致用户体验时候经常会看到一大段异常的栈信息，
对于他们而言就是乱码，所以应该怎么解决这类问题？

为此，我们可以来具体分析一下，异常出现的场景有哪些，以及我们怎样尽量避免这些异常，这里主要说的是运行时异常：

#### **什么场景会出现：**

​                            调用null对象的方法。
​                            访问或修改null对象的域。
​                            如果null是一个数组，并获取null的长度。
​                            如果null对象是一个对象数组，并访问会修改null对象的子元素
​                            试图对null对象同步。 

#### 开发人员怎样避免：

​        **1. 带有字面值的字符串比较**

​        在应用程序代码中一种常见的情况是将字符串变量与字面值进行比较。字符串字面值可能是字符串或枚举元素。我们将通过使用字面值来调用方法，而不是通过使用null对象来调用方法。例如，观察下面的例子：

```java
String str = null;
if(str.equals("Test")) {
     /* The code here will not be reached, as an exception will be thrown. */
}
```

​        上面的代码片段会抛出NullPointerException。然后，如果通过字面值来调用方法，程序会正常执行。

```java
String str = null;
if("Test".equals(str)) {
     /* Correct use case. No exception will be thrown. */
}
```

​          **2.检查一个方法的参数****(入参非空检查)**

​        在执行方法之前，确保检查了参数是否为null.当参数被适当检查后，方法会继续执行。否则，你可以抛出IllegalArgumentException并且通知调用方法传入的参数有误。

```java
public static int getLength(String s) {
     if (s == null)
          throw new IllegalArgumentException("The argument cannot be null");
     return s.length();
}
```

​         **3 优先使用String.valueOf()而不是toString()** 

​       当你的应用程序代码如要一个对象的字符串来描述时，避免使用对象的toString方法。如果你的对象的引用为null，NullPointerException将会被抛出。反之，考虑使用静态方法String.valueOf()，该方法不会抛出任何异常并且在函数参数为null的情况下会打印null。

​         **4. 使用三元运算符**
​        三元运算符能帮助我们避免NullPointerException.运算符具有这样的形式：

​        boolean expression ? value1 : value2;

​       三元运算符能帮助我们避免NullPointerException.运算符具有这样的形式：首先，计算布尔表达式，如果表达式为true,value1被返回，否则value2被返回。我们能使用三元运算符来处理null指针，例如：

​       String message = (str == null) ? "" : str.substring(0, 10);

​       变量message将为空，如果str的引用为null，否则，如果str指向实际的数据，message将获取str的前10个字符。

​	 **5.创建返回空集合而不是null的方法**

​       一种非常好的技术是创建一个返回空集合的方法，而不是返回null值。你的应用程序代码可以迭代空集合并使用它的方法和域，而不会抛出NullPointerException。例如:       

```java
public class Example {
   private static List<Integer> numbers = null;
     public static List<Integer> getList() {
          if (numbers == null)
               return Collections.emptyList();
          else
               return numbers;
     }
}
```

​         

​         **6.利用**[Apache](https://www.baidu.com/s?wd=Apache&tn=24004469_oem_dg&rsv_dl=gh_pl_sl_csd)**的 StringUtils类**

​         Apache的Commons Lang是一个库，为java.lang API提供了帮助工具，例如字符串操作方法。StringUtils.java提供了字符串的操作，该类处理字符串对象为null的情况。你可以使用StringUtils.isNotEmpty, StringUtils.IsEmpty 及 StringUtils.equals 方法，以避免NullPointerException。

​          **7.使用contains(), containsKey(), containsValue() 方法**

​         如果你的程序使用了像[Map](https://www.baidu.com/s?wd=Map&tn=24004469_oem_dg&rsv_dl=gh_pl_sl_csd)s这样的集合，考虑使用contains(), containsKey(), containsValue()方法。例如，在验证某些键存在与Map中时，返回特定键的值。

```java
Map<String, String> map = …
…
String key = …
String value = map.get(key);
System.out.println(value.toString()); // An exception will be thrown, if the value is null.
```

​       在上面的片段中，我们不检查键是否存在与Map中，返回值可能为null.最安全的方式是：

```java
Map<String, String> map = …
…
String key = …
if(map.containsKey(key)) {
     String value = map.get(key);
     System.out.println(value.toString()); // No exception will be thrown.
}
```

​        **8.检查外部方法的返回值**

​      实际环境中，使用外部的库很常见。这些库包含返回某个引用的方法。确保返回的引用不为null.阅读javadoc的方法，以更好理解函数功能与返回值。

#### 存在NullPointerException的安全方法     

​       **1.访问静态成员或类方法**
​        当你的代码试图访问静态变量或一个类的方法，即使对象的引用等于null,JVM也不会抛出NullPointerException.这是因为，在编过程中,Java编译器存储静态方法和域在特殊的位置。静态方法和域不与对象关联，而是与类名关联。

​        例如下面的代码不会抛出NullPointerException.

```java
class SampleClass {
     public static void printMessage() {
          System.out.println("Hello from Java Code Geeks!");
     }
}
public class TestStatic {
     public static void main(String[] args) {
          SampleClass sc = null;
          sc.printMessage();
     }
}
```

​        注意，尽管SampleClass的实例为null，方法还是会被执行。当方法或域为静态时，应该以“静态”的方式来访问，即通过类名来访问。例如：SampleClass.printMessage() 

​     **2.instanceof 操作符**

​        即使对象的引用为null,instanceof操作符可使用。当引用为null时，instanceof 操作符返回false,而且不会抛出NullPointerException.例如，下面的代码：

```java
String str = null;
if(str instanceof String)
     System.out.println("It's an instance of the String class!");
else
     System.out.println("Not an instance of the String class!");
```

结果如下：

Not an instance of the String class!



## 五、怎么处理异常		    

#### Java提供的是异常处理的抓抛模型。

**1."抛"：**当我们执行代码时，一旦出现异常，就会在异常代码处生成一个对应的异常类型的对象，并将此对象抛出。该异常对象将被提交给Java运行时系统，这个过程称为抛出(throw)异常。

异常对象的生成:

由虚拟机自动生成：程序运行过程中，虚拟机检测到程序发生了问题，如果在当前代码中没有找到相应的处理程序，就会在后台自动创建一个对应异常类的实例对象并抛出——自动抛出

**2."抓":**抓住上一步跑出来的异常类对象，如何抓？即为异常的处理方式。

### **处理方式一：**try{}catch(){}finally模型

```java
try{
  ......  //可能产生异常的代码
}
catch( ExceptionName1 e ){
  ......  //当产生ExceptionName1型异常时的处置措施
}
catch( ExceptionName2 e ){
......   //当产生ExceptionName2型异常时的处置措施
} 
[ finally{
......   //无论是否发生异常，都无条件执行的语句
  }  ]

```

关键字解释：

(1) try:

- 捕获异常的第一步是用try{…}语句块选定捕获异常的范围，将可能出现异常的代码放在try语句块中。

(2) catch:

- 在catch语句块中是对异常对象进行处理的代码。每个try语句块可以伴随一个或多个catch语句，用于处理可能产生的不同类型的异常对象。


- **如果明确知道产生的是何种异常，可以用该异常类作为catch的参数；也可以用其父类作为catch的参数。**

​       **比如：可以用ArithmeticException类作为参数的地方，就可以用RuntimeException类作为参数，或者用所有异常的父类Exception类作为参数。但不能是与ArithmeticException类无关的异常，如NullPointerException（catch中的语句将不会执行）**

（3） finally

- 捕获异常的最后一步是通过finally语句为异常处理提供一个统一的出口，使得在控制流转到程序的其它部分以前，能够对程序的状态作统一的管理。


- 不论在try代码块中是否发生了异常事件，catch语句是否执行，catch语句是否有异常，catch语句中是否有return，finally块中的语句都会被执行。


- finally语句和catch语句是任选的

**不捕获异常时的情况**

- 前面使用的异常都是RuntimeException类或是它的子类，这些类的异常的特点是：

  即使没有使用try和catch捕获，Java自己也能捕获，并且编译通过  ( 但运行时会发生异常使得程序运行终止)。

- 如果抛出的异常是IOException等类型的非运行时异常，则必须捕获，否则编译错误。也就是说，我们必须处理编译时异常，将异常进行捕捉，转化为运行时异常

### **处理方式二：**声明抛出异常是Java中处理异常的第二种方式

- 如果一个方法(中的语句执行时)可能生成某种异常，但是并不能确定如何处理这种异常，则此方法应显示地声明抛出异常，表明该方法将不对这些异常进行处理，而由该方法的调用者负责处理。


- 在方法声明中用throws语句可以声明抛出异常的列表，throws后面的异常类型可以是方法中产生的异常类型，也可以是它的父类。

  ​

  **重写方法声明抛出异常的原则**：

重写方法不能抛出比被重写方法范围更大的异常类型。在多态的情况下，对methodA()方法的调用-异常的捕获按父类声明的异常处理。

```
public class A {
 	public void methodA() throws IOException {
 	      ……
 	}  }
    public class B1 extends A {
 	public void methodA() throws FileNotFoundException {
 	      ……
 	}  }
    public class B2 extends A {
 	public void methodA() throws Exception {   //报错
 	        ……
  }  }

```

## 声明自定义异常

在 Java 中你可以自定义异常。编写自己的异常类时需要记住下面的几点。

- 所有异常都必须是 Throwable 的子类。
- 如果希望写一个检查性异常类，则需要继承 Exception 类。
- 如果你想写一个运行时异常类，那么需要继承 RuntimeException 类。