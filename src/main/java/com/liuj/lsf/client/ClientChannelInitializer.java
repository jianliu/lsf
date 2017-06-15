package com.liuj.lsf.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import com.liuj.lsf.Constants;
import com.liuj.lsf.codec.LSFDecoder;
import com.liuj.lsf.codec.LSFEncoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * Created by cdliujian1 on 2016/11/1.
 */
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {


    private ClientHandler clientHandler;

    public ClientChannelInitializer(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        ch.pipeline().addLast(new IdleStateHandler(5, 5, 0));
        pipeline.addLast(new LSFEncoder());
        pipeline.addLast(new LSFDecoder());
//        pipeline.addLast("readtimeout", new ReadTimeoutHandler(5));

        pipeline.addLast(Constants.CLIENT_HANDLER, clientHandler);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

}
