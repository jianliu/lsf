package com.liuj.lsf.client;

import com.liuj.lsf.config.ConsumerConfig;
import com.liuj.lsf.route.RouteHandle;

/**
 * Created by cdliujian1 on 2016/11/4.
 */
public class ClientFactory {

    public static Client buildClient(ConsumerConfig consumerConfig, RouteHandle routeHandle) {
        return new Client(consumerConfig, routeHandle);
    }

}
