package org.solq.fm.common.socket.channel;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.solq.fm.common.socket.ISocket;
import org.solq.fm.common.socket.channel.model.Channel;
import org.solq.fm.common.socket.channel.model.ChannelType;
import org.solq.fm.common.socket.channel.model.IChannelManager;
import org.solq.fm.common.util.MapUtils;

/***
 * 负责sokcet与socket 管理
 * */
public class ChannelManager implements IChannelManager {

    public final static String CHANNEL_DEFAULT_NAME = "CHANNEL_DEFAULT_NAME";
    public final static Channel CHANNEL_DEFAULT = Channel.of(CHANNEL_DEFAULT_NAME, ChannelType.DEFAULT);
    /** 管道名称映射 Channel */
    private ConcurrentHashMap<String, ChannelWrapper> n2cChannels = new ConcurrentHashMap<>();

    /** 管道类型映射 Channel */
    // private ConcurrentHashMap<ChannelType, Set<Channel>> t2cCahannels = new
    // ConcurrentHashMap<>();

    public ChannelManager() {
	registerChannel(CHANNEL_DEFAULT);
    }

    @Override
    public Set<String> getChannelNames() {
	return new HashSet<>(n2cChannels.keySet());
    }

    @Override
    public int getChannelCount(String name) {
	ChannelWrapper channels = n2cChannels.get(name);
	if (channels == null) {
	    return 0;
	}
	return channels.sockets.size();
    }

    @Override
    public void registerChannel(Channel channel) {
	final String name = channel.getName();
	ChannelWrapper channels = n2cChannels.get(name);
	if (channels == null) {
	    MapUtils.putIfAbsent(n2cChannels, name, ChannelWrapper.of(channel));
	}
    }

    @Override
    public void removeChannel(String name) {
	ChannelWrapper channels = n2cChannels.remove(name);
	if (channels != null) {
	    // 推送变更
	}
    }

    @Override
    public void joinChannel(ISocket socket, String... names) {

	for (String name : names) {
	    ChannelWrapper channels = getChannel(name);
	    if (channels == null) {
		continue;
	    }
	    synchronized (channels.channel) {
		channels.sockets.add(socket);
	    }
	    // 推送变更
	}
    }

    @Override
    public void replaceChannel(String newName, String oldName, ISocket socket) {
	exitChannel(socket, oldName);
	joinChannel(socket, newName);
    }

    @Override
    public void exitChannel(ISocket socket, String... names) {
	for (String name : names) {
	    ChannelWrapper channels = getChannel(name);
	    if (channels == null) {
		continue;
	    }
	    synchronized (channels.channel) {
		channels.sockets.remove(socket);
	    }
	    // 推送变更
	}
    }

    @Override
    public void removeSocket(ISocket socket) {
	exitChannel(socket, n2cChannels.keySet().toArray(new String[0]));
    }

    @Override
    public void send(Object message, String... channleNames) {
	for (String name : channleNames) {
	    ChannelWrapper channels = getChannel(name);
	    if (channels == null) {
		continue;
	    }
	    synchronized (channels.channel) {
		for (ISocket socket : channels.sockets) {
		    socket.send(message);
		}
	    }
	}
    }

    @Override
    public void send(Object message, ISocket... sockets) {
	for (ISocket socket : sockets) {
	    socket.send(message);
	}
    }

    @Override
    public void sendAll(Object message) {
	send(message, n2cChannels.keySet().toArray(new String[0]));
    }

    
    @Override
    public void closeChannelAndSokcet(String... channleNames){
	for (String name : channleNames) {
	    ChannelWrapper channels = getChannel(name);
	    if (channels == null) {
		continue;
	    }
	    removeChannel(name);	    
	    synchronized (channels.channel) {
		for (ISocket socket : channels.sockets) {
		    socket.stop();
		}
	    }
	}
    }

    @Override
    public void closeChannelAndSokcetAll() {
	closeChannelAndSokcet(n2cChannels.keySet().toArray(new String[0]));
    }
    
    //////////////////////private///////////////////////////
    private ChannelWrapper getChannel(String name) {
	ChannelWrapper channels = n2cChannels.get(name);
	return channels;
    }

    private static class ChannelWrapper {
	private Channel channel;
	private Set<ISocket> sockets = new HashSet<>();

	public static ChannelWrapper of(Channel channel) {
	    ChannelWrapper ret = new ChannelWrapper();
	    ret.channel = channel;
	    return ret;
	}

	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((channel == null) ? 0 : channel.hashCode());
	    return result;
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
		return true;
	    if (obj == null)
		return false;
	    if (getClass() != obj.getClass())
		return false;
	    ChannelWrapper other = (ChannelWrapper) obj;
	    if (channel == null) {
		if (other.channel != null)
		    return false;
	    } else if (!channel.equals(other.channel))
		return false;
	    return true;
	}

    }



   

}
