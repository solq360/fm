package org.solq.fm.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TestHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
	System.out.println("channelRead0");
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
	System.out.println("channelRegistered : ");

	ctx.fireChannelRegistered();

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
	System.out.println("channelUnregistered");

	ctx.fireChannelUnregistered();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
	System.out.println("channelActive");

	ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
	System.out.println("channelInactive");

	ctx.fireChannelInactive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
	System.out.println("channelRead");

	ctx.fireChannelRead(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
	System.out.println("channelReadComplete");

	ctx.fireChannelReadComplete();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
	System.out.println("userEventTriggered");

	ctx.fireUserEventTriggered(evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
	System.out.println("channelWritabilityChanged");

	ctx.fireChannelWritabilityChanged();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
	System.out.println("exceptionCaught");

	ctx.fireExceptionCaught(cause);
    }

}