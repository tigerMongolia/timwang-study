##### 1. Jedis

###### 1.1 Jedis简介

- Java语言连接redis服务
    - Jedis
    - SpringData Redis
    - Lettuce
- C、C++、C#、Erlang...


###### 1.2 HelloWord（Jedis版）

![01e131ed11150260d80c2a1243e9e074.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5vpfcy3wj30cy07aq34.jpg)

![4967c2268b0e77e115aa4d60d707f714.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5vponidgj30os0a0dg0.jpg)

- 连接redis
```
Jedis jedis = new Jedis(“localhost”, 6379);
```
- 操作redis
```
jedis.set("name","test");
jedis.get("name");
```
- 关闭redis连接
```
jedis.close();
```

###### 1.3 Jedis读写redis数据

人工智能领域的语义识别与自动对话僵尸未来服务业机器人应答呼叫技术体系中的重要技术，百度自研用户评价语义识别服务，免费开放给企业使用，同事训练自己的模型，现对使用用户的行为进行限速，限制每个用户每分钟最多发起10次调用

1. 案例要求

- 设定A、B、C三个用户
- A用户限制10次/分调用，B用户限制30/分调用，C用户不限制

2. 需求分析

- 设定一个服务方法，用于模拟实际业务调用的服务，内部采用打印模拟调用
- 在业务调用的服务调用控制单元，内部使用redis进行控制，参照之前的方案
- 对调用超限使用异常进行控制，异常处理设定为打印提示信息
- 主程序启动3个线程，分别表示3中不同用户的调用

3. 代码实现

![7d132d798751d46d44ed29a9d6ebb14d.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5vpx74t4j30rw0csdg9.jpg)

![7fd906484b494b62e35773248e747549.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5vpx74t4j30rw0csdg9.jpg)

###### 1.4 Jedis简易工具类开发

- JedisPool：Jedis提供的连接池技术
    - poolConfig：连接池配置对象
    - host：redis服务地址
    - port：redis服务端口号

![66873eaf53c5897bde8cc8bbcd50dd81.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5vqbqmksj30r60300sr.jpg)

- 封装连接参数

![9b8dda670d41b4e08600d75d899e8a96.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5vqjpj1lj30nm056748.jpg)

- 获取连接，对外房为借口，提供jedis连接对象

![928a49c2e27164933ac22d10b9ce9ae7.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5vqyrz85j30lm07e74e.jpg)


###### 1.5 可视化客户端

