package com.liuj.lsf.route.impl;

import com.liuj.lsf.route.RouteHandle;
import com.liuj.lsf.route.ZooKRouteHandler;
import com.liuj.lsf.server.Provider;
import org.I0Itec.zkclient.IZkDataListener;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by liuj on 2016/11/7.
 */
public class ZooKClientHandler extends ZooKRouteHandler implements RouteHandle {

    private final ConcurrentMap<String, List<Provider>> allProviders = new ConcurrentHashMap<String, List<Provider>>();

    public ZooKClientHandler(String root, String serverString, int timeout) {
        super(root, serverString, timeout);
    }

    @Override
    public void subscribeInterface(String interfaceId, String alisa) {
        final String holePath = getDataPath(interfaceId, alisa);
        zkClient.subscribeDataChanges(holePath, new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                List<Provider> providerList = getZKData(dataPath, (List<String>) data);
                if (CollectionUtils.isNotEmpty(providerList)) {
                    changeProviders(providerList, holePath);
                    logger.debug("find new provides,change local cache.path:{},size:{}", dataPath, providerList.size());
                } else {
                    logger.warn("can not find valid providers, cache not change");
                }
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                logger.warn("path is deleted:{}", dataPath);
            }
        });
    }

    private void changeProviders(List<Provider> providerList, String holePath) {
        allProviders.put(holePath, providerList);
    }


    @Override
    public List<Provider> route(String interfaceId, String alisa) {
        String dataPath = getDataPath(interfaceId, alisa);

        List<Provider> providerList = allProviders.get(dataPath);
        if (CollectionUtils.isNotEmpty(providerList)) {
            return providerList;
        }

        try {
            List<String> providerStrList = zkClient.getChildren(dataPath);
            List<Provider> providers = getZKData(dataPath, providerStrList);
            if(CollectionUtils.isNotEmpty(providerList)){
                changeProviders(providers,dataPath);
            }
            return providers;
        }catch (Exception e){
            logger.error("获取path子节点异常", e);
        }
        return null;
    }

}
