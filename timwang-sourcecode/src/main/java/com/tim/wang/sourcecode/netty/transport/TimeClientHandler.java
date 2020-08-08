package com.tim.wang.sourcecode.netty.transport;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * 基于流的传输比如 TCP/IP, 接收到数据是存在 socket 接收的 buffer 中。
 * 不幸的是，基于流的传输并不是一个数据包队列，而是一个字节队列。意味着，即使你发送了2个独立的数据包，
 * 操作系统也不会作为2个消息处理而仅仅是作为一连串的字节而言。因此这是不能保证你远程写入的数据就会准确地读取。
 * 举个例子，让我们假设操作系统的 TCP/TP 协议栈已经接收了3个数据包：
 *
 * 由于基于流传输的协议的这种普通的性质，在你的应用程序里读取数据的时候会有很高的可能性被分成下面的片段
 * 因此，一个接收方不管他是客户端还是服务端，都应该把接收到的数据整理成一个或者多个更有意思并且能够让程序的业务逻辑更好理解的数据。
 * 在上面的例子中，接收到的数据应该被构造成下面的格式：
 *
 * @author wangjun
 * @date 2020-08-08
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {
    /**
     * 最简单的方案是构造一个内部的可积累的缓冲，直到4个字节全部接收到了内部缓冲。
     * 下面的代码修改了 TimeClientHandler 的实现类修复了这个问题
     */
    private ByteBuf buf;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        buf = ctx.alloc().buffer(4);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        /*
         * ChannelHandler 有2个生命周期的监听方法：handlerAdded()和 handlerRemoved()。
         * 你可以完成任意初始化任务只要他不会被阻塞很长的时间。
         */
        buf.release();
        buf = null;
    }

    /**
     * 首先，所有接收的数据都应该被累积在 buf 变量里。
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf m = (ByteBuf) msg;
        buf.writeBytes(m);
        m.release();
        /*
         * 然后，处理器必须检查 buf 变量是否有足够的数据，
         * 在这个例子中是4个字节，然后处理实际的业务逻辑。否则，Netty 会重复调用channelRead() 当有更多数据到达直到4个字节的数据被积累。
         */
        if (buf.readableBytes() >= 4) {
            long currentTimeMillis = (buf.readUnsignedInt() - 2208988800L) * 1000L;
            System.out.println(new Date(currentTimeMillis));
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
