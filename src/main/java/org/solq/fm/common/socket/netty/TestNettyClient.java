package org.solq.fm.common.socket.netty;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.solq.fm.common.socket.client.model.ISocketClient;
import org.solq.fm.common.socket.config.SocketConfig;
import org.solq.fm.common.socket.netty.handle.EchoClientHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TestNettyClient {

    private final EventLoopGroup handleGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 4);

    private Bootstrap clientBoot;
    private ISocketClient sokcet;
    private SocketConfig config = SocketConfig.of("127.0.0.1:8899");

    @Before
    public void init() {
	clientBoot = new Bootstrap();
    }

    @After
    public void alter() {
	if (sokcet != null) {
	    sokcet.stop();
	}
	handleGroup.shutdownGracefully();
    }

    @Test
    public void test_echo() {
	EchoClientHandler handle = new EchoClientHandler();
	clientBoot.group(handleGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<Channel>() {

	    @Override
	    protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		pipeline.addLast(handle);

	    }
	});

	sokcet = NettySocketClient.of(clientBoot, config, null);
	sokcet.start();
	sokcet.sync();

    }
}
