package com.liuj.lsf.mock;

/**
 * Created by cdliujian1 on 2016/11/4.
 */
public interface IService {

    String println(String ha);

    String printlnException(String n) throws Exception;

    //todo type int will cannot be javassist class
    User getUser(Integer id);
}
