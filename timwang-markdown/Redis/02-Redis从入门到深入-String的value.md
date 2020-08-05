
##### 1. string类型

###### 1.1 redis数据存储类型

- redis本身是一个Map，其中所有的数据都是采用key：value的形式存储
- 数据类型指的是存储的数据的类型，也就是value部分的类型，key部分永远都是字符串

![526216587632c9331e6cfb2c3b7ebb8a.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5qcejzlaj30c609kdfv.jpg)


###### 1.2 string类型

- 存储的数据：单个数据，最简单的数据存储类型，也是最常用的数据存储类型
- 存储数据的格式：一个存储空间保存一个数据
- 存储内容：通常使用字符串，如果字符串以整数形式展示，可以作为数字操作使用

![3ec9cd49df7c959572770d341b642a48.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5qcmhtkdj30cc044a9y.jpg)


###### 1.3 基本操作

- 添加/修改数据
```
set key value
```
- 获取数据
```
get key
```
- 删除数据（删除成功返回1，失败返回0）
```
del key
```

- 添加/修改多个数据
```
mset key1 value1 key2 value2 ...
```
- 获取多个数据
```
mget key1 key2...
```
- 获取数据字符个数（字符串长度）
```
strlen key
```
- 追加信息到原始信息后部（如果原始信息存在就追加，否则新建）
```
append key value
```

###### 1.4 单数据操作与多数据操作选择

(set key value) vs (mset key1 value1 key2 value)

![2c306855e6d4343d081b6d103bec6a1e.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5qdaexdcj30lk07gdfy.jpg)


1. 一条指令的执行过程

请求+处理时间

2. 单指令3条指令的执行过程

总时间= 请求时间*6 + 处理时间*3

3. 多指令3条指令的执行过程

这里的请求时间可能比单指令要烧毁多一点，因为数据量比较大

总时间= (请求时间+)*2 + 处理时间*3


###### 1.5 String类型的扩展操作

业务场景：大型企业级应用中，分表操作是基本操作，使用多张表存储同类型数据，但是对应的主键id必须保证统一性，不能重复。Oracle数据库具有sequence设定，可以解决该问题，但是MySQL数据库并不具有类似的机制，name如何解决？
![e325e984bc9f9d1f5081b3a872f31467.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5qdiu984j31ic0fwt9z.jpg)


1. 解决方案

- 设置数值数据增加指定范围的值

```
incr <key>
incrby <key> <increment>
incrybyfloat <key> <increment>
```

- 设置数值数据减少指定范围的值

```
decr <key>
decr <key> <increment>
```

2. string作为数值操作

- string在redis内部存储默认是一个字符串，当遇到增减类操作incr，decr时会转为数值型进行计算
- redis所有的操作都是原子性的，采用单线程处理所有业务，命令都是一个一个执行的，因此无需考虑并发带来的数据影响
- 注意：按数值进行操作的类型，如果原始数据不能转为数值，或超越了redis数值上限范围，将报错。9223372036854775807 （java中long型数据最大值，Long.MAX_VALUE）


###### 1.6 投票场景

业务场景：“最强女生”启动海选投票，只能通过微信投票，每个微信号每4个小时只能投一票。
电商商家开启热门商品推荐，热门商品不能一直处于热门期，每种商品热门期只能维持3天，3天后自动取消热门。
新闻网站会出现新闻，热点新闻最大的特征是时效性，如何自动控制热点新闻的时效性。

1. 解决方案

- 设置数据具有指定的生命周期

```
setex <key> <seconds> <value>
psetex <key> <milliseconds> <value>
```
2. 要点 

设置时间后面的会覆盖前面一个设置的时间，redis控制数据的生命周期，通过数据是否失效控制业务行为，适用于所有具有时效性限定控制的操作

###### 1.7 高频key访问

主页高频访问信息显示控制，例如新浪微博大V主页显示粉丝数与微博数量

1. 解决方案

- 在redis中为大v用户设定用户信息，以用户主键和属性值作为key，后台设定定时刷新策略即可
    
    eg:user:id:35879232:fans -> 12210947
   eg:user:id:35879232:blogs -> 6164
   eg:user:id:35879232:focuss -> 83
   
- 在redis中以json格式存储大V用户信息，定时刷新（也可以使用hash类型）
   eg：user:id:35879232 -> 
   (id:35879232,name:春晚,fans:1222302,blogs:6104}
   

###### 1.7 key的设置约定

![02de67bf94ae84cae8cb152e9b3e8f36.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5qdoyjm4j30o606wmxd.jpg)


###### 1.8 string类型数据操作的注意事项

- 数据操作不成功的反馈与数据正常操作之间的差异

1. 表示运行结果是否成功（1或者0，1表示成功，0表示失败）
2. 表示运行结果值（3/2/1...）

- 数据未获取到（nil等同于null）
- 数据最大存储量(512MB)
- 数据计算最大返回（java中的long的最大值, Long.MAX_VALUE）








