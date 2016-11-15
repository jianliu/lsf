package com.liuj.lsf;

/**
 * Created by liuj on 2016/11/7.
 */
public class GlobalManager {

    //server的启动port
    public static int serverPort = 2222;

    public static String zookeeperRoot = "/lsf3";

    public static String zookeeperServerHost = "127.0.0.1:2181";

    public static int timeout = 10000;

    public static void setServerPort(int serverPort) {
        GlobalManager.serverPort = serverPort;
    }
}
