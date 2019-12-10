ollection
    + 数组

> Collection.stream()  
> Collection.parallelStream()  
> Arrays.stream(T array) or Stream.of()  

- 流的操作类型
    + **Intermediate**: 一个流可以后面跟随零个或多个 intermediate 操作。其目的主要是打开流，做出某种程度的数据映射/过滤，然后返回一个新的流，交给下一个操作使用。这类操作都是惰性化的（lazy），就是说，仅仅调用到这类方法，并没有真正开始流的遍历
    + **Terminal**: 一个流只能有一个 terminal 操作，当这个操作执行后，流就被使用“光”了，无法再被操作。所以这必定是流的最后一个操作。Terminal 操作的执行，才会真正开始流的遍历，并且会生成一个结果，或者一个 side effect。
    + 还有一种操作被称为 **short-circuiting**.

# 常用操作
## map
map生成的是个 1:1 映射，每个输入元素，都按照规则转换成为另外一个元素

``` java
    List<Book> books = Lists.newArrayList();
    List<Integer> bookIds = books.stream()
                                .map(Book::getId) //从 Book对象流 转换成了 Integer 流
                                .collect(Collectors.toList());
```

## flatMap
和map类似，不同的是其每个元素转换得到的是Stream对象，会把子Stream中的元素压缩到父集合中,是一对多映射关系 

``` java
class Article {
    private List<String> tags;
}

articles.stream()
        .flatMap(article -> article.getTags().stream())
        .collect(Collectors.toSet());
```

## filter
对原始 Stream 进行某项测试，通过测试的元素被留下来生成一个新 Stream  
过滤出符合表达式的元素，而不是过滤掉符合表达式的元素

```java
//留下偶数
Integer[] sixNums = {1, 2, 3, 4, 5, 6};
Integer[] evens = Stream.of(sixNums).filter(n -> n%2 == 0).toArray(Integer[]::new);
```

## forEach
forEach 方法接收一个 Lambda 表达式，然后在 Stream 的每一个元素上执行该表达式

## findFirst
返回 Stream 的第一个元素，或者空,返回值类型：Optional  
findAny、max/min、reduce都是返回Optional

## reduce
主要作用是把 Stream元素组合起来。它提供一个起始值（种子），然后依照运算规则（BinaryOperator），和前面 Stream 的第一个、第二个、第n个元素组合。从这个意义上说，字符串拼接、数值的 sum、min、max、average 都是特殊的 reduce。 
   
BinaryOperator: 表示对同一类型的两个操作数的操作，产生与操作数相同类型的结果。 对于操作数和结果都是相同类型的情况。

```java
// 有起始值，返回值不会为空
Integer sum = integers.reduce(0, (a, b) -> a+b);
Integer sum = integers.reduce(0, Integer::sum);
// 无起始值，返回可能为空，所以返回的是Optional
sumValue = Stream.of(1, 2, 3, 4).reduce(Integer::sum).get();
```
## collect

- `List<Book>` to `List<BookDTO>` 

```java
List<BookDTO> bookDTOs = books.stream().map(this::convert2DTO).collect(Collectors.toList());

// Collectors.toSet() 就返回 Set<BookDTO>

private BookDTO convert2DTO(Book book) {
    // convert Book to BookDTO
}
```

- `List<Book>` to `Map<Integer, Book>` 转成Map

``` java
class Book {
    private Integer id;
    private String name;
}
Map<Integer, Book> bookMap = books.stream().collect(Collectors.toMap(Book::getId, Function.identity()));
```

- `List<Book>` to `Map<Integer, String>` 转成书的ID和书名的Map

``` java
class Book {
    private Integer id;
    private String name;
}
Map<Integer, String> bookIdNameMap = books.stream().collect(Collectors.toMap(Book::getId, Book::getName));
```

- `List<Book>` to `Map<Integer, List<Book>>` 按书的分类分组

``` java
class Book {
    private Integer id;
    private String name;
    private Integer categoryId;
} 

Map<Integer, List<Book>> categoryBooksMap = books.stream().collect(Collectors.groupingBy(Book::getCategoryId));
```

- `List<Book>` to `Map<Integer, List<String>>` 按书的分类分组，并且将分组结果转成书名

``` java
class Book {
    private Integer id;
    private String name;
    private Integer categoryId;
} 

Map<Integer, List<String>> categoryBookNamesMap = books.stream().collect(Collectors.groupingBy(Book::getCategoryId, Collectors.mapping(Book::getName, Collectors.toList())));
```

- `List<Book>` to `Map<Integer, Integer>` 按书的分类分组，并且获得每类书的数量

``` java
class Book {
    private Integer id;
    private String name;
    private Integer categoryId;
}

Map<Integer, List<String>> categoryBookNamesMap = books.stream().collect(Collectors.groupingBy(Book::getCategoryId, Collectors.counting()));
```

# 关于效率
网上有大量流和迭代方式的效率比较，有说高的，有说低的，让人一头雾水的。其实那些实验在设计上就很有问题（比如，业务操作只有一个），搞清楚流的工作原理，就知道为什么人们说他会提高效率了，也知道那些效率上变慢是什么原因了。  

现有一个`List<Book> books`，里面有10本书，我们要对每本书顺序执行三种业务操作`A()`，`B()`，`C()`，假设每个业务操作都耗时1秒。

- 使用迭代方式  
使用迭代方式时，第一本书完全走完`A()`，`B()`，`C()`三个操作，第二本书才开始进行`A()`，`B()`，`C()`，因此总耗时是：`10*(1+1+1)=30秒`

- 使用流  
使用流时，当第一本书走完`A()`，进入`B()`时，第二本书就可以进入`A()`进行检查了，如图
![流的示意图][stream]
所以流的耗时是：`10 + 3 = 13 秒`    

# 简单粗暴的总结
平时开发大多数情况下还是进行对象的处理，所以大家大可以放心食用流，99%场景都不需要担心效率问题。与此同时流的代码/业务表达能力更强，用的好的可以让你的代码/业务流程赏心悦目。  
对了，还能帮助你养成抽方法的习惯🤦‍♂️









