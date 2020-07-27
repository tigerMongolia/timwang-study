#### 1. 分布式锁

##### 1.1 简介

**锁** 是一种用来解决多个执行线程 **访问共享资源** 错误或数据不一致问题的工具

如果 把一台服务器比作一个房子，那么 线程就好比里面的住户，当他们想要共同访问一个共享资源，例如厕所的时候，如果厕所门上没有锁...更甚者厕所没装门...这是会出原则性的问题的..

装上了锁，大家用起来就安心多了，本质也就是 同一时间只允许一个住户使用。

而随着互联网世界的发展，单体应用已经越来越无法满足复杂互联网的高并发需求，转而慢慢朝着分布式方向发展，慢慢进化成了 更大一些的住户。所以同样，我们需要引入分布式锁来解决分布式应用之间访问共享资源的并发问题。

##### 1.2 为何需要分布式锁

一般情况下，我们使用分布式锁主要有两个场景：

1. 避免不同节点重复相同的工作：比如用户执行了某个操作有可能不同节点会发送多封邮件；
2. 避免破坏数据的正确性：如果两个节点在同一条数据上同时进行操作，可能会造成数据错误或不一致的情况出现；

##### 1.3 Java 中实现的常见方式

上面我们用简单的比喻说明了锁的本质：同一时间只允许一个用户操作。所以理论上，能够满足这个需求的工具我们都能够使用 (就是其他应用能帮我们加锁的)：

1. 基于 MySQL 中的锁：MySQL 本身有自带的悲观锁 for update 关键字，也可以自己实现悲观/乐观锁来达到目的；
2. 基于 Zookeeper 有序节点：Zookeeper 允许临时创建有序的子节点，这样客户端获取节点列表时，就能够当前子节点列表中的序号判断是否能够获得锁；
3. 基于 Redis 的单线程：由于 Redis 是单线程，所以命令会以串行的方式执行，并且本身提供了像 SETNX(set if not exists) 这样的指令，本身具有互斥性；

每个方案都有各自的优缺点，例如 MySQL 虽然直观理解容易，但是实现起来却需要额外考虑 锁超时、加事务 等，并且性能局限于数据库，诸如此类我们在此不作讨论，重点关注 Redis。

##### 1.4 Redis分布式锁的问题

###### 1.4.1 锁超时


假设现在我们有两台平行的服务 A B，其中 A 服务在 获取锁之后 由于未知神秘力量突然 挂了，那么 B 服务就永远无法获取到锁了：

![178f44279deeac3b71e6749551fcecbd.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5wdlmsezj30u00cj74h.jpg)

所以我们需要额外设置一个超时时间，来保证服务的可用性。

但是另一个问题随即而来：如果在加锁和释放锁之间的逻辑执行得太长，以至于超出了锁的超时限制，也会出现问题。因为这时候第一个线程持有锁过期了，而临界区的逻辑还没有执行完，与此同时第二个线程就提前拥有了这把锁，导致临界区的代码不能得到严格的串行执行。

为了避免这个问题，Redis 分布式锁不要用于较长时间的任务。如果真的偶尔出现了问题，造成的数据小错乱可能就需要人工的干预。

有一个稍微安全一点的方案是 将锁的 value 值设置为一个随机数，释放锁时先匹配随机数是否一致，然后再删除 key，这是为了 确保当前线程占有的锁不会被其他线程释放，除非这个锁是因为过期了而被服务器自动释放的。

但是匹配 value 和删除 key 在 Redis 中并不是一个原子性的操作，也没有类似保证原子性的指令，所以可能需要使用像 Lua 这样的脚本来处理了，因为 Lua 脚本可以 保证多个指令的原子性执行。

###### 1.4.2 GC 可能引发的安全问题

Martin Kleppmann 曾与 Redis 之父 Antirez 就 Redis 实现分布式锁的安全性问题进行过深入的讨论，其中有一个问题就涉及到 GC。

熟悉 Java 的同学肯定对 GC 不陌生，在 GC 的时候会发生 STW(Stop-The-World)，这本身是为了保障垃圾回收器的正常执行，但可能会引发如下的问题：

![45a340a05a097c9e533d6fe8d7350491.jpeg](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5wduwdzjj30u00hijrx.jpg)

服务 A 获取了锁并设置了超时时间，但是服务 A 出现了 STW 且时间较长，导致了分布式锁进行了超时释放，在这个期间服务 B 获取到了锁，待服务 A STW 结束之后又恢复了锁，这就导致了 服务 A 和服务 B 同时获取到了锁，这个时候分布式锁就不安全了。

