package com.liuj.lsf.route.impl;

import com.liuj.lsf.route.ClientRouteHandle;
import com.liuj.lsf.server.Provider;

import java.util.Collections;
import java.util.List;

/**
 * Created by cdliujian1 on 2017/5/27.
 */
public class DefaultClientRoutHandle implements ClientRouteHandle {

    private List<Provider> providers;

    public DefaultClientRoutHandle(Provider provider) {
        providers = Collections.singletonList(provider);
    }

    public void subscribeInterface(String interfaceId, String alisa) {

    }

    public List<Provider> route(String interfaceId, String alisa) {
        return providers;
    }


}
