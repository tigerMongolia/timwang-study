##### 1. sorted_set 类型

- 新的存储需求，数据排序有利于数据的有效展示，需要提供一种可以根据自身特征进行排序的方式
- 需要的存储结构：新的存储模型，可以保存可排序的数据
- sorted_set类型：在set的存储结构基础上添加可排序字段

![4ab9b072b32a81d266bd148eb8c26be8.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5plgs8e7j30bc05q3ye.jpg)

![190f27dba670917b03c0c892cc50d951.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5plp7sqqj30km08e3yo.jpg)

##### 2. sorted_set 类型数据的基本操作

###### 2.1 操作

- 添加数据

```
zadd <key> <score1> <member1> [score2 member2]
```

- 获取全部数据

```
zrange <key> <start> <stop? [withscores]
zrevrange <key> <start> <stop? [withscores]
```

- 删除数据

```
zrem <key> <member> [member...]
```

- 按条件获取数据

```
zrangebyscore <key> <min> <max> [withscores] [limit]
zrevrangebyscore <key> <min> <max> [withscores] [limit]
```

- 条件删除数据

```
zremrangebyrank <key> <start> <stop>
zremrangebyscore <key> <min> <max>
```

- 获取集合数据总量

```
zcard <key>
zcount <key> <min> <max>
```

- 集合交并操作

```
zinterstore <destination> <numkeys> <key> [key...]
zunionstore <destination> <numkeys> <key> [key...]
```

###### 2.2 注意

- min与max用于限定搜索查询的条件
- start与stop用于限定查询范围，作用于索引，表示开始和结束索引
- offset与count用于限定查询范围，作用于查询结果，表示开始位置和数据总量


##### 3. sorted_set 类型数据的扩展操作

1. 业务场景

- 票选广东十大杰出青年，各类综艺选修海选投票
- 各类资源网站TOP10（电影，歌曲，文档，电商，游戏等）
= 聊天室活跃度统计
- 游戏好友亲密度

2. 解决方案

- 获取数据对应的索引（排名）

```
zrank <key> <member>
zrevrank <key> <member>
```

- score值获取与修改

```
zscore <key> <member>
zincrby <key> <increment> <member>
```


##### 4. sorted_set 类型数据操作的注意事项

- score保存的数据存储空间是64位，整数范围long型
- score保存的数据也可以是一个双精度的double值，基于双精度浮点数的特征，可能会丢失精度，使用时候要慎重
- sorted_set底层存储还是基于set结构的，因此数据不能重复，如果重复添加相同的数据，scoe值将被反复覆盖，保留最后一次修改的结果

##### 5. sorted_set 类型应用场景

1. 业务场景

	基础服务+增值服务类网站会设定各类会员的试用，让用户充分体验会员优势。例如观影试用VIP、游戏VIP体验，云盘下载体验VIP，数据查看体验VIP。当VIP体验到期后，如果有效管理此类信息。即便对于正式VIP用户也存在对应的管理方式。
	网站会定期开始投票、讨论。限时进行，逾期作废。如何有效管理此类过期信息

2. 解决方案

- 对于基于时间线限定的任务处理，将处理时间记录为score值。利用排序功能区分处理的先后顺序
- 记录下一个要处理的时间，当到期后处理对应任务，移除redis中的记录，并记录下一个要处理的时间
- 当新任务加入时，判断并更新当前下一个要处理的任务时间
- 当提升sorted_set的性能，通常将任务根据特征存储为若干个sorted_set。例如1小时内，1天内，1周内，1月内，季内，年度等。操作时逐级提升，将即将操作的若干个任务纳入到1小时内处理的队列中


##### 6. sorted_set 类型应用场景3

1. 业务场景

任务、消息权重设定应用
	当任务或者消息待处理，形成了任务队列或者消息队列时，对于高优先级的任务要保障对其优先处理，如何实现任务权重管理

2. 解决方案

- 对于带有权重的任务，优先处理权重高的任务，采用score记录权重即可


