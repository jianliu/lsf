package com.liuj.lsf.config;

/**
 * Created by cdliujian1 on 2016/10/31.
 */
public class RequestMethod {

    private String method;

    private Object[] methodParams;

    private String[] parameterTypes;

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

    public String[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(String[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }
}
