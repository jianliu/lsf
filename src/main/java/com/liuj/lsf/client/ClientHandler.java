package com.liuj.lsf.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import com.liuj.lsf.msg.RequestMsg;
import com.liuj.lsf.msg.ResponseMsg;
import com.liuj.lsf.transport.impl.DefaultClientTransport;

/**
 * Created by cdliujian1 on 2016/6/19.
 */
public class ClientHandler  extends ChannelInboundHandlerAdapter {

    private DefaultClientTransport clientTransport;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        if (msg instanceof ResponseMsg) {
            ResponseMsg responseMsg = (ResponseMsg) msg;
            clientTransport.success(responseMsg);

        } else if (msg instanceof RequestMsg) {
            //receive the callback ResponseMessage
            RequestMsg responseMsg = (RequestMsg) msg;

            //find the transport

        }

    }

    public void setClientTransport(DefaultClientTransport clientTransport) {
        this.clientTransport = clientTransport;
    }
}