package com.liuj.lsf.server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by cdliujian1 on 2016/10/31.
 */
public class ServerContainer {

    private final static ConcurrentMap<String, ServerConfig> serverMap = new ConcurrentHashMap<String, ServerConfig>();

    public static void addServerBean(ServerConfig serverBean) {
        serverMap.put(serverBean.getInterfaceClz() + "/" + serverBean.getAlias(), serverBean);
    }

    public <T> T getServer(ServerConfig serverBean, Class<T> clz) {
        if (serverBean.getImpl() != null) {
            return (T) serverBean.getImpl();
        }
        String key = serverBean.getInterfaceClz() + "/" + serverBean.getAlias();
        serverBean = serverMap.get(key);
        if(serverBean != null){
            return (T)serverBean.getImpl();
        }
        return null;
    }


}
