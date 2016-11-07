package com.liuj.lsf.msg;

/**
 * Created by cdliujian1 on 2016/6/19.
 */
public class MsgHeader {

    private long msgId;
    /**
     * 消息类型
     */
    private int msgType;

    /**
     * 消息体的类型
     */
    private String clz;

    /**
     * body长度
     */
    private Integer length;

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getClz() {
        return clz;
    }

    public void setClz(String clz) {
        this.clz = clz;
    }
}
