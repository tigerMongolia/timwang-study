##### 1. Bitmaps

![bf0ef358542b113571d5c9b46fd74bfc.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5w47p1fuj30zq0j6gmg.jpg)

###### 1.1 Bitmaps类型的基础操作

- 获取指定key对应偏移量上的bit值

```
getbit key offset
```

- 设置指定key对应偏移量上的bit值， value 只能是1或 0

```
setbit key offset value
```

###### 1.2 Bitmaps类型的扩展操作

1. 业务场景-电影网站

	- 统计每天某一部电影是否被点播
	- 统计每天有多少部电影被点播
	- 统计每周/月/年有多少部电影被点播
	- 统计年度哪部电影没有被点播
	
2. 业务分析

![51e3a6072a4a61a9734806cab20b4ed5.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5w4enyq9j30ki04qmx3.jpg)


###### 1.3 Bitmaps类型的扩展操作

- 对指定key按位进行交、并、非、异或操作， 并将结果保存到destKey中

```
bitop op destKey key1 [key2...]
```

    - and：交
	- or：并
	- not：非
	- xor：异或

- 统计指定key中1的数量

```
bitcount key [start end]
```

![623a81f8c36774906ac0e6f4e050fb72.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5w3sizkrj30ks0aqdgb.jpg)


##### 2. HyperLogLog

###### 2.1 基数集

![4365ff1f3a37b45774c0c986524dc611.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5w4vhpojj30qu06wdft.jpg)

###### 2.2 LogLog算法

![8114c899fb63b9af951022cfaead2170.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5w578vauj30yq0m275p.jpg)

###### 2.3 HyperLogLog类型的基本操作

- 添加数据

```
pfadd <key> <element> [element...]
```

- 统计数据

```
pfcount <key> [key...]
```

- 合并数据

```
pfmerge <destkey> <sourcekey> [sourekey...]
```

###### 2.4 相关说明

- 用于进行基数统计，不是集合，不保存数据，只记录数量而不是具体数据
- 核心是基数估算算法，最终数值存在一定误差
- 误差范围：基数估计的结果是一个带有0.81%标准错误的近似值
- 耗空间极小， 每个hyperloglog key占用了12K的内存用于标记基数
- pfadd命令不是一次性分配12K内存使用， 会随着基数的增加内存逐渐增大
- Pfmerge命令合并后占用的存储空间为12K， 无论合并之前数据量多少


##### 3. GEO

###### 3.1 GEO类型的基本操作

- 添加坐标点

```
geoadd key longitude latitude member[longitude latitude member...]
```


- 获取坐标点

```
geopos key member[member...]
```


- 计算坐标点距离

```
geodist key member1 member2[unit]
```


- 根据坐标求范围内的数据

```
georadius key longitude latitude radius m|kml ft|mi[with coord] [with dist] [with hash] [count count]
```

- 根据点求范围内数据

```
georadius by member key member radius m|km|ft|mi[with coord] [with dist] [with hash] [count count]
```

- 获取指定点对应的坐标hash值

```
geohash key member[member...]
```


![af0d72ba3298f9701f5207de07377bb8.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5w5fenuzj30m00hedgj.jpg)

![feb78535bf08aa9bb9b6aa4607dc9da4.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5w5lod3cj30ia0giq3e.jpg)
