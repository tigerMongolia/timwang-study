package com.tim.wang.sourcecode.netty.transport;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 正如你所知的，你可以增加多个 ChannelHandler 到ChannelPipeline ,
 * 因此你可以把一整个ChannelHandler 拆分成多个模块以减少应用的复杂程度，比如你可以把TimeClientHandler 拆分成2个处理器：
 *
 * TimeDecoder 处理数据拆分的问题
 * TimeClientHandler 原始版本的实现
 *
 * ByteToMessageDecoder 是 ChannelInboundHandler 的一个实现类，他可以在处理数据拆分的问题上变得很简单。
 * @author wangjun
 * @date 2020-08-08
 */
public class TimeDecoder extends ByteToMessageDecoder {

    /**
     * 每当有新数据接收的时候，ByteToMessageDecoder 都会调用 decode() 方法来处理内部的那个累积缓冲
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        /*
         * Decode() 方法可以决定当累积缓冲里没有足够数据时可以往 out 对象里放任意数据。
         * 当有更多的数据被接收了 ByteToMessageDecoder 会再一次调用 decode() 方法。
         */
        if (byteBuf.readableBytes() < 4) {
            return;
        }
        /*
         * 如果在 decode() 方法里增加了一个对象到 out 对象里，
         * 这意味着解码器解码消息成功。ByteToMessageDecoder 将会丢弃在累积缓冲里已经被读过的数据。
         * 请记得你不需要对多条消息调用 decode()，ByteToMessageDecoder 会持续调用 decode() 直到不放任何数据到 out 里。
         */
        list.add(byteBuf.readBytes(4));
    }
}
