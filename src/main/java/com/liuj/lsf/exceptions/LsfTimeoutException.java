package com.liuj.lsf.exceptions;

import java.io.Serializable;

/**
 * Created by cdliujian1 on 2016/11/4.
 */
public class LsfTimeoutException extends LsfException implements Serializable {

    private static final long serialVersionUID = 1L;

    public LsfTimeoutException(String message) {
        super(message);
    }

    public LsfTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
