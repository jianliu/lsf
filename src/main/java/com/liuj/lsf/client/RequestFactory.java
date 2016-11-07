package com.liuj.lsf.client;

import com.liuj.lsf.Constants;
import com.liuj.lsf.consumer.ConsumerConfig;
import com.liuj.lsf.msg.MsgHeader;
import com.liuj.lsf.msg.RequestMsg;

/**
 * Created by cdliujian1 on 2016/11/4.
 */
public class RequestFactory {

    public static RequestMsg buildRequest(){
        RequestMsg requestMsg = new RequestMsg();
        MsgHeader msgHeader = new MsgHeader();
        msgHeader.setClz(ConsumerConfig.class.getCanonicalName());
        requestMsg.setMsgHeader(msgHeader);
        msgHeader.setMsgType(Constants.REQUEST_MSG);
        return requestMsg;
    }

}
