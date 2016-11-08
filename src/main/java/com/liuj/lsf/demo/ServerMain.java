package com.liuj.lsf.demo;

import com.liuj.lsf.GlobalManager;
import com.liuj.lsf.config.ServerConfig;
import com.liuj.lsf.demo.mock.IServerImpl;
import com.liuj.lsf.demo.mock.IService;
import com.liuj.lsf.route.ServerRoute;
import com.liuj.lsf.route.impl.ZooKServerHandler;
import com.liuj.lsf.server.Server;

/**
 * Created by cdliujian1 on 2016/11/8.
 */
public class ServerMain {

    public static void main(String[] args) throws Exception {
        ServerRoute serverRoute1 = new ZooKServerHandler(GlobalManager.zookeeperRoot, GlobalManager.zookeeperServerHost, GlobalManager.timeout);
        Server server = new Server(serverRoute1);

        ServerConfig serverBean = new ServerConfig();
        serverBean.setAlias("test");
        serverBean.setInterfaceClz(IService.class.getCanonicalName());
        serverBean.setImpl(new IServerImpl());

        server.registerServer(serverBean);
        //start server
        server.run();

    }

}
