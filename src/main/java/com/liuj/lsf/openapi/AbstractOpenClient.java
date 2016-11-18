package com.liuj.lsf.openapi;

import com.liuj.lsf.Constants;
import com.liuj.lsf.client.ClientHandler;
import com.liuj.lsf.client.ClientTransportFactory;
import com.liuj.lsf.msg.MsgHeader;
import com.liuj.lsf.msg.RequestMsg;
import com.liuj.lsf.msg.ResponseMsg;
import com.liuj.lsf.transport.impl.DefaultClientTransport;
import com.liuj.lsf.utils.ReflectionUtils;
import io.netty.channel.Channel;

/**
 * Created by cdliujian1 on 2016/11/17.
 */
public abstract class AbstractOpenClient implements IClient {

    private final String host;
    private final int port;
    private final ClientHandler clientHandler;
    private final DefaultClientTransport clientTransport;

    public AbstractOpenClient(String host, int port, ClientHandler clientHandler) {
        this.host = host;
        this.port = port;
        this.clientHandler = clientHandler;
        Channel channel = ClientTransportFactory.buildChannel(this.host, this.port);
        channel.pipeline().remove(Constants.Client_Handler);
        channel.pipeline().addLast(Constants.Client_Handler, this.clientHandler);
        this.clientTransport = ClientTransportFactory.buildTransport(this.host, this.port, channel);
    }

    public boolean isActive() {
        return this.getChannel() !=null  && this.getChannel().isOpen() && this.getChannel().isActive();
    }

    public void reconnect() {
        try{
            this.getChannel().close();
        }catch (Exception e){

        }
        this.clientTransport.reconnect();
    }

    /**
     * 设置响应超时时间
     *
     * @param mills
     */
    public void setTimeout(int mills) {
        this.clientTransport.setTimeout(mills);
    }

    /**
     * 获取当前channel
     *
     * @return
     */
    public Channel getChannel() {
        return this.clientTransport.getChannel();
    }

    /**
     * 发送消息
     *
     * @param whatever
     */
    public void sendMsg(Object whatever) {
        sendMsgRet(whatever);
    }

    /**
     * 发送消息，有返回
     *
     * @param whatever
     * @return
     */
    public Object sendMsgRet(Object whatever) {
        if (whatever == null) {
            throw new IllegalArgumentException("send message must not be null!");
        }

        RequestMsg requestMsg = new RequestMsg();
        requestMsg.setSendTime(System.currentTimeMillis());
        requestMsg.setTargetAddress(this.host);
        requestMsg.setConsumerBean(whatever);

        MsgHeader msgHeader = new MsgHeader();
        msgHeader.setClz(ReflectionUtils.getName(whatever.getClass()));
        requestMsg.setMsgHeader(msgHeader);
        if (this.clientTransport.getChannel().isOpen() && this.clientTransport.getChannel().isActive()) {
            this.clientTransport.reconnect();
        }
        ResponseMsg responseMsg = this.clientTransport.sendMsg(requestMsg);
        return responseMsg.getResponse();
    }

    public void sendResponse(Object whatever) {
        if (whatever == null) {
            throw new IllegalArgumentException("send message must not be null!");
        }

        ResponseMsg response = new ResponseMsg();
        response.setResponse(whatever);

        MsgHeader msgHeader = new MsgHeader();
        msgHeader.setClz(ReflectionUtils.getName(whatever.getClass()));

        if (!this.clientTransport.getChannel().isOpen() || !this.clientTransport.getChannel().isActive()) {
            this.clientTransport.reconnect();
        }

        this.clientTransport.sendMsg(response);
    }
}