不仅仅局限于 Redis，Zookeeper 和 MySQL 有同样的问题。

想吃更多瓜的童鞋，可以访问下列网站看看 Redis 之父 Antirez 怎么说：http://antirez.com/news/101

###### 1.4.3 单点/多点问题

如果 Redis 采用单机部署模式，那就意味着当 Redis 故障了，就会导致整个服务不可用。

而如果采用主从模式部署，我们想象一个这样的场景：服务 A 申请到一把锁之后，如果作为主机的 Redis 宕机了，那么 服务 B 在申请锁的时候就会从从机那里获取到这把锁，为了解决这个问题，Redis 作者提出了一种 RedLock 红锁 的算法 (Redission 同 Jedis)：

```
// 三个 Redis 集群
RLock lock1 = redissionInstance1.getLock("lock1");
RLock lock2 = redissionInstance2.getLock("lock2");
RLock lock3 = redissionInstance3.getLock("lock3");

RedissionRedLock lock = new RedissionLock(lock1, lock2, lock2);
lock.lock();
// do something....
lock.unlock();
```

#### 2. SetNX

目前通常所说的 Setnx 命令，并非单指 Redis 的 setnx key value 这条命令。


一般代指 Redis 中对 Set 命令加上 NX 参数进行使用，Set 这个命令，目前已经支持这么多参数可选：

```
SET key value [EX seconds|PX milliseconds] [NX|XX] [KEEPTTL]

```

![79c71c97dc1ea548ce24e449b1717951.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5we55kojj317y0tawfu.jpg)

主要依托了它的 Key 不存在才能 Set 成功的特性，进程 A 拿到锁，在没有删除锁的 Key 时，进程 B 自然获取锁就失败了。


那么为什么要使用 PX 30000 去设置一个超时时间？是怕进程 A 不讲道理啊，锁没等释放呢，万一崩了，直接原地把锁带走了，导致系统中谁也拿不到锁。

就算这样，还是不能保证万无一失。如果进程 A 又不讲道理，操作锁内资源超过笔者设置的超时时间，那么就会导致其他进程拿到锁，等进程 A 回来了，回手就是把其他进程的锁删了，如图：

![0e0b5f4344c6c8a737605bf1cc08cde2.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5webu0lmj317k0kcq42.jpg)


还是刚才那张图，将 T5 时刻改成了锁超时，被 Redis 释放。


进程 B 在 T6 开开心心拿到锁不到一会，进程 A 操作完成，回手一个 Del，就把锁释放了。


当进程 B 操作完成，去释放锁的时候（图中 T8 时刻）：


找不到锁其实还算好的，万一 T7 时刻有个进程 C 过来加锁成功，那么进程 B 就把进程 C 的锁释放了。


以此类推，进程 C 可能释放进程 D 的锁，进程 D....(禁止套娃)，具体什么后果就不得而知了。


所以在用 Setnx 的时候，Key 虽然是主要作用，但是 Value 也不能闲着，可以设置一个唯一的客户端 ID，或者用 UUID 这种随机数。


当解锁的时候，先获取 Value 判断是否是当前进程加的锁，再去删除。伪代码：

```
String uuid = xxxx;
// 伪代码，具体实现看项目中用的连接工具
// 有的提供的方法名为set 有的叫setIfAbsent
set Test uuid NX PX 3000
try{
// biz handle....
} finally {
    // unlock
    if(uuid.equals(redisTool.get('Test')){
        redisTool.del('Test');
    }
}
```

为什么有问题还说这么多呢？有如下两点原因：


- 搞清劣势所在，才能更好的完善。
- 上文中最后这段代码，还是有很多公司在用的。

那么删除锁的正确姿势之一，就是可以使用 Lua 脚本，通过 Redis 的 eval/evalsha 命令来运行：

```
-- lua删除锁：
-- KEYS和ARGV分别是以集合方式传入的参数，对应上文的Test和uuid。
-- 如果对应的value等于传入的uuid。
if redis.call('get', KEYS[1]) == ARGV[1] 
    then 
    -- 执行删除操作
        return redis.call('del', KEYS[1]) 
    else 
    -- 不成功，返回0
        return 0 
end
```

通过 Lua 脚本能保证原子性的原因说的通俗一点：就算你在 Lua 里写出花，执行也是一个命令（eval/evalsha）去执行的，一条命令没执行完，其他客户端是看不到的。


那么既然这么麻烦，有没有比较好的工具呢？就要说到 Redisson 了。

