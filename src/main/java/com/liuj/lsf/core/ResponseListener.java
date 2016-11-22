package com.liuj.lsf.core;

import com.liuj.lsf.msg.ResponseMsg;
import com.liuj.lsf.transport.ClientTransport;

/**
 * Created by cdliujian1 on 2016/11/22.
 * client端 监听response响应
 */
public interface ResponseListener {

    /**
     * response返回后的处理
     * @param clientTransport response所在的clientTransport
     * @param responseMsg 响应消息
     */
    void onResponse(ClientTransport clientTransport, ResponseMsg responseMsg);

}
