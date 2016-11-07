package com.liuj.lsf.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by cdliujian1 on 2016/11/1.
 */
@JsonIgnoreProperties("cause")
public class LsfException  extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

//    private String message;
//
//    private Throwable cause;

    public LsfException(String message) {
        super(message);
    }

    public LsfException(String message, Throwable cause) {
        super(message, cause);
    }


}
