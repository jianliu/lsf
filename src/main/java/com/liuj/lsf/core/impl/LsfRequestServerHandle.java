package com.liuj.lsf.core.impl;

import com.liuj.lsf.config.ConsumerConfig;
import com.liuj.lsf.config.ServerConfig;
import com.liuj.lsf.core.AbstractLogger;
import com.liuj.lsf.core.RequestHandle;
import com.liuj.lsf.exceptions.ExceptionHolder;
import com.liuj.lsf.exceptions.LsfException;
import com.liuj.lsf.msg.MsgHeader;
import com.liuj.lsf.msg.RequestMsg;
import com.liuj.lsf.server.ServerContainer;
import com.liuj.lsf.utils.ReflectionUtils;
import io.netty.channel.Channel;

import java.lang.reflect.Method;

/**
 * Created by cdliujian1 on 2016/11/22.
 * lsf server端处理Request的处理器
 */
public class LsfRequestServerHandle extends AbstractLogger implements RequestHandle {

    @Deprecated
    protected ServerContainer serverContainer = new ServerContainer();

    /**
     * 是否可以处理
     * @param req
     * @return
     */
    public boolean canHandle(Object req) {
        return req != null && req instanceof ConsumerConfig;
    }

    /**
     * 处理请求
     * @param channel
     * @param req
     * @return
     */
    public Object handleReq(MsgHeader msgHeader, Channel channel, Object req) {
        return invoke((ConsumerConfig) req);
    }

    protected Object invoke(ConsumerConfig consumerBean) {
        try {
            Class targetClz = Class.forName(consumerBean.getInterfaceId());
            //最终执行方法的实例
            Object instance = getServerInstanceByConsumer(consumerBean, targetClz);
            String methodName = consumerBean.getRequestMethod().getMethod();
            Object[] params = consumerBean.getRequestMethod().getMethodParams();
            Class[] parameterTypes = new Class[params.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                String type = consumerBean.getRequestMethod().getParameterTypes()[i];
                parameterTypes[i] = ReflectionUtils.forName(type);
            }
            Method method = targetClz.getMethod(methodName, parameterTypes);
            return method.invoke(instance, params);
        } catch (Throwable throwable) {
            logger.error("执行方法异常 接口:{},method:{}", consumerBean.getInterfaceId(),
                    consumerBean.getRequestMethod().getMethod(), throwable);
            return new ExceptionHolder(new LsfException("server端执行失败", throwable));
        }
    }


    protected <T> T getServerInstanceByConsumer(ConsumerConfig consumerBean, Class<T> clz) {
        ServerConfig serverBean = new ServerConfig();
        serverBean.setInterfaceId(consumerBean.getInterfaceId());
        serverBean.setAlias(consumerBean.getAlias());
        return ServerContainer.getServer(serverBean, clz);
    }

}
