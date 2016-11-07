package com.liuj.lsf.client;

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

/**
 * Created by cdliujian1 on 2016/11/1.
 */
public class ClientTransportFactory {

    private static final Logger logger = LoggerFactory.getLogger(ClientTransportFactory.class);

    public static Channel buildChannel(final Provider provider) {
        Channel channel = null;
        Bootstrap b = new Bootstrap(); // (1)
        b.group(new NioEventLoopGroup()); // (2)
        b.channel(NioSocketChannel.class); // (3)
        b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
        b.handler(new ClientChannelInitializer());

        // Start the client.
        try {
            ChannelFuture f = b.connect(provider.getHost(), provider.getPort()).sync();
            if (f.isSuccess()) {
                channel = f.channel();
                logger.info("connect to server success,host--{},port--{}", provider.getHost(), provider.getPort());
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

    public static DefaultClientTransport buildTransport(Provider provider, Channel channel) {
        DefaultClientTransport clientTransport = new DefaultClientTransport();
        clientTransport.setChannel(channel);
        clientTransport.setProvider(provider);
        ClientHandler clientHandler = (ClientHandler) channel.pipeline().get(Constants.Client_Handler);
        clientHandler.setClientTransport(clientTransport);
        return clientTransport;
    }

    public static ClientTransport buildTransport(DefaultClientTransport clientTransport) {
        ClientHandler clientHandler = (ClientHandler) clientTransport.getChannel().pipeline().get(Constants.Client_Handler);
        clientHandler.setClientTransport(clientTransport);
        return clientTransport;
    }

}
