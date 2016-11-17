package com.liuj.lsf.msg;

import com.liuj.lsf.config.ConsumerConfig;

/**
 * Created by cdliujian1 on 2016/6/15.
 */
public class RequestMsg extends BaseMsg{

    private long sendTime;

    /**
     * this object is msgBody
     */
    private Object consumerBean;

    private String targetAddress;

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public Object getConsumerBean() {
        return consumerBean;
    }

    public void setConsumerBean(Object consumerBean) {
        this.consumerBean = consumerBean;
    }

    public String getTargetAddress() {
        return targetAddress;
    }

    public void setTargetAddress(String targetAddress) {
        this.targetAddress = targetAddress;
    }
}
