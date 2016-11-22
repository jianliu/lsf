package com.liuj.lsf.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import com.liuj.lsf.Constants;
import com.liuj.lsf.codec.LSFDecoder;
import com.liuj.lsf.codec.LSFEncoder;

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
        pipeline.addLast(new LSFEncoder());
        pipeline.addLast(new LSFDecoder());
        pipeline.addLast(Constants.CLIENT_HANDLER, clientHandler);
    }


}
