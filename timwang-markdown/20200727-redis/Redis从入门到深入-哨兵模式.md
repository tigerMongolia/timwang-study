##### 1. 哨兵简介-主机“宕机”

- 将宕机的master下线
- 找一个slave作为master
- 通知所有的slave连接新的master
- 启动新的master与slave
- 全量复制*N+部分复制*N
- 谁来确认master宕机了
- 找一个主?怎么找法?
- 修改配置后，原始的主恢复了怎么办?

![b79775e1edf100f562fcdc531659a445.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5w8w77ohj30nk0aswel.jpg)

##### 2. 哨兵

哨兵(sentinel) 是一个分布式系统， 用于对主从结构中的每台服务器进行监控， 当出现故障时通过投票机制选择新的master并将所有slave连接到新的master。

![6197102c52995caed8cc0c36803c0358.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5w93o6sdj30gq0aet8v.jpg)

1. 哨兵的作用

- 监控
	
	不断的检查master和slave是否正常运行。
	master存活检测、master与slave运行情况检测

- 通知(提醒)
	
	当被监控的服务器出现问题时，向其他(哨兵间，客户端)发送通知。

- 自动故障转移

	断开master与slave连接， 选取一个slave作为master， 将其他slave连接到新的master， 并告知客户端新的服
务器地址

注意：

	哨兵也是一台redis服务器， 只是不提供数据服务
	通常哨兵配置数量为单数
    
2. 启用哨兵模式

- 配置一拖二的主从结构
- 配置三个哨兵(配置相同，端口不同)
- 启动哨兵

```
redis-sentinel sentinel-端口号.conf
```	

![8a97418e4f4bedb4239f51597877577b.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5w9ckgocj30ru02edfr.jpg)

##### 3. 哨兵工作原理

1. 阶段一：监控阶段

- 用于同步各个节点的状态信息
	
	- 获取各个sentinel的状态(是否在线)

	- 获取master的状态

		- master属性
			- run id
			- role：master
		- 各个slave的详细信息

	- 获取所有slave的状态(根据master中的slave信息)

		- slave属性

			- run id
			- role：slave
			- master_host、master_port
			- offset
			- ......

![dcef26e8ba2723f016a0a39deae15e63.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5w9i33nwj30im0iajrm.jpg)

![2a05ce04c751e1a9d7e84644d1c846e5.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5w9om9yfj31460roabu.jpg)

2. 阶段二：通知阶段

![a13f46bc6cf734bd6f2e423ad9e93a4c.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5w9w9o9gj31di0ki75h.jpg)

3. 阶段三：故障转移阶段

![7678ee2e85a28abdc10b56d99ac4b0a0.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5wa38manj31fg0oodhr.jpg)

![da995784c73d11e0eb3881cb2feff9bf.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5wa8c5lwj318a0n8q4e.jpg)

- 服务器列表中挑选备选master

	- 在线的
	- 响应慢的
	- 与原master断开时间久的
	- 优先原则
		- 优先级
		- offset
		- runid

- 发送指令(sentinel)

	
	- 向新的master发送slave of no one
	- 向其他slave发送slave of新master IP端口


- 监控
	
	- 同步信息

- 通知
	
	- 保持联通

- 故障转移

	- 发现问题
	- 竞选负责人
	- 优选新master
	- 新master上任， 其他slave切换master， 原master作为slave故障回复后连接

