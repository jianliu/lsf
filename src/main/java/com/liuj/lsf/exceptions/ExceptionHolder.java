package com.liuj.lsf.exceptions;

import com.liuj.lsf.codec.Codec;
import com.liuj.lsf.codec.impl.JacksonCodec;

import java.io.Serializable;

/**
 * Created by cdliujian1 on 2016/11/7.
 */
public class ExceptionHolder implements Serializable{

    private static final long serialVersionUID = 1L;

    private transient LsfException lsfException;

    public static void main(String[] args) {
        Throwable t = null;
        try{
            Integer.valueOf(null);
        }catch (Throwable throwable){
            t=throwable;
        }
        ExceptionHolder exceptionHolder = new ExceptionHolder(new LsfException("haha",t));
        Codec codec = new JacksonCodec();
        byte[] bytes = codec.encode(exceptionHolder);
        ExceptionHolder et = codec.decode(bytes,ExceptionHolder.class);
        throw et.getLsfException();
    }

    public ExceptionHolder() {
    }

    public ExceptionHolder(LsfException lsfException) {
        this.lsfException = lsfException;
    }

    public void setLsfException(LsfException lsfException) {
        this.lsfException = lsfException;
    }

    public LsfException getLsfException() {
        return lsfException;
    }

}
