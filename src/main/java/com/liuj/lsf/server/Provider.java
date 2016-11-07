package com.liuj.lsf.server;

import io.netty.channel.Channel;

/**
 * Created by cdliujian1 on 2016/11/1.
 */
public class Provider {

    private String host;
    private int port;

    private transient Channel channel;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
