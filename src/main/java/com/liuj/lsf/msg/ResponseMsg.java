package com.liuj.lsf.msg;

/**
 * Created by cdliujian1 on 2016/6/15.
 */
public class ResponseMsg extends BaseMsg{

    private long receiveTime;

    //this message is msgBody
    private Object response;

    public long getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(long receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }
}
