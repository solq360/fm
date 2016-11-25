package org.solq.fm.common.socket;

import org.solq.fm.common.socket.config.SocketConfig;
import org.solq.fm.common.socket.session.ISession;

public abstract class AbsSocket implements ISocket {
    protected ISession session;
    protected SocketConfig socketConfig;

    private boolean closed = true;

    abstract protected void doStart();

    abstract protected void doStop();

    @Override
    public synchronized void start() {
 	if (!isClose()) {
	    return;
	}
	closed = false;
	doStart();
    }

    @Override
    public synchronized void stop() {
	if (isClose()) {
	    return;
	}
	doStop();
	closed = true;
    }

    @Override
    public boolean isClose() {
	return closed;
    }

    @Override
    public void setSession(ISession session) {
	this.session = session;
    }

    @Override
    public ISession getSession() {
	return session;
    }

    @Override
    public SocketConfig getSocketConfig() {
	return socketConfig;
    }

    @Override
    public void setSocketConfig(SocketConfig socketConfig) {
	this.socketConfig = socketConfig;
    }

}
