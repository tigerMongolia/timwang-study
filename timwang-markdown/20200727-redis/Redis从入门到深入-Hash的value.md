##### 2. hash类型

###### 2.1 存储的困惑

对象类数据的存储如果具有较频繁的更新需求操作会显得笨重

![d8dfc49432a8baf02aa4de23bee3f259.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5qi11tqyj31160bkt93.jpg)

![ea157101e8e7a301741d9672e8b9f89e.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5qi7wwjyj310g0ccglz.jpg)

###### 2.2 hash类型

- 新的存储需求，对一系列的存储的数据进行编组，方便管理，典型应用存储对象信息
- 需要的存储结构：一个存储空间保存多个键值对数据
- hash存储结构优化
    
    - 如果field数量比较少，存储结构优化为类数组结构
    - 如果field数量比较多，存储结构使用HashMap结构

![45cbd9f2b1c5dfa6c912d5d7900da2d6.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5qif5zoaj30og0ekmxc.jpg)

###### 2.3 hash类型数据的基本操作

- 添加/修改数据

```
hset <key> <field> <value>
```
- 获取数据
```
hget <key> <field>
hgetall <key>
```
- 删除数据
```
hdel <key> <field1> [field2]...
```
- 添加/修改多个数据
```
hmset <key> <filed1> <value1> <filed2> <value2> ..
```
- 获取多个数据
```
hmget <key> <filed1> <filed2> ..
```
- 获取哈希表中字段的数量
```
hlen <key>
```
- 获取哈希表中是否存在指定的字段
```
hexists <key> <field>
```

- 获取哈希表中所有的字段或字段值
```
hkeys <key>
hvals <key>
```
- 设置指定字段的数值数据增加指定范围的值
```
hincrby <key> <filed> <increment>
hincrbyfloat <key> <field> <increment>
```

###### 2.4 hash类型数据操作的注意事项

- hash类型下的value只能存储字符串，不允许存储其他数据类型，不存在嵌套现象。如果数据未获取到对应的值为（nil）

- 每个hash可以存储2^32-1个键值对

- hash类型十分贴近对象的数据存储形式，并且可以灵活添加删除对象属性。但hash设计初衷不是为了存储大量对象而设计的，切记不可滥用，更不可以将hash作为对象列表使用

- hgetall操作可以获取全部属性，如果内部field过多，遍历整体数据效率就会很低，有可能成为数据访问瓶颈

###### 2.5 hash类型应用场景-购物车

1. 业务场景

电商网站购物车设计与实现

![f4df3e33ccc8ad56957ca396b4bb70c3.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5qimkbpuj30jk0lo75c.jpg)

2. 业务分析

- 仅分析购物车的redis存储模型（添加、浏览、更改数量、删除、清空）
- 购物车于数据库间持久化同步
- 购物车于订单间关系
    提交购物车：读取数据生成订单
    商家临时加个调整：隶属于订单级别
- 未登录用户购物车信息存储
    cookie存储
    
3. 解决方案

- 以客户id作为key，每位客户创建一个hash存储结构存储对应的购物车信息
- 将商品编号作为field，购买数量作为value
- 添加商品：追加全新的field与value
- 浏览：遍历hash
- 更改数量：自增/自减，设置value值
- 删除商品：删除field
- 清空：删除key

4. 设计方案优缺点

当前仅仅是将数据存储到了redis中，并没有起到加速的作用，商品信息还需要二次查询数据库

- 每条购物车中的商品记录保存成两条field
- field1专用于保存购买数量
     命名格式：商品id:nums
     保存数据：数值
- field2专用于保存购物车种显示的信息，包含文字描述，图片地址，所属商家信息等（独立hash））
     命名格式：商品id:info
     保存数据：json

```
hsetnx key field value
```

###### 2.5 hash类型应用场景-重置

1. 业务场景

双11活动日，销售手机充值卡的商家对移动、联通、电信的30元、50元、100元商品退出抢购活动，每种商品抢购上限1000张

![566dd90f5477fa71786e0e30c9e37bc9.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5qiusxmqj30v80fq3yz.jpg)

2. 解决方案

- 以商品id作为key
- 将参与抢购的商品id作为field
- 将参与抢购的商品数量作为对应的value
- 抢购时使用降值得方式控制产品数量
- 实际业务中还有超卖等实际问题，这里不做讨论