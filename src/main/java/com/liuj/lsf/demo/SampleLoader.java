package com.liuj.lsf.demo;

import com.liuj.lsf.client.ProxyFactory;
import javassist.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by cdliujian1 on 2016/11/8.
 */
public class SampleLoader  extends Loader {
    /* Call MyApp.main().
     */
    public static void main(String[] args) throws Throwable {
        SampleLoader s = new SampleLoader();
        final Class c = s.loadClass("com.liuj.lsf.demo.JavassistTest");
        final String[] args_ = args;
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    c.getDeclaredMethod("main", new Class[] { String[].class })
                            .invoke(null, new Object[] { args_ });
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        },"it");

        thread.setContextClassLoader(s);
        thread.start();
    }

    private ClassPool pool;

    public SampleLoader() throws NotFoundException {
//        pool = ClassPool.getDefault();
//        pool.getClassLoader();
        pool = new ClassPool(false);
        pool.insertClassPath(new ClassClassPath(ProxyFactory.class));

//        pool.insertClassPath("E:\\sanfang-workspace\\my\\lsf\\target\\classes"); // MyApp.class must be there.
    }

    /* Finds a specified class.
     * The bytecode for that class can be modified.
     */
    protected Class findClass(String name) throws ClassNotFoundException {
        try {
            CtClass cc = pool.get(name);
            // modify the CtClass object here
            byte[] b = cc.toBytecode();
            return defineClass(name, b, 0, b.length);
        } catch (NotFoundException e) {
            throw new ClassNotFoundException();
        } catch (IOException e) {
            throw new ClassNotFoundException();
        } catch (CannotCompileException e) {
            throw new ClassNotFoundException();
        }
    }
}