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

#### 三、通过 Selector 选择 Channel

#### 四、Selector 的基本使用流程

