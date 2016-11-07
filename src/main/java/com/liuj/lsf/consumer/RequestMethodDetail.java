package com.liuj.lsf.consumer;

/**
 * Created by cdliujian1 on 2016/10/31.
 */
public class RequestMethodDetail {

    private String method;

    private Object[] methodParams;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }

    public void setMethodParams(Object[] methodParams) {
        this.methodParams = methodParams;
    }

    public void setMethodParams0(Object... methodParams) {
        this.methodParams = methodParams;
    }
}
