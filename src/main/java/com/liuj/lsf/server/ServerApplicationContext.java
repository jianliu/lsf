package com.liuj.lsf.server;

import com.liuj.lsf.config.ServerConfig;
import com.liuj.lsf.config.ConsumerConfig;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by cdliujian1 on 2016/10/31.
 */
public class ServerApplicationContext implements ApplicationContextAware{

    private static ApplicationContext applicationContext;

    public String getBeanType(ConsumerConfig consumerBean){
        return consumerBean.getInterfaceId();
    }

    public ServerConfig getServerBean(Class<?> type, String clz){
        ServerConfig serverBean = new ServerConfig();
        Object instance = applicationContext.getBean(type);
        serverBean.setId("");
        serverBean.setImpl(instance);
        serverBean.setParams(null);
        serverBean.setInterfaceId(clz);
        return serverBean;
    }


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
