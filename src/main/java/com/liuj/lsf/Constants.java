package com.liuj.lsf;

/**
 * Created by cdliujian1 on 2016/6/19.
 */
public class Constants {

    /**
     * 协议头的魔术位
     */
    public static final byte[] MAGICCODEBYTE = new byte[]{(byte) 0xAD, (byte) 0xBF};

    /*---------消息类型开始-----------*/
    public static final int REQUEST_MSG = 1;

    public static final int RESPONSE_MSG = 2;

    public static final String Client_Handler = "com.liuj.lsf.Client_Handler";

    public static final String NULL_RESULT_CLASS = "null";

}
