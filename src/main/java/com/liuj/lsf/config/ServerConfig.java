package com.liuj.lsf.config;

import java.util.Map;

/**
 * Created by cdliujian1 on 2016/10/31.
 */
public class ServerConfig {

    private String id;
    private String interfaceId;
    private Object impl;
    private String alias;
    private int workerPoolSize = 200;
    private Map<String, Object> params;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
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

    public int getWorkerPoolSize() {
        return workerPoolSize;
    }

    public void setWorkerPoolSize(int workerPoolSize) {
        this.workerPoolSize = workerPoolSize;
    }
}
