##### 1. 事务简介

###### 1.1 什么是事务

redis执行指令过程中， 多条连续执行的指令被干扰， 打断， 插队

redis事务就是一个命令执行的队列， 将一系列预定义命令包装成一个整体(一个队列) 。当执行时，一次性按照添加顺序依次执行，中间不会被打断或者干扰。

一个队列中，一次性、顺序性、排他性的执行一系列命令

![f0ff77e44996432078f155e3a7b40a04.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5vwaenkdj30u404sjrl.jpg)

###### 1.2 事务的基本操作-开启

- 开启事务

```
multi
```

- 作用

设定事务的开启位置，此指令执行后，后续的所有指令均加入到事务中

###### 1.3 事务的基本操作-关闭

- 执行事务

```
exec
```

- 作用

设定事务的结束位置， 同时执行事务。与multi成对出现， 成对使用

注意：加入事务的命令暂时进入到任务队列中， 并没有立即执行，只有执行exec命令才开始执行

![e654ea8472871eb0c6e21f4e058e1a33.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5vwkinz0j30ey0dyglx.jpg)

###### 1.4 事务的基本操作-取消

定义事务的过程中，怎么办?

- 取消事务

```
discard
```

- 作用

终止当前事务的定义，发生在multi之后，exec之前

###### 1.5 事务的工作流程

![cdc0862f427c01c2e6adaf5286e83610.png](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh5vwtpxzoj30ve0fcaar.jpg)

##### 2. 事务的注意事项

###### 2.1 定义事务的过程中，命令格式输入错误怎么办?

- 语法错误

	指命令书写格式有误

- 处理结果

	如果定义的事务中所包含的命令存在语法错误，整体事务中所有命令均不会执行。包括那些语法正确的命令。

###### 2.2 定义事务的过程中，命令执行出现错误怎么办?

- 运行错误
	
	指命令格式正确， 但是无法正确的执行。例如对list进行incr操作

- 处理结果

	能够正确运行的命令会执行，运行错误的命令不会被执行


注意：已经执行完毕的命令对应的数据不会自动回滚，需要程序员自己在代码中实现回滚。

###### 2.3 手动进行事务回滚

- 记录操作过程中被影响的数据之前的状态
	
	- 单数据：string
	- 多数据：hash、list、set、z set

- 设置指令恢复所有的被修改的项

	- 单数据：直接set(注意周边属性， 例如时效)
	- 多数据：修改对应值或整体克隆复制