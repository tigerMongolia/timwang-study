#### 一、Dubbo的provider和consumer都配置timeout

在Provider上尽量多配置Consumer端属性，原因如下：

1. 作服务的提供者，比服务使用方更清楚服务性能参数，如调用的超时时间，合理的重试次数，等等

2. 在Provider配置后，Consumer不配置则会使用Provider的配置值，即Provider配置可以作为Consumer的缺省值。否则，Consumer会使用Consumer端的全局设置，这对于Provider不可控的，并且往往是不合理的

3. 配置的覆盖规则：

   1) 方法级配置别优于接口级别，即小Scope优先 

   2) Consumer端配置 优于 Provider配置 优于 全局配置

#### 二、Dubbo是怎么嵌入spring容器的

#### 三、Dubbo服务挂了怎么办

#### 四、Dubbo服务如何处理高并发请求

#### 五、dubbo超时重试、降级

#### 五. 注册中心宕机怎么办

假如zookeeper注册中心宕掉，一段时间内服务消费方还是能够调用提供方的服务的，实际上它使用的本地缓存进行通讯，这只是dubbo健壮性的一种体现。

1. 监控中心宕掉不影响使用，只是丢失部分采样数据
2. 数据库宕掉后，注册中心仍能通过缓存提供服务列表查询，但不能注册新服务
3. 注册中心对等集群，任意一台宕掉后，将自动切换到另一台
4. 注册中心全部宕掉后，服务提供者和服务消费者仍能通过本地缓存通讯
5. 服务提供者无状态，任意一台宕掉后，不影响使用
6. 服务提供者全部宕掉后，服务消费者应用将无法使用，并无限次重连等待服务提供者恢复

#### 六、Dubbo泛化调用

#### 七、Dubbo异常处理

#### 8. **Dubbo 服务暴露**

Dubbo 会在 Spring 实例化完 bean 之后，在刷新容器最后一步发布 ContextRefreshEvent 事件的时候，通知实现了 ApplicationListener 的 ServiceBean 类进行回调 onApplicationEvent 事件方法，Dubbo 会在这个方法中调用 ServiceBean 父类 ServiceConfig 的 export 方法，而该方法真正实现了服务的（异步或者非异步）发布。

#### 9. Dubbo协议

dubbo:// rmi:// http:// hession:// redis://

#### 10. Dubbo服务之间的调用是阻塞的吗

Dubbo 缺省协议采用单一长连接，底层实现是 Netty 的 NIO 异步通讯机制；基于这种机制，Dubbo 实现了以下几种调用方式：

- 同步调用
- 异步调用
- 参数回调
- 事件通知

#### 11. Dubbo和SpringCloud区别

Dubbo 是 SOA 时代的产物，它的关注点主要在于服务的调用，流量分发、流量监控和熔断。

而 Spring Cloud 诞生于微服务架构时代，考虑的是微服务治理的方方面面，另外由于依托了 Spring、Spring Boot 的优势之上

两个框架在开始目标就不一致，Dubbo 定位服务治理、Spring Cloud 是打造一个生态。

- Dubbo 底层是使用 Netty 这样的 NIO 框架，是基于 TCP 协议传输的，配合以 Hession 序列化完成 RPC 通信。
- Spring Cloud 是基于 Http 协议 Rest 接口调用远程过程的通信，相对来说 Http 请求会有更大的报文，占的带宽也会更多。但是 REST 相比 RPC 更为灵活，服务提供方和调用方的依赖只依靠一纸契约，不存在代码级别的强依赖，这在强调快速演化的微服务环境下，显得更为合适，至于注重通信速度还是方便灵活性，具体情况具体考虑。

#### 12. Dubbo负载均衡

Random LoadBalance(默认，基于权重的随机负载均衡机制)

RoundRobin LoadBalance(不推荐，基于权重的轮询负载均衡机制)

LeastActive LoadBalance 最少活跃调用数

ConsistentHash LoadBalance **一致性 Hash**

#### 13. Dubbo 参数透传

如果需要查看一次调用的全链路日志，则一般的做法是通过在系统边界中产生一个 **`traceId`**，向调用链的后续服务传递 **`traceId`**，后续服务继续使用 **`traceId`** 打印日志，并再向其他后续服务传递 **`traceId`**，此过程简称，**traceId透传**。

1. 基于RpcContext实现
2. 基于Filter实现

#### 14. Dubbo Filter

#### 15. **Dubbo的集群容错方案有哪些？**

