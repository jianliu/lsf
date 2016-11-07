package com.liuj.lsf.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import com.liuj.lsf.Constants;
import com.liuj.lsf.consumer.ConsumerConfig;
import com.liuj.lsf.msg.MsgHeader;
import com.liuj.lsf.msg.RequestMsg;
import com.liuj.lsf.msg.ResponseMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cdliujian1 on 2016/6/15.
 */
public class ServerHandler extends AbstractServerHandler {

    private final static Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        Channel channel = ctx.channel();
        if (msg instanceof RequestMsg) {
            RequestMsg requestMsg = (RequestMsg) msg;
            ConsumerConfig consumerBean = ((RequestMsg) msg).getConsumerBean();
            logger.info("find RequestMsg ");
            ResponseMsg responseMsg = new ResponseMsg();
            responseMsg.setReceiveTime(System.currentTimeMillis());

            Object result = invoke(consumerBean);

            MsgHeader msgHeader = new MsgHeader();
            msgHeader.setMsgType(Constants.RESPONSE_MSG);

            if(result != null) {
                msgHeader.setClz(result.getClass().getName());
            }else {
                msgHeader.setClz(Constants.NULL_RESULT_CLASS);
            }

            responseMsg.setMsgHeader(msgHeader);
            responseMsg.getMsgHeader().setMsgId(requestMsg.getMsgId());
            responseMsg.setResponse(result);

            channel.writeAndFlush(responseMsg, channel.voidPromise());
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
}