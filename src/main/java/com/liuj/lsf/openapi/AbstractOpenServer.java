package com.liuj.lsf.openapi;

import com.liuj.lsf.server.AbstractServerHandler;
import com.liuj.lsf.server.Server;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by cdliujian1 on 2016/11/17.
 */
public abstract class AbstractOpenServer implements IServer{

    private Server server = new Server(null);

    public void setServerChannelHandler(AbstractServerHandler serverChannel) {
        server.setServerHandler(serverChannel);
    }

    public void setWorkerGroup(NioEventLoopGroup workerGroup) {
      server.setWorkerGroup(workerGroup);
    }

    public void startServer() throws Exception {
        server.run();
    }
}
