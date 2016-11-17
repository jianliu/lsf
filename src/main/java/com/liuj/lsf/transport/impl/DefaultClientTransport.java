package com.liuj.lsf.transport.impl;

import com.liuj.lsf.Constants;
import io.netty.channel.Channel;
import com.liuj.lsf.client.ClientTransportFactory;
import com.liuj.lsf.client.MsgFuture;
import com.liuj.lsf.config.ConsumerConfig;
import com.liuj.lsf.exceptions.LsfException;
import com.liuj.lsf.msg.BaseMsg;
import com.liuj.lsf.msg.RequestMsg;
import com.liuj.lsf.msg.ResponseMsg;
import com.liuj.lsf.server.Provider;
import com.liuj.lsf.transport.ClientTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by cdliujian1 on 2016/11/1.
 */
public class DefaultClientTransport implements ClientTransport {

    private Logger logger = LoggerFactory.getLogger(DefaultClientTransport.class);

    private int timeout = 2000;

    private ConsumerConfig consumerConfig;

    private Provider provider;

    private Channel channel;

    private AtomicLong requestMsgId = new AtomicLong(1);

    private final ConcurrentHashMap<Long, MsgFuture> futureMap = new ConcurrentHashMap<Long, MsgFuture>();


    public void reconnect() {

        boolean isOpen = this.channel != null && this.channel.isActive() && this.channel.isOpen();
        if (!isOpen) {
            try {
                Channel channel = ClientTransportFactory.buildChannel(provider);
                this.setChannel(channel);
                ClientTransportFactory.buildTransport(this);
            } catch (LsfException e) {
                logger.error("init reconnect error", e);
            }
        }
    }

    public void shutdown() {
        if (channel != null && channel.isOpen()) {
            try {
                channel.close();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public ResponseMsg sendMsg(BaseMsg baseMsg) {

        baseMsg.getMsgHeader().setMsgId(requestMsgId.getAndIncrement());
        if(baseMsg instanceof RequestMsg) {
            baseMsg.getMsgHeader().setMsgType(Constants.REQUEST_MSG);
        }else if(baseMsg instanceof ResponseMsg){
            baseMsg.getMsgHeader().setMsgType(Constants.RESPONSE_MSG);
        }
        MsgFuture<ResponseMsg> msgFuture = this.sendAsync(baseMsg);
        try {

            return msgFuture.get();

        } catch (InterruptedException e) {
            e.printStackTrace();
            this.futureMap.remove(baseMsg.getMsgHeader().getMsgId());
            throw new LsfException("线程中断",e);
        } catch (LsfException e) {
            this.futureMap.remove(baseMsg.getMsgHeader().getMsgId());
            throw e;
        }
    }

    private MsgFuture<ResponseMsg> sendAsync(BaseMsg baseMsg) {
        MsgFuture<ResponseMsg> msgFuture = new MsgFuture<ResponseMsg>(baseMsg, timeout);
        futureMap.put(baseMsg.getMsgId(), msgFuture);
        if (baseMsg instanceof RequestMsg) {
            channel.writeAndFlush(baseMsg, channel.voidPromise());
        }
        msgFuture.setSendTime(System.currentTimeMillis());
        return msgFuture;
    }

    public void success(ResponseMsg responseMsg) {
        MsgFuture msgFuture = futureMap.get(responseMsg.getMsgId());
        if(msgFuture == null){
            logger.warn("消息id:{} 已经被丢弃", responseMsg.getMsgId());
            return;
        }
        if (!msgFuture.isDone()) {
            msgFuture.setResult(responseMsg);
        }
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setConsumerConfig(ConsumerConfig consumerConfig) {
        this.consumerConfig = consumerConfig;
        if (consumerConfig != null && consumerConfig.getTimeout() > 0) {
            this.timeout = consumerConfig.getTimeout();
        }
    }
}
