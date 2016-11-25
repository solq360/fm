package org.solq.fm.common.socket.session;

import java.util.function.Consumer;

import org.solq.fm.common.socket.ISocket;

public interface ISession {

    //public ISession copySession();

    ////////// 会话属性操作//////////////
    public void putAtt(Object key, Object value);

    public <T> T getAtt(Object key);

    public void removeAtt(Object key);

    public <T> T getIdentity();

    public void close();

    /***
     * 绑定socket
     */
    public void bindSocket(Object identity, ISocket socket);

    public void send(Object message);

    public void send(ISession session, Object message);

    public void sendAll(Object message);

    public void send(Object message, Consumer<ISession> action);

}
