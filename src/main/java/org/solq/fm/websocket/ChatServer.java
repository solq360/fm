package org.solq.fm.websocket;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.ImmediateEventExecutor;

public class ChatServer {
    private final ChannelGroup group = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private Channel channel;

    public ChannelFuture start(InetSocketAddress address) {
	ServerBootstrap boot = new ServerBootstrap();
	boot.group(workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<Channel>() {

	    @Override
	    protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		pipeline.addLast(new HttpServerCodec());
		pipeline.addLast(new ChunkedWriteHandler());
		pipeline.addLast(new HttpObjectAggregator(64 * 1024));
		

		pipeline.addLast(new HttpRequestHandler("/ws"));
		pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
		pipeline.addLast(new TextWebSocketFrameHandler(group));
	    }
	});

	ChannelFuture f = boot.bind(address).syncUninterruptibly();
	channel = f.channel();
	return f;
    }

    public void destroy() {
	if (channel != null)
	    channel.close();
	group.close();
	workerGroup.shutdownGracefully();
    }

    public static void main(String[] args) {
	final ChatServer server = new ChatServer();
	ChannelFuture f = server.start(new InetSocketAddress(2048));
	System.out.println("server start................");
	Runtime.getRuntime().addShutdownHook(new Thread() {
	    @Override
	    public void run() {
		server.destroy();
	    }
	});
	f.channel().closeFuture().syncUninterruptibly();
    }

}