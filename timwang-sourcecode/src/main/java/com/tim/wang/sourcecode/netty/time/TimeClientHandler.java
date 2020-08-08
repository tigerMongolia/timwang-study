package com.tim.wang.sourcecode.netty.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * https://tools.ietf.org/html/rfc868
 * 1.BootStrap 和 ServerBootstrap 类似,不过他是对非服务端的 channel 而言，比如客户端或者无连接传输模式的 channel。
 * 2.如果你只指定了一个 EventLoopGroup，那他就会即作为一个 boss group ，也会作为一个 worker group，尽管客户端不需要使用到 boss worker 。
 * 3.代替NioServerSocketChannel的是NioSocketChannel,这个类在客户端channel 被创建时使用。
 * 4.不像在使用 ServerBootstrap 时需要用 childOption() 方法，因为客户端的 SocketChannel 没有父亲。
 * 5.我们用 connect() 方法代替了 bind() 方法。
 * 正如你看到的，他和服务端的代码是不一样的。ChannelHandler 是如何实现的?他应该从服务端接受一个32位的整数消息，
 * 把他翻译成人们能读懂的格式，并打印翻译好的时间，最后关闭连接:
 * @author wangjun
 * @date 2020-08-08
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf m = (ByteBuf) msg;
        try {
            long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
            System.out.println(new Date(currentTimeMillis));
            ctx.close();
        } finally {
            m.release();
        }
    }
}
