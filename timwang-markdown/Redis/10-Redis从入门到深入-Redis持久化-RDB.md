##### 1. 持久化简介

###### 1.1 什么是持久化

利用永久性存储介质将数据进行保存，在特定的时间将保存的数据进行恢复的工作机制称为持久化。

###### 1.2 为什么要进行持久化

防止数据的意外丢失，确保数据安全性

###### 1.3 持久化过程保存什么

- 将当前数据状态进行保存，快照形式，存储数据结果，存储格式简单，关注点在数据
- 将数据的操作过程进行保存，日志形式，存储操作过程，存储格式复杂，关注点在数据的操作过程

![6c31b1935bc4fec72a2b274d2a79ff9a.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5vrsigi9j30sq09274o.jpg)

##### 2. RDB-启动save

###### 2.1 谁，什么时间，干什么事情

- 谁：redis操作者（用户）
- 什么时间：即时（随时进行）
- 干什么事情：保存数据

###### 2.2 Save指令

- 命令 （save)
- 作用（手动执行一次保存操作）

###### 2.3 save指令相关配置

- db filename dump.rdb
	
	悦明：设置本地数据库文件名， 默认值为dump.rdb
	经验：通常设置为dump-端口号.rdb

- dir
	
	说明：设置存储.rdb文件的路径
	经验：通常设置成存储空间较大的目录中， 目录名称data

- rdb compression yes
	
	说明：设置存储至本地数据库时是否压缩数据， 默认为yes， 采用LZF压缩
	经验：通常默认为开启状态， 如果设置为no， 可以节省CPU运行时间， 但会使存储的文件变大(巨大)

- rdb checksum yes

	说明：设置是否进行RDB文件格式校验， 该校验过程在写文件和读文件过程均进行
	经验：通常默认为开启状态，如果设置为no，可以节约读写性过程约10%时间消耗，但是存储一定的数据损坏风险

![2a298084b82303924ea28f6cc3296240.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5vs5b2exj308i04gq2w.jpg)

###### 2.4 save指令工作原理

客户端1:127.0.0.1：6379>setkey 1 value 1 
客户端2:127.0.0.1：6379>setkey 2 value 2 
客户端3:127.0.0.1：6379>save
客户端4:127.0.0.1：6379>get key 1 

![3df66ac5c91225f393ab55db8cfd61e8.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5vseja6uj30ci03ot8n.jpg)


注意：save指令的执行会阻塞当前Redis服务器， 直到当前RDB过程完成为止， 有可能会造成长时间阻塞， 线上环境不建议使用。

##### 3. RDB-启动bgsave

###### 3.1 谁，什么时间，干什么事情

- 谁：redis操作者(用户) 发起指令； redis服务器控制指令执行
- 什么时间：即时(发起)；合理的时间(执行)
- 干什么事情：保存数据

###### 3.2 bgsave指令

- 命令

```
bgsave
```

- 作用
    手动启动后台保存操作，但不是立即执行
    
    
###### 3.3 bgsave指令工作原理

![77b08b98d3a6a6537ede73d93d2e918c.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5vspbrtqj30u607aaac.jpg)

注意：bgsave命令是针对save阻塞问题做的优化，Redis内部所有涉及到RDB操作都采用bgsave的方式，save命令可放弃使用


###### 3.4 bgsave指令相关配置

- dbfilename dump.rdb
- dir
- rdb compression yes
- rdb checksum yes
- stop-writes-on-bgsave-error yes
	说明：后台存储过程中如果出现错误现象，是否停止保存操作
	经验：通常默认为开启状态

##### 4 RDB启动-bgsave配置文件

反复执行保存指令，忘记了怎么办?不知道数据产生了多少变化，何时保存?

1. 自动执行

- 谁：redis服务器发起指令(基于条件)
- 什么时间：满足条件
- 干什么事情：保存数据

###### 4.1 save配置

- 配置

```
save <second> <changes>
```

- 作用
	
	满足限定时间范围内key的变化数量达到指定数量即进行持久化

- 参数
	
	second：监控时间范围
	changes：监控key的变化量

- 位置
	
	在conf文件中进行配置

- 范例

```
save 900 1
save 300 10
save 60 10000
```
    
###### 4.2 save配置原理

![24e4ca378a63ce7698cf6ce46349f98c.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5vt057yhj30na0aogm1.jpg)

注意：
- save配置要根据实际业务情况进行设置，频率过高过低都会出现性能问题，结果可能是灾难级的
- save配置中对于second与changes设置通常具有互补对应关系，尽量不要设置成包含性关系
- save配置启动后执行的是bgsave操作

##### 5. RDB三种启动方式对比


![d9f6936f62350df9cb49e78bc1febb8e.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5vtbpau1j30p207adfz.jpg)

##### 6. RDB特殊启动形式

- 全量复制
    在主从复制中详细讲解
- 服务器运行过程中重启
```
debug <reload>
```
- 关闭服务器时指定保存数据
```
shutdown <save>
```


##### 7. RDB优缺点


RDB优点

- RDB是一个紧凑压缩的二进制文件， 存储效率较高
- RDB内部存储的是redis在某个时间点的数据快照， 非常适合用于数据备份， 全量复制等场景
- RDB恢复数据的速度要比A OF快很多
- 应用：服务器中每X小时执行bg save备份， 并将RDB文件拷贝到远程机器中， 用于灾难恢复。

RDB缺点

- RDB方式无论是执行指令还是利用配置， 无法做到实时持久化， 具有较大的可能性丢失数据
- bgsave指令每次运行要执行fork操作创建子进程， 要牺牲掉一些性能
- redis的众多版本中未进行RDB文件格式的版本统一，有可能出现各版本服务之间数据格式无法兼容现象

