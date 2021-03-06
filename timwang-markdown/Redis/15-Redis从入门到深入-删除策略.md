##### 1. 删除策略

###### 1.1 过期数据

1. redis中的数据特征

- redis是一种内存级数据库， 所有数据均存放在内存中， 内存中的数据可以通过TTL指令获取其状态

	- XX：具有时效性的数据
	- -1：永久有效的数据
	- -2：已经过期的数据或被删除的数据或未定义的数据

- 过期的数据真的删除了吗?

###### 1.2  数据删除策略

1.定时删除
2.惰性删除
3.定期删除

###### 1.3 时效性数据的存储结构

![d075ab875ffb393c89073160f5eea9e8.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5vy1k6rmj30ug0cc3yy.jpg)

###### 1.4 数据删除策略的目标

在内存占用与CPU占用之间寻找一种平衡，顾此失彼都会造成整体redis性能的下降， 甚至引发服务器宕机或内存泄露


##### 2. 定时删除

- 创建一个定时器， 当key设置有过期时间， 且过期时间到达时， 由定时器任务立即执行对键的删除操作
- 优点：节约内存，到时就删除，快速释放掉不必要的内存占用
- 缺点：CPU压力很大， 无论CPU此时负载量多高， 均占用CPU， 会影响redis服务器响应时间和指令吞吐量
- 总结：用处理器性能换取存储空间

![5e656a39989b9b9919bb364eb7df207b.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5vyc050cj30h2098jrc.jpg)

##### 3. 惰性删除

- 数据到达过期时间，不做处理。等下次访问该数据时
	
	- 如果未过期，返回数据
	- 发现已过期，删除，返回不存在

- 优点：节约CPU性能， 发现必须删除的时候才删除
- 缺点：内存压力很大，出现长期占用内存的数据

![941e5e11bba32b53090f89729fefc797.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5vyjwanfj30fg07wgll.jpg)

##### 4. 定期删除-两种方案都走极端，有没有折中方案?

###### 4.0 定期删除策略

Redis 会将每个设置了过期时间的 key 放入到一个独立的字典中，默认每 100ms 进行一次过期扫描：

1. 随机抽取 20 个 key
2. 删除这 20 个key中过期的key
3. 如果过期的 key 比例超过 1/4，就重复步骤 1，继续删除。


###### 4.0 定期删除伪代码

- redis启动服务器初始化时， 读取配置server.hz的值， 默认为 10

- 每秒钟执行server.hz次serverCron() -> databasesCron() -> activeExpireCycle()

- active Expire Cycle() 对每个expires[*] 逐一进行检测， 每次执行250ms/server.hz

- 对某个expires[*] 检测时， 随机挑选W个key检测

	- 如果key超时， 删除key
	- 如果一轮中删除的key的数量>W*25%， 循环该过程
	- 如果一轮中删除的key的数量≤W*25%， 检查下一个expires[*] ， 0-15循环
	- W取值=ACTIVE EXPIRE CYCLE LOOKUPS PER LOOP属性值

- 参数current_db用于记录activeExpireCycle() 进入哪个expires[*] 执行

- 如果activeExpireCycle() 执行时间到期， 下次从current_db继续向下执行


![f5d72ed2f4f44a5f63c288d53c44b233.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5vytn8fvj30jq0r0jsa.jpg)

###### 4.1 定期删除特点

- 周期性轮询redis库中的时效性数据， 采用随机抽取的策略，利用过期数据占比的方式控制删除频度

- 特点1：CPU性能占用设置有峰值， 检测频度可自定义设置
- 特点2：内存压力不是很大，长期占用内存的冷数据会被持续清理

- 总结：周期性抽查存储空间(随机抽查，重点抽查)

###### 4.2 为什么不扫描所有的key

Redis 是单线程，全部扫描岂不是卡死了。而且为了防止每次扫描过期的 key 比例都超过 1/4，导致不停循环卡死线程，Redis 为每次扫描添加了上限时间，默认是 25ms。

如果客户端将超时时间设置的比较短，比如 10ms，那么就会出现大量的链接因为超时而关闭，业务端就会出现很多异常。而且这时你还无法从 Redis 的 slowlog 中看到慢查询记录，因为慢查询指的是逻辑处理过程慢，不包含等待时间。

如果在同一时间出现大面积 key 过期，Redis 循环多次扫描过期词典，直到过期的 key 比例小于 1/4。这会导致卡顿，而且在高并发的情况下，可能会导致缓存雪崩。

为什么 Redis 为每次扫描添的上限时间是 25ms，还会出现上面的情况？

因为 Redis 是单线程，每个请求处理都需要排队，而且由于 Redis 每次扫描都是 25ms，也就是每个请求最多 25ms，100 个请求就是 2500ms。

##### 5. 删除策略比对

