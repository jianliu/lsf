package com.liuj.lsf.demo;

import com.liuj.lsf.GlobalManager;
import com.liuj.lsf.client.Client;
import com.liuj.lsf.client.ProxyFactory;
import com.liuj.lsf.consumer.ConsumerConfig;
import com.liuj.lsf.mock.IService;
import com.liuj.lsf.mock.User;
import com.liuj.lsf.route.RouteHandle;
import com.liuj.lsf.route.impl.ZooKClientHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cdliujian1 on 2016/11/4.
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {

        //全局的handler
        RouteHandle zooKClientHandler = new ZooKClientHandler(GlobalManager.zookeeperRoot, GlobalManager.zookeeperServerHost, GlobalManager.timeout);

        ConsumerConfig consumerConfig = genConsumerBean();
        Client client = new Client(consumerConfig, zooKClientHandler);
        IService iService = ProxyFactory.buildProxy(IService.class, client);
        String response = null;
//        response = iService.println("you get this");
//        logger.info("response is:{}",response);
        response = iService.printlnException("oha you");
        logger.info("response is:{}", response);
        response = iService.println("wahaha");
        logger.info("response is:{}", response);
        User user = iService.getUser(2);
        logger.info("user is :{}-{}",user.getId(), user.getName() );
    }

    private static ConsumerConfig genConsumerBean() {
        ConsumerConfig consumerBean = new ConsumerConfig();
        consumerBean.setInterfaceClz("com.liuj.lsf.mock.IService");
        consumerBean.setAlias("test");
        consumerBean.setTimeout(200000);
        return consumerBean;
    }

}
