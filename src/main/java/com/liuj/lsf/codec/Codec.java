package com.liuj.lsf.codec;

/**
 * Created by cdliujian1 on 2016/6/19.
 */
public interface Codec {

    /**
     * decode bytes到对象
     *
     * @param bytes
     * @param clz
     * @param <T>
     * @return
     */
    <T> T decode(byte[] bytes, Class<T> clz);

    /**
     * 对象转byte[]
     * @param data
     * @return
     */
    byte[] encode(Object data);

}