因为 Redis 版本在 2.6.12 之前，Set 是不支持 NX 参数的，如果想要完成一个锁，那么需要两条命令：
1. setnx Test uuid
2. expire Test 30


即放入 Key 和设置有效期，是分开的两步，理论上会出现 1 刚执行完，程序挂掉，无法保证原子性。


但是早在 2013 年，也就是 7 年前，Redis 就发布了 2.6.12 版本，并且官网(Set 命令页)，也早早就说明了“SETNX，SETEX，PSETEX 可能在未来的版本中，会弃用并永久删除”。

笔者曾阅读过一位大佬的文章，其中就有一句指导入门者的面试小套路，具体文字忘记了，大概意思如下：说到 Redis 锁的时候，可以先从 Setnx 讲起，最后慢慢引出 Set 命令的可以加参数，可以体现出自己的知识面。


如果有缘你也阅读过这篇文章，并且学到了这个套路，作为本文的笔者我要加一句提醒：请注意你的工作年限！首先回答官网表明即将废弃的命令，再引出 Set 命令七年前的“新特性”，如果是刚毕业不久的人这么说，面试官会以为自己穿越了。

#### 3. Redisson

Redisson 是 Java 的 Redis 客户端之一，提供了一些 API 方便操作 Redis。

但是 Redisson 这个客户端可有点厉害，笔者在官网截了仅仅是一部分的图：

![01b581c293de1f937c1ef696a4adf5c9.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5weioq4zj316h0u00ub.jpg)

这个特性列表可以说是太多了，是不是还看到了一些 JUC 包下面的类名，Redisson 帮我们搞了分布式的版本。

比如 AtomicLong，直接用 RedissonAtomicLong 就行了，连类名都不用去新记，很人性化了。


锁只是它的冰山一角，并且从它的 Wiki 页面看到，对主从，哨兵，集群等模式都支持，当然了，单节点模式肯定是支持的。


##### 3.1 Redission实现

Redisson是一个在Redis的基础上实现的Java驻内存数据网格（In-Memory Data Grid）。它不仅提供了一系列的分布式的Java常用对象，还实现了可重入锁（Reentrant Lock）、公平锁（Fair Lock、联锁（MultiLock）、 红锁（RedLock）、 读写锁（ReadWriteLock）等，还提供了许多分布式服务。

Redisson提供了使用Redis的最简单和最便捷的方法。Redisson的宗旨是促进使用者对Redis的关注分离（Separation of Concern），从而让使用者能够将精力更集中地放在处理业务逻辑上。

##### 3.2 Redisson 分布式重入锁用法

Redisson 支持单点模式、主从模式、哨兵模式、集群模式，这里以单点模式为例：

```
// 1.构造redisson实现分布式锁必要的Config
Config config = new Config();
config.useSingleServer().setAddress("redis://127.0.0.1:5379").setPassword("123456").setDatabase(0);
// 2.构造RedissonClient
RedissonClient redissonClient = Redisson.create(config);
// 3.获取锁对象实例（无法保证是按线程的顺序获取到）
RLock rLock = redissonClient.getLock(lockKey);
try {
    /**
     * 4.尝试获取锁
     * waitTimeout 尝试获取锁的最大等待时间，超过这个值，则认为获取锁失败
     * leaseTime   锁的持有时间,超过这个时间锁会自动失效（值应设置为大于业务处理的时间，确保在锁有效期内业务能处理完）
     */
    boolean res = rLock.tryLock((long)waitTimeout, (long)leaseTime, TimeUnit.SECONDS);
    if (res) {
        //成功获得锁，在这里处理业务
    }
} catch (Exception e) {
    throw new RuntimeException("aquire lock fail");
}finally{
    //无论如何, 最后都要解锁
    rLock.unlock();
}
```

![fdc5a052eaa7e2efcee96d14336fc971.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5wep55zvj312g0u0q3s.jpg)

![a785053dd13f539439257e58f3c50644.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5wf3opxyj30zu0u0wfc.jpg)

我们可以看到，RedissonLock是可重入的，并且考虑了失败重试，可以设置锁的最大等待时间， 在实现上也做了一些优化，减少了无效的锁申请，提升了资源的利用率。

需要特别注意的是，RedissonLock 同样没有解决 节点挂掉的时候，存在丢失锁的风险的问题。而现实情况是有一些场景无法容忍的，所以 Redisson 提供了实现了redlock算法的 RedissonRedLock，RedissonRedLock 真正解决了单点失败的问题，代价是需要额外的为 RedissonRedLock 搭建Redis环境。

