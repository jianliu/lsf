package com.liuj.lsf.demo;

import com.liuj.lsf.GlobalManager;
import com.liuj.lsf.config.ServerConfig;
import com.liuj.lsf.demo.mock.IServerImpl;
import com.liuj.lsf.demo.mock.IService;
import com.liuj.lsf.route.ServerRoute;
import com.liuj.lsf.route.impl.ZooKServerHandler;
import com.liuj.lsf.server.Server;
import com.liuj.lsf.server.ServerHandler;

/**
 * Created by cdliujian1 on 2016/11/8.
 */
public class ServerMain {

    public static void main(String[] args) throws Exception {
        ServerRoute serverRoute1 = new ZooKServerHandler(GlobalManager.zookeeperRoot, GlobalManager.zookeeperServerHost, GlobalManager.timeout);

        ServerConfig serverBean = new ServerConfig();
        serverBean.setAlias("test");
        serverBean.setInterfaceId(IService.class.getCanonicalName());
        serverBean.setImpl(new IServerImpl());

        final Server server = new Server(serverRoute1,new ServerHandler(), GlobalManager.serverPort);
        Thread t= new Thread(new Runnable() {
            public void run() {
                //start server
                try {
                    server.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        server.registerServer(serverBean);


    }

}
