package com.liuj.lsf.msg;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

/**
 * Created by cdliujian1 on 2016/6/15.
 */
public abstract class BaseMsg {

     /**
     * 请求头
     */
    private MsgHeader msgHeader;

    /**
     * 消息体
     */
    private ByteBuf msgBody;

    private transient Channel channel;

    public long getMsgId() {
        return msgHeader.getMsgId();
    }

    public void setMsgId(long msgId) {
        this.getMsgHeader().setMsgId(msgId);
    }

    public ByteBuf getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(ByteBuf msgBody) {
        this.msgBody = msgBody;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public MsgHeader getMsgHeader() {
        return msgHeader;
    }

    public void setMsgHeader(MsgHeader msgHeader) {
        this.msgHeader = msgHeader;
    }
}
