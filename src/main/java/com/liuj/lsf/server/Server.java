package com.liuj.lsf.server;

import com.liuj.lsf.config.ServerConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import com.liuj.lsf.GlobalManager;
import com.liuj.lsf.route.ServerRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cdliujian1 on 2016/6/19.
 */
public class Server {

    private final static Logger logger = LoggerFactory.getLogger(Server.class);

    private int port;

    private ServerRoute serverRoute;

    private ChannelInboundHandlerAdapter serverHandler;

    private int loopThreadsN = 200;

    private NioEventLoopGroup workerGroup = new NioEventLoopGroup(loopThreadsN);

    public Server(ServerRoute serverRoute, ChannelInboundHandlerAdapter serverHandler, int port) {
        this.serverRoute = serverRoute;
        this.serverHandler = serverHandler;
        this.port = port;
    }

    public void registerServer(ServerConfig serverBean){
        ServerContainer.addServerBean(serverBean);
        serverRoute.register(serverBean.getInterfaceId(),serverBean.getAlias());
    }



    public void setWorkerGroup(NioEventLoopGroup workerGroup) {
        this.workerGroup = workerGroup;
    }

    public void run() throws Exception {
        //仅仅启动一个bossGroup即足够
        final NioEventLoopGroup bossGroup = new NioEventLoopGroup(1); // (1)
        final NioEventLoopGroup workerGroup = this.workerGroup;
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ServerChannelInitializer(this.serverHandler))
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
