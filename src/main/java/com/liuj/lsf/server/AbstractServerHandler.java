package com.liuj.lsf.server;

import com.liuj.lsf.Constants;
import com.liuj.lsf.config.ServerConfig;
import com.liuj.lsf.msg.MsgHeader;
import com.liuj.lsf.msg.ResponseMsg;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInboundHandlerAdapter;
import com.liuj.lsf.config.ConsumerConfig;
import com.liuj.lsf.exceptions.ExceptionHolder;
import com.liuj.lsf.exceptions.LsfException;
import com.liuj.lsf.utils.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by cdliujian1 on 2016/10/31.
 */
public abstract class AbstractServerHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Class<?> getParameterTypes(Object pa){
        return ReflectionUtils.forName(pa.getClass().getName());
    }

    protected void sendResponse(Channel channel, Object messageResult) {
        MsgHeader msgHeader = new MsgHeader(Constants.RESPONSE_MSG);
        if(messageResult != null) {
            msgHeader.setClz(messageResult.getClass().getName());
        }else {
            msgHeader.setClz(Constants.NULL_RESULT_CLASS);
        }

        ResponseMsg responseMsg = new ResponseMsg();
        responseMsg.setReceiveTime(System.currentTimeMillis());
        responseMsg.setResponse(messageResult);
        responseMsg.setMsgHeader(msgHeader);
        channel.writeAndFlush(responseMsg, channel.voidPromise());
    }

    /**
     * 发送服务端的响应
     * @param messageId
     * @param channel
     * @param messageResult
     */
    protected void sendResponse(long messageId, Channel channel, Object messageResult) {
        MsgHeader msgHeader = new MsgHeader(Constants.RESPONSE_MSG);
        if(messageResult != null) {
            msgHeader.setClz(messageResult.getClass().getName());
        }else {
            msgHeader.setClz(Constants.NULL_RESULT_CLASS);
        }

        ResponseMsg responseMsg = new ResponseMsg();
        responseMsg.setReceiveTime(System.currentTimeMillis());
        responseMsg.setResponse(messageResult);
        responseMsg.setMsgHeader(msgHeader);
        responseMsg.getMsgHeader().setMsgId(messageId);

        //如果使用voidPromise，则无法和IdleStateHandler同时使用，因为它会触发voidPromise的addListener(...)操作，从而导致write失败
        channel.writeAndFlush(responseMsg, channel.newPromise());
    }

}
