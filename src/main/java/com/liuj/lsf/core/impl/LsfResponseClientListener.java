package com.liuj.lsf.core.impl;

import com.liuj.lsf.core.AbstractLogger;
import com.liuj.lsf.core.ResponseListener;
import com.liuj.lsf.msg.ResponseMsg;
import com.liuj.lsf.transport.ClientTransport;

/**
 * Created by cdliujian1 on 2016/11/22.
 * client端 监听response响应
 */
public class LsfResponseClientListener extends AbstractLogger implements ResponseListener {

    /**
     * response返回后的处理
     * @param clientTransport response所在的clientTransport
     * @param responseMsg 响应消息
     */
    public void onResponse(ClientTransport clientTransport, ResponseMsg responseMsg) {
        logger.debug("接收到response");
        clientTransport.success(responseMsg);
    }
}
