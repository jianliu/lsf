package com.liuj.lsf.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import com.liuj.lsf.codec.LSFDecoder;
import com.liuj.lsf.codec.LSFEncoder;

/**
 * Created by cdliujian1 on 2016/6/19.
 */
@ChannelHandler.Sharable
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private ChannelInboundHandlerAdapter serverChannelHandler;

    public ServerChannelInitializer(ChannelInboundHandlerAdapter serverChannelHandler) {
        this.serverChannelHandler = serverChannelHandler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new LSFDecoder(),new LSFEncoder(),this.serverChannelHandler);
    }
}
