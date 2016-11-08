package com.liuj.lsf.config;

import java.util.Map;

/**
 * Created by cdliujian1 on 2016/10/31.
 */
public class ServerConfig {

    private String id;
    private String interfaceClz;
    private Object impl;
    private String alias;
    private Map<String, Object> params;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInterfaceClz() {
        return interfaceClz;
    }

    public void setInterfaceClz(String interfaceClz) {
        this.interfaceClz = interfaceClz;
    }

    public Object getImpl() {
        return impl;
    }

    public void setImpl(Object impl) {
        this.impl = impl;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
