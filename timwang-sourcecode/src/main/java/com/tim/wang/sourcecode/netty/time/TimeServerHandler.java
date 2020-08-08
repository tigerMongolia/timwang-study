package com.tim.wang.sourcecode.netty.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * https://github.com/waylau/netty-4-user-guide-demos/tree/master/netty4-demos/src/main/java/com/waylau/netty/demo/time
 * @author wangjun
 * @date 2020-08-08
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * channelActive() 方法将会在连接被建立并且准备进行通信时被调用。因此让我们在这个方法里完成一个代表当前时间的32位整数消息的构建工作。
     * 为了发送一个新的消息，我们需要分配一个包含这个消息的新的缓冲。因为我们需要写入一个32位的整数，因此我们需要一个至少有4个字节的 ByteBuf。
     * 通过 ChannelHandlerContext.alloc() 得到一个当前的ByteBufAllocator，然后分配一个新的缓冲。
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        final ByteBuf time = ctx.alloc().buffer(4);
        /*
         * 这里为什么要加2208988800L，是因为格林威治时间是从1970-01-01开始的
         *  the time  2,208,988,800 corresponds to 00:00  1 Jan 1970 GMT,
         */
        time.writeInt((int) ((System.currentTimeMillis()) / 1000L + 2208988800L));
        /*
         * 和往常一样我们需要编写一个构建好的消息。但是等一等，flip 在哪？
         * 难道我们使用 NIO 发送消息时不是调用 java.nio.ByteBuffer.flip() 吗？
         * ByteBuf 之所以没有这个方法因为有两个指针，一个对应读操作一个对应写操作。
         * 当你向 ByteBuf 里写入数据的时候写指针的索引就会增加，同时读指针的索引没有变化。
         * 读指针索引和写指针索引分别代表了消息的开始和结束。
         */
        ChannelFuture future = ctx.writeAndFlush(time);
        /*
         * 另外一个点需要注意的是 ChannelHandlerContext.write()
         * (和 writeAndFlush() )方法会返回一个 ChannelFuture 对象，
         * 一个 ChannelFuture 代表了一个还没有发生的 I/O 操作。这意味着任何一个请求操作都不会马上被执行，
         * 因为在 Netty 里所有的操作都是异步的。举个例子下面的代码中在消息被发送之前可能会先关闭连接。
         */
        future.addListener(ChannelFutureListener.CLOSE);
        /*
         * 这段代码其实和上面的是一个意思
         */
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) {
                ctx.close();
            }
        });

        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
