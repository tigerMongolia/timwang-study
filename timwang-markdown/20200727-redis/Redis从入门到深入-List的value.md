##### 1. list类型

###### 1.1 List类型

- 数据存储需求：存储多个数据，并对数据进入存储空间的顺序进行区分
- 需要的存储结构：一个存储空间保存多个数据，且通过数据可以体现进入顺序
- list类型：保存多个数据，底层使用双向链表存储结构实现
 ![d9e8a72f1eda0cc8714a053b458e57e2.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5pi6dp6qj30t806i0sy.jpg)
 ![18a46fbc649d28b3e7f8607908f47664.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5pihqof0j30pg0as0sq.jpg)
 
###### 1.2 list类型数据基本操作

- 添加/修改数据
```
lpush <key> <value1> [value2]...
rpush <key> <value1> [value2]...
```
- 获取数据
```
lrange <key> <start> <stop>
lindex <key> <index>
llen <key>
```
- 获取并移除数据
```
lpop <key>
rpop <key>
```
- 规定时间内获取并移除数据
```
blpop <key1> [key2] ... <timeout>
brpop <key1> [key2] ... <timeout>
```
- 移除指定数据
```
lrem <key> <count> <value>
```

###### 1.3 list类型数据操作注意事项

- list保存的数据都是string类型的，数据总容量是有限的，最多2^32-1个元素
- list具有索引的概念，但是操作数据时通常以队列的形式进行入队出队操作，或以栈的形式进行入栈出栈操作
- 获取全部数据操作结束索引设置为-1
- list可以对数据进行分页操作，通常第一页的信息来自list，第2页及更多的信息通过数据库的形式加载

###### 1.4 list类型应用场景

1. 业务场景

twitter、新浪微博、腾讯微博中个人用户的关注列表需要按照用户的关注顺序进行展示，粉丝列表需要将最近关注的粉丝列在前面

新闻、资讯类网站如何将最新的新闻或者咨询按照发生的时间顺序展示？

企业运营过程中，系统将产生出大量的运营数据，如何保障多台服务器操作日志的统一顺序输出

![333eeb944b05009b09500b0270296f86.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5pipdpp4j31bm0e6q47.jpg)

2. 解决方案

- 依赖list的数据具有顺序性的特征对信息进行管理
- 使用队列模型解决多路信息汇总合并的问题
- 使用栈模型解决最新消息的问题