1.定时删除 (节约内存，无占用)（不分时段占用CPU资源， 频度高）（拿时间换空间）
2.惰性删除（内存占用严重）（延时执行， CPU利用率高）（拿空间换时间）
3.定期删除（内存定期随机清理）（每秒花费固定的CPU资源维护内存）（随机抽查，重点抽查）
传智播客旗下高端IT教育品牌

##### 6. 逐出算法

###### 6.1 当新数据进入redis时， 如果内存不足怎么办?

- redis使用内存存储数据， 在执行每一个命令前， 会调用freeMemorylfNeeded() 检测内存是否充足。如果内存不满足新加入数据的最低存储要求， redis要临时删除一些数据为当前指令清理存储空间。清理数据的策略称为逐出算法。

- 注意：逐出数据的过程不是100%能够清理出足够的可使用的内存空间，如果不成功则反复执行。当对所有数据尝试完毕后，如果不能达到内存清理的要求，将出现错误信息。

###### 6.2 影响数据逐出的相关配置

- 最大可使用内存

```
maxmemory
```
占用物理内存的比例，默认值为0，表示不限制。生产环境中根据需求设定，通常设置在50%以上。

- 每次选取待删除数据的个数

```
maxmemory-samples
```
选取数据时并不会全库扫描，导致严重的性能消耗，降低读写性能。因此采用随机获取数据的方式作为待检测删除数据

- 删除策略

```
maxmemory-policy
```
达到最大内存后的，对被挑选出来的数据进行删除的策略

###### 6.3 影响数据逐出的相关配置

- 检测易失数据(可能会过期的数据集server.db[].expires)

    1. noeviction：当内存超出 maxmemory，写入请求会报错，但是删除和读请求可以继续。（使用这个策略，疯了吧）
    2. allkeys-lru：当内存超出 maxmemory，在所有的 key 中，移除最少使用的key。只把 Redis 既当缓存是使用这种策略。（推荐）。
    3. allkeys-random：当内存超出 maxmemory，在所有的 key 中，随机移除某个 key。（应该没人用吧）
    4. volatile-Iru：挑选最近最少使用的数据淘汰
    5. volatile-lfu：挑选最近使用次数最少的数据淘汰
    6. volatile-ttl：挑选将要过期的数据淘汰
    7. volatile-random：任意选择数据淘汰

![49c3a6bab0b6e8994fd97a413d8f2a86.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5vz59ocmj30n007aq33.jpg)

###### 6.4 影响数据逐出的相关配置

- 检测易失数据(可能会过期的数据集server.db[i] .expires)

	1. volatile-lru：挑选最近最少使用的数据淘汰
	2. volatile-lfu：挑选最近使用次数最少的数据淘汰
	3. volatile-ttl：挑选将要过期的数据淘汰
	4. volatile-random：任意选择数据淘汰

- 检测全库数据(所有数据集server.db[] .dict)

	5. allkeys-lru：挑选最近最少使用的数据淘汰
	6. allkeys-lfu：挑选最近使用次数最少的数据淘汰
	7. allkeys-random：任意选择数据淘汰

- 放弃数据驱逐

	8. no-envi ction(驱逐) ：禁止驱逐数据(red is 4.0中默认策略) ， 会引发错误OOM(OutOfMemory)

```
max memory-policy volatile-lru
```


###### 6.5 数据逐出策略配置依据

- 使用INFO命令输出监控信息， 查询缓存hit和miss的次数， 根据业务需求调优Redis配置


###### 6.6 LRU算法

实现 LRU 算法除了需要 key/value 字典外，还需要附加一个链表，链表中的元素按照一定的顺序进行排列。当空间满的时候，会踢掉链表尾部的元素。当字典的某个元素被访问时，它在链表中的位置会被移动到表头。所以链表的元素排列顺序就是元素最近被访问的时间顺序。

使用 Python 的 OrderedDict(双向链表 + 字典) 来实现一个简单的 LRU 算法：

###### 6.7 LFU算法

Redis 4.0 里引入了一个新的淘汰策略 —— LFU（Least Frequently Used） 模式，作者认为它比 LRU 更加优秀。

LFU 表示按最近的访问频率进行淘汰，它比 LRU 更加精准地表示了一个 key 被访问的热度。

如果一个 key 长时间不被访问，只是刚刚偶然被用户访问了一下，那么在使用 LRU 算法下它是不容易被淘汰的，因为 LRU 算法认为当前这个 key 是很热的。而 LFU 是需要追踪最近一段时间的访问频率，如果某个 key 只是偶然被访问一次是不足以变得很热的，它需要在近期一段时间内被访问很多次才有机会被认为很热。

**Redis 对象的热度**

Redis 的所有对象结构头中都有一个 24bit 的字段，这个字段用来记录对象的热度。











