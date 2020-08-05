##### 1. RDB的弊端

1. 弊端

- 存储数据量较大，效率较低
	- 基于快照思想，每次读写都是全部数据，当数据量巨大时，效率非常低
- 大数据量下的IO性能较低
- 基于fork创建子进程， 内存产生额外消耗
- 宕机带来的数据丢失风险

2. 解决思路

- 不写全数据，仅记录部分数据
- 改记录数据为记录操作过程
- 对所有操作均进行记录，排除丢失数据的风险

##### 2. AOF概念

- AOF(append only file) 持久化：以独立日志的方式记录每次写命令， 重启时再重新执行AOF文件中命令，达到恢复数据的目的。与RDB相比可以简单描述为改记录数据为记录数据产生的过程
- AOF的主要作用是解决了数据持久化的实时性， 目前已经是Redis持久化的主流方式

###### 2.1 AOF写数据过程

![60ae833ffa3ea869d987e23c168074b8.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5vu4sfddj30o00bk0sy.jpg)

###### 2.2 AOF写数据三种策略（appendfsync）

- always(每次)
	
	每次写入操作均同步到AOF文件中， 数据零误差， 性能较低

- every sec(每秒)
	
	每秒将缓冲区中的指令同步到AOF文件中， 数据准确性较高， 性能较高
	在系统突然容机的情况下丢失1秒内的数据

- no(系统控制)

	由操作系统控制每次同步到AOF文件的周期， 整体过程不可控

###### 2.3 AOF功能开启

- 配置

```
appendonly yes|no
```

- 作用
	
	是否开启AOF持久化功能， 默认为不开启状态

- 配置

```
appendfsync always|everysec|no
```

- 作用

	AOF写数据策略


###### 2.4 AOF写数据遇到的问题

如果连续执行如下指令该如何处理

127.0.0.1：6379>setname zs
127.0.0.1：6379>setname Is
127.0.0.1：6379>setname ww
127.0.0.1：6379>inc rnum
127.0.0.1：6379>inc rnum
127.0.0.1：6379>inc rnum

##### 3. AOF重写

###### 3.1 AOF重写

随着命令不断写入AOF， 文件会越来越大， 为了解决这个问题， redis引入了AOF重写机制压缩文件体积。AOF文件重写是将redis进程内的数据转化为写命令同步到新AOF文件的过程。简单说就是将对同一个数据的若干个条命令执行结果转化成最终结果数据对应的指令进行记录。

###### 3.2 AOF重写作用

- 降低磁盘占用量，提高磁盘利用率
- 提高持久化效率，降低持久化写时间，提高lO性能
- 降低数据恢复用时，提高数据恢复效率

###### 3.3 AOF重写规则

- 进程内已超时的数据不再写入文件
- 忽略无效指令，重写时使用进程内数据直接生成，这样新的AOF文件只保留最终数据的写入命令

	如del key1、hdel key2、srem key3、set key4 111、set key4 222等

- 对同一数据的多条写命令合并为一条命令

	如lpush list 1a、lpush list 1b、lpush list1 c可以转化为：lpush list1 a b c。
	为防止数据量过大造成客户端缓冲区溢出， 对list、set、hash、zset等类型， 每条指令最多写入64个元素
    
###### 3.4 AOF重写方式

- 手动重写
```
bgrewriteaof
```
- 自动重写
```
auto-aof-rewrite-min-size <size>
auto-aof-rewrite-percentage <percentage>
```

##### 4. AOF手动重写

###### 4.1 bgrewriteaof指令原理

![d35cc8bf24fa50df9544f717a72aa639.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5vufc0loj30so06qt8y.jpg)

##### 5 AOF自动重写方式

###### 5.1 AOF自动重写方式

- 自动重写触发条件设置

```
auto-aof-rewrite-min-size size
auto-aof-rewrite-percentage percent
```

- 自动重写触发比对参数(运行指令info Persistence获取具体信息)

```
aof_urrent_size
aof_base_size
```

- 自动重写触发条件

```
aof_current_size>auto-aof-rewrite-min-size
aof_current_size-aof_base_size/aof_base_size>=auto-aof-rewrite-percentage
```

##### 6. AOF重写流程

![efc2c95fd4263eae91a1bbfd326d3e60.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5vunmea6j30u40cadgd.jpg)

![34a90eaf022138bebf28061a1763cb6b.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5vv2gogij30n00cgt94.jpg)



