package org.solq.fm.common.socket.channel.model;

public class Channel {
    private String name;

    private ChannelType type;

    public static Channel of(String name,ChannelType type) {
	Channel ret = new Channel();
	ret.name = name ;
	ret.type = type;
 	return ret;
    }

    public String getName() {
	return name;
    }

    public ChannelType getType() {
	return type;
    }

}
