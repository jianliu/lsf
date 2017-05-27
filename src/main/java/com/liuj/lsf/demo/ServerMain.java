package com.liuj.lsf.demo;

import com.liuj.lsf.GlobalManager;
import com.liuj.lsf.config.ServerConfig;
import com.liuj.lsf.core.RequestHandle;
import com.liuj.lsf.core.impl.LsfRequestServerHandle;
import com.liuj.lsf.demo.mock.IServerImpl;
import com.liuj.lsf.demo.mock.IService;
import com.liuj.lsf.route.ServerRoute;
import com.liuj.lsf.route.impl.DefaultServerRoute;
import com.liuj.lsf.route.impl.ZooKServerRouteHandler;
import com.liuj.lsf.server.Server;
import com.liuj.lsf.server.ServerHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cdliujian1 on 2016/11/8.
 */
public class ServerMain {

    public static void main(String[] args) throws Exception {
//        ServerRoute serverRoute1 = new ZooKServerRouteHandler(GlobalManager.zookeeperRoot, GlobalManager.zookeeperServerHost, GlobalManager.timeout);

        List<RequestHandle> requestHandleList = new ArrayList<RequestHandle>();
        requestHandleList.add(new LsfRequestServerHandle());

        final Server server = new Server(new DefaultServerRoute(),new ServerHandler(requestHandleList), GlobalManager.serverPort);
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

        ServerConfig serverBean = new ServerConfig();
        serverBean.setAlias("test");
        serverBean.setInterfaceId(IService.class.getCanonicalName());
        serverBean.setImpl(new IServerImpl());
        server.registerServer(serverBean);


    }

}
