package org.solq.fm.common.socket.server.model;

import org.solq.fm.common.socket.ISocket;
import org.solq.fm.common.socket.client.model.ISocketClient;
import org.solq.fm.common.socket.config.SocketConfig;

/**
 * <pre>
 * socketServer 职责
 * 1.开放端口给 client 访问
 * 2.管理多个 client
 * 3.推送多個消息
 * </pre>
 * 
 * @author solq
 */
public interface ISocketServer extends ISocket {
    /** 连接socket */
    public ISocketClient openSocket(SocketConfig clientSocketConfig);
    public void closeSocket(ISocket client);

    /** 发送所有消息给socket */
    public void sendAll(Object message);    
    /** 发送消息给socket */
    public void send(ISocket socket, Object message);

}
