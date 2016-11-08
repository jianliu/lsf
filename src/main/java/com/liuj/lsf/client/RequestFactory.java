package com.liuj.lsf.client;

import com.liuj.lsf.Constants;
import com.liuj.lsf.config.ConsumerConfig;
import com.liuj.lsf.config.RequestMethod;
import com.liuj.lsf.msg.MsgHeader;
import com.liuj.lsf.msg.RequestMsg;

/**
 * Created by cdliujian1 on 2016/11/4.
 */
public class RequestFactory {

    public static RequestMsg buildRequest(ConsumerConfig consumerConfig, RequestMethod requestMethod){
        RequestMsg requestMsg = new RequestMsg();
        MsgHeader msgHeader = new MsgHeader();
        msgHeader.setClz(ConsumerConfig.class.getCanonicalName());
        msgHeader.setMsgType(Constants.REQUEST_MSG);

        requestMsg.setMsgHeader(msgHeader);
        requestMsg.setConsumerBean(consumerConfig);
        requestMsg.getConsumerBean().setRequestMethod(requestMethod);
        return requestMsg;
    }

}
