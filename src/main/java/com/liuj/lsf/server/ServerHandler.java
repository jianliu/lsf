package com.liuj.lsf.server;

import com.liuj.lsf.core.RequestHandle;
import com.liuj.lsf.msg.RequestMsg;
import com.liuj.lsf.msg.ResponseMsg;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cdliujian1 on 2016/6/15.
 */
@ChannelHandler.Sharable
public class ServerHandler extends AbstractServerHandler {

    private final static Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    private List<RequestHandle> requestHandleList = new ArrayList<RequestHandle>();


    public ServerHandler(List<RequestHandle> requestHandleList) {
        this.requestHandleList = requestHandleList;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        Channel channel = ctx.channel();
        if (msg instanceof RequestMsg) {
            RequestMsg requestMsg = (RequestMsg) msg;
            Object consumerBean = ((RequestMsg) msg).getConsumerBean();
            logger.info("find RequestMsg ");
            ResponseMsg responseMsg = new ResponseMsg();
            responseMsg.setReceiveTime(System.currentTimeMillis());

            Object result = null;
            boolean requestHandled = false;
            for (RequestHandle requestHandle : requestHandleList) {
                if (requestHandle.canHandle(consumerBean)) {
                    requestHandled = true;
                    result = requestHandle.handleReq(requestMsg.getMsgHeader(), channel, consumerBean);
                    break;
                }
            }

            if (!requestHandled) {
                logger.warn("消息类型:{} 未发现可以处理的requestHandle.", consumerBean == null ? "null" :
                        consumerBean.getClass().getCanonicalName());
            }
            sendResponse(requestMsg.getMsgId(), channel, result);

        } else if (msg instanceof ResponseMsg) {
            //receive the callback ResponseMessage
            ResponseMsg responseMsg = (ResponseMsg) msg;

            //find the transport

        }

    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) { // (1)
        try {
            super.channelActive(ctx);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        ChannelFuture f = ctx.writeAndFlush(new LTime());
//        f.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public void setRequestHandleList(RequestHandle... requestHandleList) {
        this.requestHandleList = Arrays.asList(requestHandleList);
    }
}