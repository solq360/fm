package org.solq.fm.common.socket.config;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class SocketConfig {
    private String addr;
    private Integer port;

    public static SocketConfig of(String host) {
	SocketConfig ret = new SocketConfig();
	ret.addr = host.split(":")[0];
	ret.port = Integer.valueOf(host.split(":")[1]);
	return ret;
    }

    public static SocketConfig of(String host, int port) {
	SocketConfig ret = new SocketConfig();
	ret.addr = host;
	ret.port = port;
	return ret;
    }

    public String getAddr() {
	return addr;
    }

    public Integer getPort() {
	return port;
    }

    public SocketAddress toInetSocketAddress() {
	return new InetSocketAddress(addr, port);
    }

}
