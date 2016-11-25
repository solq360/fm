package org.solq.fm.common.socket.channel.model;

import java.util.Set;

import org.solq.fm.common.socket.ISocket;

public interface IChannelManager {

    public Set<String> getChannelNames();

    public int getChannelCount(String name);

    public void registerChannel(Channel channel);

    public void removeChannel(String name);

    public void joinChannel(ISocket socket,String... names);
    public void replaceChannel(String newName,String oldName, ISocket socket);
    public void exitChannel( ISocket socket,String... names);
    public void removeSocket(ISocket socket);

    public void send(Object message, String... channleNames);
    public void send(Object message, ISocket... sockets);

    public void sendAll(Object message);

    public void closeChannelAndSokcet(String... channleNames);
    public void closeChannelAndSokcetAll();
}
