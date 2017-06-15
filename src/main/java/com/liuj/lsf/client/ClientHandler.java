package com.liuj.lsf.client;

import com.liuj.lsf.core.ResponseListener;
import com.liuj.lsf.core.impl.LsfResponseClientListener;
import com.liuj.lsf.msg.ResponseMsg;
import com.liuj.lsf.transport.impl.DefaultClientTransport;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cdliujian1 on 2016/6/19.
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

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

            for (ResponseListener responseListener : responseListenerList) {
                responseListener.onResponse(clientTransport, responseMsg);
            }
        }

    }

//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        if (evt instanceof IdleStateEvent) {
//            IdleStateEvent e = (IdleStateEvent) evt;
//            if (e.state() == IdleState.READER_IDLE) {
//                ctx.close();
//            } else if (e.state() == IdleState.WRITER_IDLE) {
////                                 ctx.writeAndFlush(new PingMessage());
//            }
//        }
//    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(IdleStateEvent.class.isAssignableFrom(evt.getClass())){
            IdleStateEvent event = (IdleStateEvent) evt;
            if(event.state() == IdleState.READER_IDLE)
                System.out.println("read idle2");
            else if(event.state() == IdleState.WRITER_IDLE)
                System.out.println("write idle2");
            else if(event.state() == IdleState.ALL_IDLE)
                System.out.println("all idle2");
        }


    }

    public void setClientTransport(DefaultClientTransport clientTransport) {
        this.clientTransport = clientTransport;
    }

    public void setResponseListenerList(List<ResponseListener> responseListenerList) {
        this.responseListenerList = responseListenerList;
    }

    public synchronized void addListener(ResponseListener responseListener) {
        this.responseListenerList.add(responseListener);
    }

}