package org.solq.fm.common.socket.netty;

import org.solq.fm.common.socket.AbsSocket;
import org.solq.fm.common.socket.client.model.ISocketClient;
import org.solq.fm.common.socket.config.SocketConfig;
import org.solq.fm.common.socket.session.ISession;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

public class NettySocketClient extends AbsSocket implements ISocketClient {
    private Bootstrap clientBoot;
    private Channel channel;

    @Override
    public void send(Object message) {
	if (!isConnect()) {
	    start();
	}
	channel.writeAndFlush(message);
    }

    @Override
    public boolean isClose() {
	return channel == null || !channel.isActive();
    }

    @Override
    protected void doStart() {
	if (isConnect()) {
	    return;
	}
	if (clientBoot == null) {
	    throw new RuntimeException("NettySocketClient 未初始化 clientBoot");
	}
	ChannelFuture f = clientBoot.connect(socketConfig.toInetSocketAddress()).syncUninterruptibly();
	channel = f.channel();
    }

    @Override
    protected void doStop() {
	if (channel != null) {
	    channel.close();
	}
    }

    @Override
    public boolean isConnect() {
	return channel != null && channel.isActive();
    }

    public static ISocketClient of(Bootstrap clientBoot, SocketConfig clientSocketConfig, Channel channel, ISession session) {
	NettySocketClient ret = new NettySocketClient();
	ret.clientBoot = clientBoot;
	ret.socketConfig = clientSocketConfig;
	ret.channel = channel;
	ret.session = session;
	return ret;
    }

    public static ISocketClient of(Bootstrap clientBoot, SocketConfig clientSocketConfig, ISession session) {
	NettySocketClient ret = new NettySocketClient();
	ret.clientBoot = clientBoot;
	ret.socketConfig = clientSocketConfig;
	ret.session = session;
	return ret;
    }

    @Override
    public void sync() {
	try {
	    channel.closeFuture().sync();
	} catch (InterruptedException e) {
 	    e.printStackTrace();
	}
    }

}
