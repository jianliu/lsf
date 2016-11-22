package com.liuj.lsf.core;

import com.liuj.lsf.msg.MsgHeader;
import io.netty.channel.Channel;

/**
 * Created by cdliujian1 on 2016/11/22.
 * server端处理Request的处理器
 */
public interface RequestHandle {


    Object NULL= new Object();

    /**
     * 是否可以处理
     * @param req
     * @return
     */
    boolean canHandle(Object req);

    /**
     * 处理
     * @param channel
     * @param req
     * @return
     */
    Object handleReq(MsgHeader msgHeader, Channel channel, Object req);

}
