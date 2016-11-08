package com.liuj.lsf.server;

import com.liuj.lsf.config.ServerConfig;
import com.liuj.lsf.demo.mock.IService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import com.liuj.lsf.GlobalManager;
import com.liuj.lsf.demo.mock.IServerImpl;
import com.liuj.lsf.route.ServerRoute;
import com.liuj.lsf.route.impl.ZooKServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cdliujian1 on 2016/6/19.
 */
public class Server {

    private final static Logger logger = LoggerFactory.getLogger(Server.class);

    private ServerRoute serverRoute;

    public Server(ServerRoute serverRoute) {
        this.serverRoute = serverRoute;
    }

    public void registerServer(ServerConfig serverBean){
        ServerContainer.addServerBean(serverBean);
        serverRoute.register(serverBean.getInterfaceClz(),serverBean.getAlias());
    }

    public void run() throws Exception {
        final int port = GlobalManager.serverPort;
        final NioEventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        final NioEventLoopGroup workerGroup = new NioEventLoopGroup(200);
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ServerChannelInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync(); // (7)
            f.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(future.isSuccess()){
                        //logger success
                        logger.info("start server success, port:{}",port);
                    }else{
                        bossGroup.shutdownGracefully();
                        workerGroup.shutdownGracefully();
                        logger.error("start server failed");
                    }
                }
            });
            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
