package com.liuj.lsf.openapi;

import com.liuj.lsf.server.AbstractServerHandler;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * Created by cdliujian1 on 2016/11/17.
 */
public interface IServer {

    /**
     * 基于netty的服务端channel处理器
     * @param serverChannel
     */
    void setServerChannelHandler(AbstractServerHandler serverChannel);

    /**
     * 基于netty的服务端非阻塞worker多路复用
     * @param workerGroup
     */
    void setWorkerGroup(NioEventLoopGroup workerGroup);

    /**
     * 启动server
     */
    void startServer() throws Exception;

}
