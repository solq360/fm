package org.solq.fm.common.socket;

public interface ISend {
    public void send(Object message);
    public void send(Object message, String... channleNames);
    public void send(Object message, ISocket... sockets);

    public void sendAll(Object message);
}
