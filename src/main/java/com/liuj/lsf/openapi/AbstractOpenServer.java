package com.liuj.lsf.openapi;

import com.liuj.lsf.server.AbstractServerHandler;
import com.liuj.lsf.server.Server;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by cdliujian1 on 2016/11/17.
 */
public abstract class AbstractOpenServer implements IServer{

    private Server server;

    public void init(ChannelInboundHandlerAdapter serverHandler, int port) {
        server = new Server(null,serverHandler, port);
    }

    public void setWorkerGroup(NioEventLoopGroup workerGroup) {
      server.setWorkerGroup(workerGroup);
    }

    public void startServer() throws Exception {
        server.run();
    }
}
