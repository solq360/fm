package org.solq.fm.common.socket;

import org.solq.fm.common.socket.config.SocketConfig;
import org.solq.fm.common.socket.session.ISession;

public interface ISocket {
    // 运行操作

    public void start();

    public void stop();
    
    public void sync();

    public boolean isClose();

    public void send(Object message);

    // 会话绑定
    public void setSession(ISession session);

    public ISession getSession();

    // 配置绑定
    public SocketConfig getSocketConfig();

    public void setSocketConfig(SocketConfig socketConfig);

}
