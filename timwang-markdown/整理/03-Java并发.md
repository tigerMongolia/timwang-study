#### 一、ThreadPoolExecutor

##### 1.1 线程池创建参数

corePoolSize：    线程池维护线程的最少数量 （core : 核心）
maximumPoolSize：  线程池维护线程的最大数量 
keepAliveTime：   线程池维护线程所允许的空闲时间
unit：        线程池维护线程所允许的空闲时间的单位
workQueue：     线程池所使用的缓冲队列
handler：       线程池对拒绝任务的处理策略

##### 1.2 线程池处理优先级

核心线程corePoolSize、任务队列workQueue、最大线程maximumPoolSize，如果三者都满了，使用handler处理被拒绝的任务。

##### 1.3 拒绝策略

- ThreadPoolExecutor.AbortPolicy()：   抛出java.util.concurrent.RejectedExecutionException异常
- ThreadPoolExecutor.CallerRunsPolicy():   重试添加当前的任务，他会自动重复调用execute()方法
- ThreadPoolExecutor.DiscardOldestPolicy():   抛弃旧的任务
- ThreadPoolExecutor.DiscardPolicy():   抛弃当前的任务

#### 二、什么是悲观锁和乐观锁

#### 三、可重入锁和读写锁的区别，哪个效率更高，高并发的场景用哪个更好

