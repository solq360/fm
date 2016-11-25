package org.solq.fm.common.socket.netty;

import org.junit.Test;
import org.solq.fm.common.socket.config.SocketConfig;
import org.solq.fm.common.socket.netty.handle.EchoServerHandler;
import org.solq.fm.websocket.HttpRequestHandler;
import org.solq.fm.websocket.TextWebSocketFrameHandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.ImmediateEventExecutor;

public class TestNettyServer {
    private SocketConfig config = SocketConfig.of("127.0.0.1:8899");

    @Test
    public void test1() {
	final ChannelGroup group = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

	NettySocketServer sokcet = NettySocketServer.of(config, new ChannelInitializer<Channel>() {

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
	sokcet.start();
    }

    @Test
    public void test_echo() {
	EchoServerHandler handle = new EchoServerHandler();
	NettySocketServer sokcet = NettySocketServer.of(config, new ChannelInitializer<Channel>() {

	    @Override
	    protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(handle);
	    }
	});
	sokcet.start();
	sokcet.sync();
    }
}
