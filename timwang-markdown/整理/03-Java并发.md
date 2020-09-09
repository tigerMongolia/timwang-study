#### 1. ThreadPoolExecutor

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

#### 2. 什么是悲观锁和乐观锁

#### 3. 可重入锁和读写锁的区别，哪个效率更高，高并发的场景用哪个更好

#### 4. 在 Java 中 Executor 和 Executors 的区别？

#### 5. 并发编程三要素？

#### 6. 什么是线程组，为什么在 Java 中不推荐使用？

#### 7. 在 java 中守护线程和本地线程区别？

#### 8. 什么是多线程中的上下文切换？

#### 9. Java 中用到的线程调度算法是什么？

