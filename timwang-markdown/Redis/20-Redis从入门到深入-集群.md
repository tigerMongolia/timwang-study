##### 1. 集群

###### 1.1 现状问题

业务发展过程中遇到的峰值瓶颈
- redis提供的服务PS可以达到10万/秒， 当前业务OPS已经达到20万/秒
- 内存单机容量达到256G，当前业务需求内存容量1T
- 使用集群的方式可以快速解决上述问题


###### 1.2 集群架构

集群就是使用网络将若干台计算机联通起来，并提供统一的管理方式，使其对外呈现单机的服务效果

![b979def9aba54e55a3737bc1b95b0a3a.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5wauvpqfj30lg0buaa2.jpg)


###### 1.3 集群作用

- 分散单台服务器的访问压力，实现负载均衡
- 分散单台服务器的存储压力，实现可扩展性
- 降低单台服务器宕机带来的业务灾难

![bb541132211a7cd74cf1933530d0175c.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5wb0posej30qe0l6q3d.jpg)

##### 2. redis集群结构设计

- 通过算法设计， 计算出key应该保存的位置
- 将所有的存储空间计划切割成16384份，每台主机保存一部分
	
	每份代表的是一个存储空间， 不是一个key的保存空间

- 将key按照计算出的结果放到对应的存储空间

![80216c475c5d388a4d3fdce59b7e0b61.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5wb6l1f7j316w0nujsj.jpg)

###### 2.1 数据存储设计

![9a615d4d09b3b35bdb8b89798f98cc7c.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5wbcw2vqj30no0gk74n.jpg)

###### 2.2 集群内部通讯设计

![fe7683b729c8585b800da19e7f656ddb.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5wbjtdcjj30wg0eyaaw.jpg)

##### 3. Cluster集群架构搭建

- 设置加入cluster， 成为其中的节点
```
cluster-enabled yes|no
```

- cluster配置文件名， 该文件属于自动生成， 仅用于快速查找文件并查询文件内容
```
cluster-config-file<filename>
```
- 节点服务响应超时时间，用于判定该节点是否下线或切换为从节点
```
cluster-node-timeout<milliseconds>
```
- master连接的slave最小数量
```
cluster-migration-barrier<count>
```
