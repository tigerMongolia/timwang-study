### 一、Selector

Selector 允许一个单一的线程来操作多个 Channel. 如果我们的应用程序中使用了多个 Channel, 那么使用 Selector 很方便的实现这样的目的, 但是因为在一个线程中使用了多个 Channel, 因此也会造成了每个 Channel 传输效率的降低.

为了使用 Selector, 我们首先需要将 Channel 注册到 Selector 中, 随后调用 Selector 的 select()方法, 这个方法会阻塞, 直到注册在 Selector 中的 Channel 发送可读写事件. 当这个方法返回后, 当前的这个线程就可以处理 Channel 的事件了

##### 1.1 使用步骤

**创建选择器**

```java
//通过 Selector.open()方法, 我们可以创建一个选择器:
Selector selector = Selector.open();
```

**将Channel注册到选择器中**

为了使用选择器管理 Channel, 我们需要将 Channel 注册到选择器中

```java
channel.configureBlocking(false);
SelectionKey key = channel.register(selector, SelectionKey.OP_READ);
```

> 注意, 如果一个 Channel 要注册到 Selector 中, 那么这个 Channel 必须是非阻塞的, 即channel.configureBlocking(false); 因为 Channel 必须要是非阻塞的, 因此 FileChannel 是不能够使用选择器的, 因为 FileChannel 都是阻塞的

注意到, 在使用 Channel.register()方法时, 第二个参数指定了我们对 Channel 的什么类型的事件感兴趣, 这些事件有:

- Connect, 即连接事件(TCP 连接), 对应于SelectionKey.OP_CONNECT
- Accept, 即确认事件, 对应于SelectionKey.OP_ACCEPT
- Read, 即读事件, 对应于SelectionKey.OP_READ, 表示 buffer 可读.
- Write, 即写事件, 对应于SelectionKey.OP_WRITE, 表示 buffer 可写.

一个 Channel发出一个事件也可以称为 对于某个事件, Channel 准备好了. 因此一个 Channel 成功连接到了另一个服务器也可以被称为 connect ready. 我们可以使用或运算|来组合多个事件, 例如:

```java
int interestSet = SelectionKey.OP_READ | SelectionKey.OP_WRITE;
```

> 注意, 一个 Channel 仅仅可以被注册到一个 Selector 一次, 如果将 Channel 注册到 Selector 多次, 那么其实就是相当于更新 SelectionKey 的 interest set. 例如:

```java
channel.register(selector, SelectionKey.OP_READ);
channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
```

上面的 channel 注册到同一个 Selector 两次了, 那么第二次的注册其实就是相当于更新这个 Channel 的 interest set 为 SelectionKey.OP_READ | SelectionKey.OP_WRITE

#### 二、SelectionKey

当我们使用 register 注册一个 Channel 时, 会返回一个 SelectionKey 对象, 这个对象包含了如下内容:

- interest set, 即我们感兴趣的事件集, 即在调用 register 注册 channel 时所设置的 interest set.
- ready set
- channel
- selector
- attached object, 可选的附加对象

##### 2.1 interest set

我们可以通过如下方式获取 interest set:

```java
int interestSet = selectionKey.interestOps();

boolean isInterestedInAccept  = interestSet & SelectionKey.OP_ACCEPT;
boolean isInterestedInConnect = interestSet & SelectionKey.OP_CONNECT;
boolean isInterestedInRead    = interestSet & SelectionKey.OP_READ;
boolean isInterestedInWrite   = interestSet & SelectionKey.OP_WRITE;  
```

##### 2.2 ready set

代表了 Channel 所准备好了的操作.我们可以像判断 interest set 一样操作 Ready set, 但是我们还可以使用如下方法进行判断:

```java
int readySet = selectionKey.readyOps();

selectionKey.isAcceptable();
selectionKey.isConnectable();
selectionKey.isReadable();
selectionKey.isWritable();
```

##### 2.3 Channel 和 Selector

我们可以通过 SelectionKey 获取相对应的 Channel 和 Selector:

```java
Channel  channel  = selectionKey.channel();
Selector selector = selectionKey.selector();
```

##### 2.4 Attaching Object

我们可以在selectionKey中附加一个对象:

```java
selectionKey.attach(theObject);
Object attachedObj = selectionKey.attachment();
```

或者在注册时直接附加:

```java
SelectionKey key = channel.register(selector, SelectionKey.OP_READ, theObject);
```

#### 三、通过 Selector 选择 Channel

我们可以通过 Selector.select()方法获取对某件事件准备好了的 Channel, 即如果我们在注册 Channel 时, 对其的可写事件感兴趣, 那么当 select()返回时, 我们就可以获取 Channel 了.

> 注意, select()方法返回的值表示有多少个 Channel 可操作.

##### 3.1 获取可操作的 Channel

如果 select()方法返回值表示有多个 Channel 准备好了, 那么我们可以通过 Selected key set 访问这个 Channel:

```java
Set<SelectionKey> selectedKeys = selector.selectedKeys();

Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

while(keyIterator.hasNext()) {
    
    SelectionKey key = keyIterator.next();

    if(key.isAcceptable()) {
        // a connection was accepted by a ServerSocketChannel.

    } else if (key.isConnectable()) {
        // a connection was established with a remote server.

    } else if (key.isReadable()) {
        // a channel is ready for reading

    } else if (key.isWritable()) {
        // a channel is ready for writing
    }

    keyIterator.remove();
}
```

> 
> 注意, 在每次迭代时, 我们都调用 "keyIterator.remove()" 将这个 key 从迭代器中删除, 因为 select() 方法仅仅是简单地将就绪的 IO 操作放到 selectedKeys 集合中, 因此如果我们从 selectedKeys 获取到一个 key, 但是没有将它删除, 那么下一次 select 时, 这个 key 所对应的 IO 事件还在 selectedKeys 中. 例如此时我们收到 OP_ACCEPT 通知, 然后我们进行相关处理, 但是并没有将这个 Key 从 SelectedKeys 中删除, 那么下一次 select() 返回时 我们还可以在 SelectedKeys 中获取到 OP_ACCEPT 的 key.

> 注意, 我们可以动态更改 SekectedKeys 中的 key 的 interest set. 例如在 OP_ACCEPT 中, 我们可以将 interest set 更新为 OP_READ, 这样 Selector 就会将这个 Channel 的 读 IO 就绪事件包含进来了

#### 四、Selector 的基本使用流程

1. 通过 Selector.open() 打开一个 Selector.
2. 将 Channel 注册到 Selector 中, 并设置需要监听的事件(interest set)
3. 不断重复:
   - 调用 select() 方法
   - 调用 selector.selectedKeys() 获取 selected keys
   - 迭代每个 selected key:
     - 从 selected key 中获取 对应的 Channel 和附加信息(如果有的话)
     - 判断是哪些 IO 事件已经就绪了, 然后处理它们. 如果是 OP_ACCEPT 事件, 则调用 "SocketChannel clientChannel = ((ServerSocketChannel) key.channel()).accept()" 获取 SocketChannel, 并将它设置为 非阻塞的, 然后将这个 Channel 注册到 Selector 中.
     - 根据需要更改 selected key 的监听事件.
     - 将已经处理过的 key 从 selected keys 集合中删除.

当调用了 Selector.close()方法时, 我们其实是关闭了 Selector 本身并且将所有的 SelectionKey 失效, 但是并不会关闭 Channel.





https://github.com/bingbo/blog/wiki/Java_nio

https://github.com/feixueck/whatsmars/wiki/NIO-NIO-vs.-IO

https://wiki.jikexueyuan.com/project/java-nio-zh/java-nio-buffer.html