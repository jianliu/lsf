package com.liuj.lsf.transport;

import com.liuj.lsf.msg.BaseMsg;
import com.liuj.lsf.msg.ResponseMsg;

/**
 * Created by cdliujian1 on 2016/11/1.
 */
public interface ClientTransport {

    void reconnect();

    void shutdown();

    ResponseMsg sendMsg(BaseMsg baseMsg);

    void success(ResponseMsg responseMsg);

}
