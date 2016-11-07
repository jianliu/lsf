package com.liuj.lsf.server;

import io.netty.channel.ChannelInboundHandlerAdapter;
import com.liuj.lsf.consumer.ConsumerConfig;
import com.liuj.lsf.exceptions.ExceptionHolder;
import com.liuj.lsf.exceptions.LsfException;
import com.liuj.lsf.utils.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by cdliujian1 on 2016/10/31.
 */
public abstract class AbstractServerHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    protected ServerContainer serverContainer = new ServerContainer();

    protected Object invoke(ConsumerConfig consumerBean){
        try {
            Class targetClz = Class.forName(consumerBean.getInterfaceClz());
            //最终执行方法的实例
            Object instance = getServerInstanceByConsumer(consumerBean,targetClz);
            String methodName = consumerBean.getConsumerDetail().getMethod();
            Object[] params = consumerBean.getConsumerDetail().getMethodParams();

            Class<?>[] parameterTypes = new Class<?>[params.length];
            for(int i=0;i< params.length;i++){
                parameterTypes[i]= getParameterTypes(params[i]);
            }
            Method method =targetClz.getMethod(methodName,parameterTypes);
            return method.invoke(instance,params);
        } catch (Throwable throwable) {
            logger.error("执行方法异常 接口:{},method:{}",consumerBean.getInterfaceClz(),
                    consumerBean.getConsumerDetail().getMethod(), throwable);
            return new ExceptionHolder(new LsfException("server端执行失败", throwable));
        }
    }

    protected <T> T getServerInstanceByConsumer(ConsumerConfig consumerBean, Class<T> clz){
        ServerConfig serverBean = new ServerConfig();
        serverBean.setInterfaceClz(consumerBean.getInterfaceClz());
        serverBean.setAlias(consumerBean.getAlias());
       return serverContainer.getServer(serverBean,clz);
    }

    private Class<?> getParameterTypes(Object pa){
        return ReflectionUtils.forName(pa.getClass().getName());
    }


    public static void main(String[] args) {

    }

}