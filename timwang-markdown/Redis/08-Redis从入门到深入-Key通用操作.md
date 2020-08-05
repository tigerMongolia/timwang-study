##### 1. key通用操作

###### 1.1 key特征

- key是一个字符串， 通过key获取red is中保存的数据

###### 1.2 key应该设计哪些操作?

- 对于key自身状态的相关操作， 例如：删除， 判定存在， 获取类型等
- 对于key有效性控制相关操作， 例如：有效期设定， 判定是否有效， 有效状态的切换等
- 对于key快速查询操作， 例如：按指定策略查询key

###### 1.3 key基本操作

- 删除指定key

```
del key

```
	
- 获取key是否存在

```
exists key

```
	
- 获取key的类型

```
type key
```

###### 1.3 key扩展操作(时效性控制)

- 为指定key设置有效期

```
expire key seconds
pexpire key milliseconds
expire at key timestamp
pexpire at key milliseconds-timestamp
```

- 获取key的有效时间

```
ttl key
pttl key
```

- 切换key从时效性转换为永久性

```
persist key
```



###### 1.4 key扩展操作(查询模式)

- 查询key

```
keys pattern
```
- 查询模式规则

· 匹配任意数量的任意符号
? 配合一个任意符号
[] 匹配一个指定符号

keys *（查询所有）
keys it*（查询所有以it开头）
keys *heima（查询所有以heim a结尾）
keys ??heima（查询所有前面两个字符任意， 后面以heima结尾）
keys user:?（查询所有以user:开头， 最后一个字符任意）
keys u[st]er:1（查询所有以u开头，以er:11结尾，中间包含一个字母，s或t）

###### 1.5 key其他操作

- 为key改名

```
rename key new key
rename nx key new key
```

- 对所有key排序

```
sort
```

- 其他key通用操作

```
help @generic
```

##### 2. key的重复问题

- key是由程序员定义的
- redis在使用过程中， 伴随着操作数据量的增加，会出现大量的数据以及对应的key
- 数据不区分种类、类别混杂在一起，极易出现重复或冲突


1. 解决方案

- redis为每个服务提供有16个数据库， 编号从0到15
- 每个数据库之间的数据相互独立


##### 3. db 基本操作

- 切换数据库

```
select <index>
```

- 其他操作

```
quit
ping
echo <message>
```

- 数据移动

```
move <key> <db>
```

- 数据清除

```
dbsize
flushdb
flushall
```

