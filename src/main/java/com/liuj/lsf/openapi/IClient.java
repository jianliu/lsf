package com.liuj.lsf.openapi;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;

/**
 * Created by cdliujian1 on 2016/11/17.
 */
public interface IClient {

    /**
     * client连接是否可用
     * @return
     */
    boolean isActive();

    /**
     * 重连接
     */
    void reconnect();

    /**
     * 设置响应超时时间
     * @param mills
     */
    void setTimeout(int mills);

    /**
     * 获取当前channel
     * @return
     */
    Channel getChannel();

    /**
     * 发送请求消息,会等待消息返回
     * @param whatever
     */
    void sendMsg(Object whatever);

    /**
     * 发送消息，有返回
     * @param whatever
     * @return
     */
    Object sendMsgRet(Object whatever);

    /**
     * 发送响应
     * @param whatever
     */
    void sendResponse(Object whatever);

}
