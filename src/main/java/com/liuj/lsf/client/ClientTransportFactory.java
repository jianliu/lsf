package com.liuj.lsf.client;

import com.liuj.lsf.core.ResponseListener;
import com.liuj.lsf.core.impl.LsfResponseClientListener;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import com.liuj.lsf.Constants;
import com.liuj.lsf.exceptions.LsfException;
import com.liuj.lsf.server.Provider;
import com.liuj.lsf.transport.ClientTransport;
import com.liuj.lsf.transport.impl.DefaultClientTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cdliujian1 on 2016/11/1.
 */
public class ClientTransportFactory {

    private static final Logger logger = LoggerFactory.getLogger(ClientTransportFactory.class);

    public static Channel buildChannel(String host,int port){
        Channel channel = null;
        Bootstrap b = new Bootstrap(); // (1)
        b.group(new NioEventLoopGroup()); // (2)
        b.channel(NioSocketChannel.class); // (3)
        b.option(ChannelOption.SO_KEEPALIVE, true); // (4)

        List<ResponseListener> responseListeners = new ArrayList<ResponseListener>();
        responseListeners.add(new LsfResponseClientListener());
        ClientHandler clientHandler = new ClientHandler(responseListeners);

        b.handler(new ClientChannelInitializer(clientHandler));

        // Start the client.
        try {
            ChannelFuture f = b.connect(host, port).sync();
            if (f.isSuccess()) {
                channel = f.channel();
                logger.info("connect to server success,host--{},port--{}", host, port);
            } else {
                throw new LsfException("建立连接失败");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (LsfException e) {
            throw e;
        }
        return channel;
    }

    public static Channel buildChannel(final Provider provider) {
       return buildChannel(provider.getHost(),provider.getPort());
    }


    public static DefaultClientTransport buildTransport(String host, int port, Channel channel) {
        Provider provider =new Provider();
        provider.setHost(host);
        provider.setPort(port);
        return buildTransport(provider,channel);
    }

    public static DefaultClientTransport buildTransport(Provider provider, Channel channel) {
        if(provider == null){
            throw new IllegalArgumentException("provider must not be null!");
        }
        DefaultClientTransport clientTransport = new DefaultClientTransport();
        clientTransport.setChannel(channel);
        clientTransport.setProvider(provider);
        ClientHandler clientHandler = (ClientHandler) channel.pipeline().get(Constants.CLIENT_HANDLER);
        clientHandler.setClientTransport(clientTransport);
        return clientTransport;
    }

    public static ClientTransport buildTransport(DefaultClientTransport clientTransport) {
        ClientHandler clientHandler = (ClientHandler) clientTransport.getChannel().pipeline().get(Constants.CLIENT_HANDLER);
        clientHandler.setClientTransport(clientTransport);
        return clientTransport;
    }

}
