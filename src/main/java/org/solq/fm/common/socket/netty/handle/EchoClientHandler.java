package org.solq.fm.common.socket.netty.handle;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {
    private final ByteBuf firstMessage;

    public EchoClientHandler() {
	firstMessage = Unpooled.buffer(256);
	for (int i = 0; i < firstMessage.capacity(); i++) {
	    firstMessage.writeByte((byte) i);
	}
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
	ctx.writeAndFlush(firstMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
 	System.out.println(msg.toString());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
	ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	cause.printStackTrace();
	ctx.close();
    }
}