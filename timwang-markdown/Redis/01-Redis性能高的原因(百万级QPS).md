#### 一、性能测试报告

查看了下阿里云 Redis 的性能测试报告如下，能够达到数十万、百万级别的 QPS（暂时忽略阿里对 Redis 所做的优化），我们从 Redis 的设计和实现来分析一下 Redis 是怎么做的。

![](https://tva1.sinaimg.cn/large/007S8ZIlgy1ghgb3ns5fvj30rr0kdjsx.jpg)

#### 二、Redis 的设计与实现

其实 Redis 主要是通过三个方面来满足这样高效吞吐量的性能需求

- 高效的数据结构
- 多路复用 IO 模型
- 事件机制

##### 2.1 高效的数据结构

Redis 支持的几种高效的数据结构 string（字符串）、hash（哈希）、list（列表）、set（集合）、zset（有序集 合）

以上几种对外暴露的数据结构它们的底层编码方式都是做了不同的优化的，例如string上一章节就有讲到

##### 2.2 多路复用IO模型

假设某一时刻与 Redis 服务器建立了 1 万个长连接，对于阻塞式 IO 的做法就是，对每一条连接都建立一个线程来处理，那么就需要 1万个线程，同时根据我们的经验对于 IO 密集型的操作我们一般设置，线程数 = 2 * CPU 数量 + 1，对于 CPU 密集型的操作一般设置线程 = CPU 数量 + 1，当然各种书籍或者网上也有一个详细的计算公式可以算出更加合适准确的线程数量，但是得到的结果往往是一个比较小的值，像阻塞式 IO 这也动则创建成千上万的线程，系统是无法承载这样的负荷的更加弹不上高效的吞吐量和服务了。

而多路复用 IO 模型的做法是，用一个线程将这一万个建立成功的链接陆续的放入 event_poll，event_poll 会为这一万个长连接注册回调函数，当某一个长连接准备就绪后（建立建立成功、数据读取完成等），就会通过回调函数写入到 event_poll 的就绪队列 rdlist 中，这样这个单线程就可以通过读取 rdlist 获取到需要的数据



https://blog.csdn.net/javaer_lee/article/details/87444386

https://draveness.me/redis-io-multiplexing/

https://juejin.im/post/6844904082176475144

https://blog.csdn.net/u014590757/article/details/79860766