所以，如果业务场景可以容忍这种小概率的错误，则推荐使用 RedissonLock， 如果无法容忍，则推荐使用 RedissonRedLock。


源码中加锁/释放锁操作都是用 Lua 脚本完成的，封装的非常完善，开箱即用。这里有个小细节，加锁使用 Setnx 就能实现，也采用 Lua 脚本是不是多此一举？加锁解锁的 Lua 脚本考虑的非常全面，其中就包括锁的重入性，这点可以说是考虑非常周全，我也随手写了代码测试一下：

![c94cc84c478e28af48b135e0b4dac652.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5wfapvndj30u012q75y.jpg)

#### 5. RedLock

RedLock的中文是直译过来的，就叫红锁。红锁并非是一个工具，而是 Redis 官方提出的一种分布式锁的算法。


就在刚刚介绍完的 Redisson 中，就实现了 RedLock 版本的锁。也就是说除了 getLock 方法，还有 getRedLock 方法。


笔者大概画了一下对红锁的理解：

![321486025aef33b786835ac9398fa78b.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5wfhtzmjj311j0u0wfu.jpg)

如果你不熟悉 Redis 高可用部署，那么没关系。RedLock 算法虽然是需要多个实例，但是这些实例都是独自部署的，没有主从关系。

RedLock 作者指出，之所以要用独立的，是避免了 Redis 异步复制造成的锁丢失，比如：主节点没来的及把刚刚 Set 进来这条数据给从节点，就挂了。


有些人是不是觉得大佬们都是杠精啊，天天就想着极端情况。其实高可用嘛，拼的就是 99.999...% 中小数点后面的位数。


回到上面那张简陋的图片，红锁算法认为，只要 2N+1 个节点加锁成功，那么就认为获取了锁， 解锁时将所有实例解锁。

流程为：

- 顺序向五个节点请求加锁
- 根据一定的超时时间来推断是不是跳过该节点
- 三个节点加锁成功并且花费时间小于锁的有效期
- 认定加锁成功


也就是说，假设锁 30 秒过期，三个节点加锁花了 31 秒，自然是加锁失败了。


这只是举个例子，实际上并不应该等每个节点那么长时间，就像官网所说的那样，假设有效期是 10 秒，那么单个 Redis 实例操作超时时间，应该在 5 到 50 毫秒(注意时间单位)。


还是假设我们设置有效期是 30 秒，图中超时了两个 Redis 节点。那么加锁成功的节点总共花费了 3 秒，所以锁的实际有效期是小于 27 秒的。


即扣除加锁成功三个实例的 3 秒，还要扣除等待超时 Redis 实例的总共时间。看到这，你有可能对这个算法有一些疑问，那么你不是一个人。

#### 6. Redis 分布式锁的实现

分布式锁类似于 "占坑"，而 SETNX(SET if Not eXists) 指令就是这样的一个操作，只允许被一个客户端占有，我们来看看 源码(t_string.c/setGenericCommand) 吧：

```
// SET/ SETEX/ SETTEX/ SETNX 最底层实现
void setGenericCommand(client *c, int flags, robj *key, robj *val, robj *expire, int unit, robj *ok_reply, robj *abort_reply) {
    longlong milliseconds = 0; /* initialized to avoid any harmness warning */

    // 如果定义了 key 的过期时间则保存到上面定义的变量中
    // 如果过期时间设置错误则返回错误信息
    if (expire) {
        if (getLongLongFromObjectOrReply(c, expire, &milliseconds, NULL) != C_OK)
            return;
        if (milliseconds <= 0) {
            addReplyErrorFormat(c,"invalid expire time in %s",c->cmd->name);
            return;
        }
        if (unit == UNIT_SECONDS) milliseconds *= 1000;
    }
    // lookupKeyWrite 函数是为执行写操作而取出 key 的值对象
    // 这里的判断条件是：
    // 1.如果设置了 NX(不存在)，并且在数据库中找到了 key 值
    // 2.或者设置了 XX(存在)，并且在数据库中没有找到该 key
    // => 那么回复 abort_reply 给客户端
    if ((flags & OBJ_SET_NX && lookupKeyWrite(c->db,key) != NULL) ||
        (flags & OBJ_SET_XX && lookupKeyWrite(c->db,key) == NULL))
    {
        addReply(c, abort_reply ? abort_reply : shared.null[c->resp]);
        return;
    }
    
    // 在当前的数据库中设置键为 key 值为 value 的数据
    genericSetKey(c->db,key,val,flags & OBJ_SET_KEEPTTL);
    // 服务器每修改一个 key 后都会修改 dirty 值
    server.dirty++;
    if (expire) setExpire(c,c->db,key,mstime()+milliseconds);
    notifyKeyspaceEvent(NOTIFY_STRING,"set",key,c->db->id);
    if (expire) notifyKeyspaceEvent(NOTIFY_GENERIC,
        "expire",key,c->db->id);
    addReply(c, ok_reply ? ok_reply : shared.ok);
}
```

