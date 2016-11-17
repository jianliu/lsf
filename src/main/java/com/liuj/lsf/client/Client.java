package com.liuj.lsf.client;

import io.netty.channel.Channel;
import com.liuj.lsf.config.ConsumerConfig;
import com.liuj.lsf.exceptions.ExceptionHolder;
import com.liuj.lsf.exceptions.LsfException;
import com.liuj.lsf.msg.BaseMsg;
import com.liuj.lsf.msg.ResponseMsg;
import com.liuj.lsf.route.RouteHandle;
import com.liuj.lsf.server.Provider;
import com.liuj.lsf.transport.Connection;
import com.liuj.lsf.transport.impl.DefaultClientTransport;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by cdliujian1 on 2016/6/19.
 */
public class Client {

    private Logger logger = LoggerFactory.getLogger(Client.class);
    private ConsumerConfig consumerConfig;
    private List<Connection> connectionList = new ArrayList<Connection>();
    private ReentrantLock lock = new ReentrantLock();
    boolean hasInit = false;

    private RouteHandle routeHandle;

    public Client(ConsumerConfig consumerConfig, RouteHandle routeHandle) {
        this.consumerConfig = consumerConfig;
        this.routeHandle = routeHandle;
        if(routeHandle != null) {
            routeHandle.subscribeInterface(consumerConfig.getInterfaceId(), consumerConfig.getAlias());
        }
        if(consumerConfig!=null && !consumerConfig.isLazy()) {
            init();
        }
    }


    /**
     * 通过反射或javassist直接调用
     * @param baseMsg
     * @return
     * @throws Exception
     */
    public Object invoke(BaseMsg baseMsg) throws Exception {
        if(!hasInit){
            init();
        }

        if(CollectionUtils.isEmpty(connectionList)){
            throw new LsfException("没有可用的连接");
        }
        try {
            Connection connection = connectionList.get(0);
            ResponseMsg responseMsg = connection.getClientTransport().sendMsg(baseMsg);
            Object responseResult = responseMsg.getResponse();
            if(responseResult instanceof ExceptionHolder){
                throw ((ExceptionHolder) responseResult).getLsfException();
            }
            return responseResult;
        }catch (Exception e){
             throw e;
        }
    }

    public void init() {
        lock.lock();
        try {
            List<Provider> providerList = routeHandle.route(consumerConfig.getInterfaceId(), consumerConfig.getAlias());
            if(CollectionUtils.isEmpty(providerList)){
                return;
            }
            for (Provider provider : providerList) {
                Connection connection = buildConnection(provider);
                if (connection == null) {
                    logger.warn("provider is invalid,host--{},port--{}", provider.getHost(), provider.getPort());
                } else {
                    connectionList.add(connection);
                }
            }
        }finally {
            hasInit = true;
            lock.unlock();
        }
    }

    private Connection buildConnection(Provider provider) {
        try {
            Channel channel = ClientTransportFactory.buildChannel(provider);
            DefaultClientTransport clientTransport = ClientTransportFactory.buildTransport(provider, channel);
            clientTransport.setConsumerConfig(this.consumerConfig);
            Connection connection = new Connection();
            connection.setProvider(provider);
            connection.setClientTransport(clientTransport);
            logger.info("connect to server success,host--{},port--{}", provider.getHost(), provider.getPort());
            return connection;
        }catch (Exception e){
            logger.error("something is wrong on buildConnection",e);
        }
        return null;
    }

    public ConsumerConfig getConsumerConfig() {
        return consumerConfig;
    }

    public void setConsumerConfig(ConsumerConfig consumerConfig) {
        this.consumerConfig = consumerConfig;
    }
}
