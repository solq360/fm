package org.solq.fm.common.socket.client.model;

import org.solq.fm.common.socket.ISocket;

/**
 * <pre>
 * socketClien 职责
 * 1.实现socket底层处理
 * 2.维护socket生命周期
 * </pre>
 * 
 * @author solq
 */
public interface ISocketClient extends ISocket {

    public void send(Object message);
    public boolean isConnect();
}
