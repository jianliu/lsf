package com.liuj.lsf.demo;

import com.liuj.lsf.GlobalManager;
import com.liuj.lsf.client.Client;
import com.liuj.lsf.client.ClientFactory;
import com.liuj.lsf.client.ProxyFactory;
import com.liuj.lsf.config.ConsumerConfig;
import com.liuj.lsf.demo.mock.IService;
import com.liuj.lsf.route.impl.DefaultClientRoutHandle;
import com.liuj.lsf.server.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端启动
 * Created by cdliujian1 on 2016/11/4.
 */
public class ClientMain {

    private static final Logger logger = LoggerFactory.getLogger(ClientMain.class);

    public static void main(String[] args) throws Exception {

        //全局的handler
//        ClientRouteHandle zooKClientHandler = new ZooKClientHandler(GlobalManager.zookeeperRoot, GlobalManager.zookeeperServerHost, GlobalManager.timeout);
        ConsumerConfig consumerConfig = genConsumerConfig();

        Provider provider = new Provider("127.0.0.1", GlobalManager.serverPort);
        Client client = ClientFactory.buildClient(consumerConfig, new DefaultClientRoutHandle(provider));

        IService iService = ProxyFactory.buildProxy(IService.class, client);
//        User user = new User();
//        user.setName("xx");
//        user.setId(1);
//                    User newUser = iService.findByUser(user);

        iService.amVoid();
//        User user = new User();
//
//        user.setName("what");
//        long start = System.currentTimeMillis();
//        long na =System.nanoTime();
//        for(int i=0;i<1;i++) {
//            user.setId(i);
//            User newUser = iService.findByUser(user);
//            logger.info("user is :{}-{}", newUser.getId(), newUser.getName());
//        }
//        logger.info("cost time:{}na",System.nanoTime() - na);
//        logger.info("cst time:{}ms", System.currentTimeMillis() - start);
//        String response = null;
////        response = iService.println("you get this");
////        logger.info("response is:{}",response);
//        response = iService.printlnException("oha you");
//        logger.info("response is:{}", response);
//        response = iService.println("wahaha");
//        logger.info("response is:{}", response);
//        User user = iService.getUser(2);
//        logger.info("user is :{}-{}",user.getId(), user.getName() );
    }

    private static ConsumerConfig genConsumerConfig() {
        ConsumerConfig consumerConfig = new ConsumerConfig();
        consumerConfig.setInterfaceId(IService.class.getCanonicalName());
        consumerConfig.setAlias("test");
        consumerConfig.setTimeout(3000);
        return consumerConfig;
    }

}
