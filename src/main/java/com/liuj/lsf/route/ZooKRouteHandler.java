package com.liuj.lsf.route;

import com.liuj.lsf.server.Provider;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuj on 2016/11/7.
 */
public abstract class ZooKRouteHandler {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());


    private String root;

    private String serverString = "127.0.0.1:4180,127.0.0.1:4181,127.0.0.1:4182";

    private int timeout = 50000;

    protected ZkClient zkClient;

    public ZooKRouteHandler(String root, String serverString, int timeout) {
        this.root = root;
        this.serverString = serverString;
        this.timeout = timeout;
        zkClient = new ZkClient(serverString, timeout);
        if (!zkClient.exists(root)) {
            zkClient.createPersistent(root, true);
        }
    }

    public List<Provider> getZKData(String dataPath, List<String> providerStrList) {
        if (CollectionUtils.isEmpty(providerStrList)) {
            return null;
        }
        List<Provider> providerList = new ArrayList<Provider>(providerStrList.size());
        Provider provider;
        for (String pStr : providerStrList) {
            try {
                String[] pArr = pStr.split(":");
                provider = new Provider();
                provider.setHost(pArr[0]);
                provider.setPort(Integer.valueOf(pArr[1]));
                providerList.add(provider);
            } catch (Exception e) {
                logger.error("不可解析的地址--path:{}--{}", dataPath, pStr);
            }
        }

        if (providerList.isEmpty()) {
            logger.warn("不可解析的地址--path:{}", dataPath);
        }
        return providerList;
    }

    protected String getDataPath(String childPath) {
        return (root + "/" + childPath).replace("//", "/");
    }

    protected String getDataPath(String childPath, String alisa) {
        return (root + "/" + childPath + "/" + alisa).replace("//", "/");
    }

    public String getRoot() {
        return root;
    }

    public String getServerString() {
        return serverString;
    }

    public int getTimeout() {
        return timeout;
    }

}
