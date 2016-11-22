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

        channel.writeAndFlush(responseMsg, channel.voidPromise());
    }

}
