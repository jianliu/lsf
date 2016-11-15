package com.liuj.lsf.demo;

import com.liuj.lsf.GlobalManager;
import com.liuj.lsf.client.Client;
import com.liuj.lsf.client.ClientFactory;
import com.liuj.lsf.client.ProxyFactory;
import com.liuj.lsf.config.ConsumerConfig;
import com.liuj.lsf.demo.mock.IService;
import com.liuj.lsf.demo.mock.User;
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

        Client client = ClientFactory.buildClient(consumerConfig, zooKClientHandler);

        IService iService = ProxyFactory.buildProxy(IService.class, client);
        iService.amVoid();
        User user = new User();
        user.setId(2);
        user.setName("what");
        for(int i=0;i<1000;i++) {
            User newUser = iService.findByUser(user);
            logger.info("user is :{}-{}", newUser.getId(), newUser.getName());
        }
        String response = null;
////        response = iService.println("you get this");
////        logger.info("response is:{}",response);
//        response = iService.printlnException("oha you");
//        logger.info("response is:{}", response);
//        response = iService.println("wahaha");
//        logger.info("response is:{}", response);
//        User user = iService.getUser(2);
//        logger.info("user is :{}-{}",user.getId(), user.getName() );
    }

    private static ConsumerConfig genConsumerBean() {
        ConsumerConfig consumerBean = new ConsumerConfig();
        consumerBean.setInterfaceId(IService.class.getCanonicalName());
        consumerBean.setAlias("test");
        consumerBean.setTimeout(200000);
        return consumerBean;
    }

}
