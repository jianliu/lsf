package com.liuj.lsf.transport;

import io.netty.channel.Channel;
import com.liuj.lsf.server.Provider;

/**
 * Created by cdliujian1 on 2016/11/1.
 */
public class Connection {

    private Provider provider;

    private ClientTransport clientTransport;

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public ClientTransport getClientTransport() {
        return clientTransport;
    }

    public void setClientTransport(ClientTransport clientTransport) {
        this.clientTransport = clientTransport;
    }
}
