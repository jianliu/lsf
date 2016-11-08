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
    private ConsumerConfig consumerBean;

    private String targetAddress;

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public ConsumerConfig getConsumerBean() {
        return consumerBean;
    }

    public void setConsumerBean(ConsumerConfig consumerBean) {
        this.consumerBean = consumerBean;
    }

    public String getTargetAddress() {
        return targetAddress;
    }

    public void setTargetAddress(String targetAddress) {
        this.targetAddress = targetAddress;
    }
}
