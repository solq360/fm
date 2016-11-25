package org.solq.fm.common.socket.netty;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.solq.fm.common.socket.AbsSocket;
import org.solq.fm.common.socket.ISocket;
import org.solq.fm.common.socket.channel.ChannelManager;
import org.solq.fm.common.socket.client.model.ISocketClient;
import org.solq.fm.common.socket.config.SocketConfig;
import org.solq.fm.common.socket.server.model.ISocketServer;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

/***
 * 具有主动跟被动连接
 * 
 * @author solq
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class NettySocketServer extends AbsSocket implements ISocketServer {

    private final Map<ChannelOption, Object> childOptions = new LinkedHashMap<>();
    private final Map<AttributeKey, Object> childAttrs = new LinkedHashMap<>();

    private final EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
    private final EventLoopGroup handleGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 4);

    private Channel channel;
    private ServerBootstrap serverBoot;
    private Bootstrap clientBoot;

    private ChannelHandler serverChildHandler;
    private ChannelHandler clientChildHandler;

    private ChannelManager channelManager = new ChannelManager();

    @Override
    public void send(Object message) {
	throw new RuntimeException("server 不能主动发送");
    }

    @Override
    protected void doStart() {
	channel = startServer();
    }

    private Channel startServer() {
	if (serverBoot == null) {
	    serverBoot = new ServerBootstrap();
	    serverBoot.group(workerGroup, handleGroup).channel(NioServerSocketChannel.class).childHandler(serverChildHandler);
	    for (Entry<ChannelOption, Object> entry : childOptions.entrySet()) {
		serverBoot.childOption(entry.getKey(), entry.getValue());
	    }

	    for (Entry<AttributeKey, Object> entry : childAttrs.entrySet()) {
		serverBoot.childAttr(entry.getKey(), entry.getValue());
	    }
	}

	ChannelFuture f = serverBoot.bind(this.socketConfig.toInetSocketAddress()).syncUninterruptibly();
	return f.channel();
    }

    private Channel startClient(SocketConfig socketConfig) {
	if (clientBoot == null) {
	    clientBoot = new Bootstrap();
	    clientBoot.group(handleGroup).channel(NioSocketChannel.class).handler(clientChildHandler);
	    for (Entry<ChannelOption, Object> entry : childOptions.entrySet()) {
		clientBoot.option(entry.getKey(), entry.getValue());
	    }

	    for (Entry<AttributeKey, Object> entry : childAttrs.entrySet()) {
		clientBoot.attr(entry.getKey(), entry.getValue());
	    }
	}

	ChannelFuture f = clientBoot.connect(socketConfig.toInetSocketAddress()).syncUninterruptibly();
	return f.channel();
    }

    @Override
    protected void doStop() {
	channelManager.closeChannelAndSokcetAll();
	if (channel != null) {
	    channel.close();
	}
	workerGroup.shutdownGracefully();
	handleGroup.shutdownGracefully();
    }

    public static NettySocketServer of(SocketConfig socketConfig, ChannelHandler handler) {
	NettySocketServer ret = new NettySocketServer();
	ret.socketConfig = socketConfig;
	ret.serverChildHandler = handler;
	return ret;
    }

    @Override
    public ISocketClient openSocket(SocketConfig clientSocketConfig) {
	Channel channel = startClient(clientSocketConfig);
	ISocketClient ret = NettySocketClient.of(this.clientBoot, clientSocketConfig, channel, null);
	channelManager.joinChannel(ret, ChannelManager.CHANNEL_DEFAULT_NAME);
	return ret;
    }

    @Override
    public void closeSocket(ISocket client) {
	channelManager.removeSocket(client);
	client.stop();
    }

    @Override
    public void sendAll(Object message) {
	channelManager.sendAll(message);
    }

    @Override
    public void send(ISocket socket, Object message) {
	socket.send(message);
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
