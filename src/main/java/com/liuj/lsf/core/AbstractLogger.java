package com.liuj.lsf.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cdliujian1 on 2016/11/22.
 */
public abstract class AbstractLogger {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

}
