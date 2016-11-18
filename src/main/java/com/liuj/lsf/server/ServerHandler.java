package com.liuj.lsf.server;

import com.liuj.lsf.config.ServerConfig;
import com.liuj.lsf.exceptions.ExceptionHolder;
import com.liuj.lsf.exceptions.LsfException;
import com.liuj.lsf.utils.ReflectionUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import com.liuj.lsf.Constants;
import com.liuj.lsf.config.ConsumerConfig;
import com.liuj.lsf.msg.MsgHeader;
import com.liuj.lsf.msg.RequestMsg;
import com.liuj.lsf.msg.ResponseMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by cdliujian1 on 2016/6/15.
 */
@ChannelHandler.Sharable
public class ServerHandler extends AbstractServerHandler {

    private final static Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        Channel channel = ctx.channel();
        if (msg instanceof RequestMsg) {
            RequestMsg requestMsg = (RequestMsg) msg;
            Object consumerBean = ((RequestMsg) msg).getConsumerBean();
            logger.info("find RequestMsg ");
            ResponseMsg responseMsg = new ResponseMsg();
            responseMsg.setReceiveTime(System.currentTimeMillis());

            Object result = invoke((ConsumerConfig)consumerBean);

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


    protected ServerContainer serverContainer = new ServerContainer();

    protected Object invoke(ConsumerConfig consumerBean){
        try {
            Class targetClz = Class.forName(consumerBean.getInterfaceId());
            //最终执行方法的实例
            Object instance = getServerInstanceByConsumer(consumerBean,targetClz);
            String methodName = consumerBean.getRequestMethod().getMethod();
            Object[] params = consumerBean.getRequestMethod().getMethodParams();
            Class[] parameterTypes = new Class[params.length];
            for(int i = 0; i< parameterTypes.length; i++){
                String type = consumerBean.getRequestMethod().getParameterTypes()[i];
                parameterTypes[i] = ReflectionUtils.forName(type);
            }
            Method method =targetClz.getMethod(methodName,parameterTypes);
            return method.invoke(instance,params);
        } catch (Throwable throwable) {
            logger.error("执行方法异常 接口:{},method:{}",consumerBean.getInterfaceId(),
                    consumerBean.getRequestMethod().getMethod(), throwable);
            return new ExceptionHolder(new LsfException("server端执行失败", throwable));
        }
    }

    protected <T> T getServerInstanceByConsumer(ConsumerConfig consumerBean, Class<T> clz){
        ServerConfig serverBean = new ServerConfig();
        serverBean.setInterfaceId(consumerBean.getInterfaceId());
        serverBean.setAlias(consumerBean.getAlias());
        return serverContainer.getServer(serverBean,clz);
    }

}