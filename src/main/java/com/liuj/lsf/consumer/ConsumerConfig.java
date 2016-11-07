package com.liuj.lsf.consumer;

import java.util.Map;

/**
 * Created by cdliujian1 on 2016/10/31.
 */
public class ConsumerConfig {

    private String id;
    private String interfaceClz;
    private String alias;
    private String protocol;
    private int timeout;
    private boolean lazy = false;

    private RequestMethodDetail consumerDetail;
    /**
     * lsf自定义参数
     */
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public RequestMethodDetail getConsumerDetail() {
        return consumerDetail;
    }

    public void setConsumerDetail(RequestMethodDetail consumerDetail) {
        this.consumerDetail = consumerDetail;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public boolean isLazy() {
        return lazy;
    }

    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }
}
