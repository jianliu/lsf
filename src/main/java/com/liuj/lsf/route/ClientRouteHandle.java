package com.liuj.lsf.route;

import com.liuj.lsf.server.Provider;

import java.util.List;

/**
 * Created by liuj on 2016/11/7.
 */
public interface ClientRouteHandle {

    /**
     * 订阅节点变更
     * @param interfaceId
     * @param alisa
     */
    public void subscribeInterface(String interfaceId, String alisa);

    /**
     * 获取server列表
     * @param interfaceId
     * @param alisa
     * @return
     */
    List<Provider> route(String interfaceId, String alisa);

}