就像上面介绍的那样，其实在之前版本的 Redis 中，由于 SETNX 和 EXPIRE 并不是 原子指令，所以在一起执行会出现问题。

也许你会想到使用 Redis 事务来解决，但在这里不行，因为 EXPIRE 命令依赖于 SETNX 的执行结果，而事务中没有 if-else 的分支逻辑，如果 SETNX 没有抢到锁，EXPIRE 就不应该执行。

为了解决这个疑难问题，Redis 开源社区涌现了许多分布式锁的 library，为了治理这个乱象，后来在 Redis 2.8 的版本中，加入了 SET 指令的扩展参数，使得 SETNX 可以和 EXPIRE 指令一起执行了：

```
> SET lock:test true ex 5 nx
OK
... do something critical ...
> del lock:test
```

你只需要符合 SET key value [EX seconds | PX milliseconds] [NX | XX] [KEEPTTL] 这样的格式就好了，你也在下方右拐参照官方的文档：

官方文档：https://redis.io/commands/set
另外，官方文档也在 SETNX 文档中提到了这样一种思路：把 SETNX 对应 key 的 value 设置为 <current Unix time + lock timeout + 1>，这样在其他客户端访问时就能够自己判断是否能够获取下一个 value 为上述格式的锁了。

##### 6.1 代码实现

下面用 Jedis 来模拟实现一下，关键代码如下：

```
privatestaticfinal String LOCK_SUCCESS = "OK";
privatestaticfinal Long RELEASE_SUCCESS = 1L;
privatestaticfinal String SET_IF_NOT_EXIST = "NX";
privatestaticfinal String SET_WITH_EXPIRE_TIME = "PX";

@Override
public String acquire() {
    try {
        // 获取锁的超时时间，超过这个时间则放弃获取锁
        long end = System.currentTimeMillis() + acquireTimeout;
        // 随机生成一个 value
        String requireToken = UUID.randomUUID().toString();
        while (System.currentTimeMillis() < end) {
            String result = jedis
                .set(lockKey, requireToken, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
            if (LOCK_SUCCESS.equals(result)) {
                return requireToken;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    } catch (Exception e) {
        log.error("acquire lock due to error", e);
    }

    returnnull;
}

@Override
public boolean release(String identify) {
    if (identify == null) {
        returnfalse;
    }

    String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
    Object result = new Object();
    try {
        result = jedis.eval(script, Collections.singletonList(lockKey),
            Collections.singletonList(identify));
        if (RELEASE_SUCCESS.equals(result)) {
            log.info("release lock success, requestToken:{}", identify);
            returntrue;
        }
    } catch (Exception e) {
        log.error("release lock due to error", e);
    } finally {
        if (jedis != null) {
            jedis.close();
        }
    }

    log.info("release lock failed, requestToken:{}, result:{}", identify, result);
    returnfalse;
}
```

#### 7. 推荐阅读

1. 【官方文档】Distributed locks with Redis - https://redis.io/topics/distlock

2. Redis【入门】就这一篇! - https://www.wmyskxz.com/2018/05/31/redis-ru-men-jiu-zhe-yi-pian/

3. Redission - Redis Java Client 源码 - https://github.com/redisson/redisson

4. 手写一个 Jedis 以及 JedisPool - https://juejin.im/post/5e5101c46fb9a07cab3a953a

#### 8. 参考资料

1. 再有人问你分布式锁，这篇文章扔给他 - https://juejin.im/post/5bbb0d8df265da0abd3533a5#heading-0
2. 【官方文档】Distributed locks with Redis - https://redis.io/topics/distlock
3. 【分布式缓存系列】Redis实现分布式锁的正确姿势 - https://www.cnblogs.com/zhili/p/redisdistributelock.html
4. Redis源码剖析和注释（九）--- 字符串命令的实现(t_string) - https://blog.csdn.net/men_wen/article/details/70325566
5. 《Redis 深度历险》 - 钱文品/ 著
