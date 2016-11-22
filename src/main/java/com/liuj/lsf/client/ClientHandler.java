package com.liuj.lsf.client;

import com.liuj.lsf.core.ResponseListener;
import com.liuj.lsf.core.impl.LsfResponseClientListener;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import com.liuj.lsf.msg.RequestMsg;
import com.liuj.lsf.msg.ResponseMsg;
import com.liuj.lsf.transport.impl.DefaultClientTransport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cdliujian1 on 2016/6/19.
 */
public class ClientHandler  extends ChannelInboundHandlerAdapter {

    private DefaultClientTransport clientTransport;

    private List<ResponseListener> responseListenerList = new ArrayList<ResponseListener>();

    public ClientHandler() {
        responseListenerList.add(new LsfResponseClientListener());
    }

    public ClientHandler(List<ResponseListener> responseListenerList) {
        this.responseListenerList = responseListenerList;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        if (msg instanceof ResponseMsg) {
            ResponseMsg responseMsg = (ResponseMsg) msg;
            //responseMsg不引用byteBuf，方便gc
            responseMsg.setMsgBody(null);

            for(ResponseListener responseListener: responseListenerList){
                responseListener.onResponse(clientTransport, responseMsg);
            }
        } else if (msg instanceof RequestMsg) {
            //receive the callback ResponseMessage
            RequestMsg responseMsg = (RequestMsg) msg;

            //find the transport

        }

    }

    public void setClientTransport(DefaultClientTransport clientTransport) {
        this.clientTransport = clientTransport;
    }

    public void setResponseListenerList(List<ResponseListener> responseListenerList) {
        this.responseListenerList = responseListenerList;
    }

    public synchronized void addListener(ResponseListener responseListener){
        this.responseListenerList.add(responseListener);
    }

}