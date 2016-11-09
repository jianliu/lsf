package com.liuj.lsf.demo;

import com.liuj.lsf.demo.mock.User;
import javassist.ClassPool;
import javassist.NotFoundException;

/**
 * Created by cdliujian1 on 2016/11/8.
 */
public class JavassistTest {

    public static void main(String[] args) throws NotFoundException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        ClassPool classPool = ClassPool.getDefault();

        SampleLoader myLoader = new SampleLoader();
        Class clazz = myLoader.loadClass("com.liuj.lsf.demo.mock.User");
        Object obj = clazz.newInstance();
        User b = (User) obj;    // this always throws ClassCastException.
    }

}
