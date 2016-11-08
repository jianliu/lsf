package com.liuj.lsf.client;

import javassist.*;
import com.liuj.lsf.config.ConsumerConfig;
import com.liuj.lsf.config.RequestMethod;
import com.liuj.lsf.msg.RequestMsg;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by cdliujian1 on 2016/11/4.
 */
public class ProxyFactory {

    /**
     * class pool cache
     */
    private final static ConcurrentHashMap<String, Object> CLASS_POOL_CACHE = new ConcurrentHashMap<String, Object>();

    private final static AtomicInteger CLASS_INDEX = new AtomicInteger(0);



    public static  <T> T buildProxy(Class<T> tClass, Client client) throws Exception {

        String key = client.getConsumerConfig().getInterfaceClz()+"/"+client.getConsumerConfig().getAlias();
        if(CLASS_POOL_CACHE.get(key)!=null){
            return (T) CLASS_POOL_CACHE.get(key);
        }

        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.makeClass(tClass.getName() + "_Proxy_" + CLASS_INDEX.incrementAndGet());
         CtClass ctClass =  pool.get(tClass.getCanonicalName());
        // cc.setSuperclass(ctClass);

        cc.addInterface(ctClass);
        cc.addField(CtField.make("public "+ Client.class.getCanonicalName()+" client=null;",cc));
        Method[] methods = tClass.getMethods();

        for(Method method: methods){
            buildMethod(method,cc);
        }

        Class clz = cc.toClass();
        cc.writeFile("E://tmp\\");
        T instance = (T) clz.newInstance();

        //add client instance
        clz.getField("client").set(instance,client);
        CLASS_POOL_CACHE.putIfAbsent(key, instance);
        return instance;
    }

    private static void buildMethod(Method method,CtClass cc) throws CannotCompileException {
        int modifiers = method.getModifiers();
        //only make abstract method
        if(!(Modifier.isAbstract(modifiers) && Modifier.isPublic(modifiers))){
            return;
        }
        Class<?>[] paramsTypes =  method.getParameterTypes();
        Class<?> returnType = method.getReturnType();

        StringBuilder sb = new StringBuilder();
        //like public Integer getInt(
        sb.append("public ").append(returnType.getCanonicalName()).append(" ").append(method.getName()).append("(");
        int i= 0;

        //append params
        for(Class<?> clazz: paramsTypes){
            sb.append(clazz.getCanonicalName()).append(" var").append(i);
            i++;
            if(i < paramsTypes.length){
                sb.append(",");
            }
        }
        sb.append(")");

        //append exception
        Class<?>[] exceptions = method.getExceptionTypes();
        if(exceptions.length >0 ){
            sb.append(" throws ");
            int j=0;
            for(Class<?> exception: exceptions){
                sb.append(exception.getCanonicalName());
                j++;
                if(j< exceptions.length){
                    sb.append(",");
                }
            }
        }

        //start method
        sb.append(" {");

        //method body


        sb.append(RequestMethod.class.getCanonicalName()).append(" requestDetail = new ")
                .append(RequestMethod.class.getCanonicalName()).append("();");
        sb.append("requestDetail.setMethod(\"").append(method.getName()).append("\");");
        sb.append("Object[] params = new Object[").append(i).append("];");
        sb.append("String[] parameterTypes = new String[").append(i).append("];");
        if(i>0){
            for(int j=0;j<i;j++){
                sb.append("params[").append(j).append("] = ($w)$").append(j+1).append(" ;");
                sb.append("parameterTypes[").append(j).append("] = \"").append(paramsTypes[j].getCanonicalName()).append("\" ;");
            }
        }
        sb.append("requestDetail.setMethodParams(params);");
        sb.append("requestDetail.setParameterTypes(parameterTypes);");

        sb.append(ConsumerConfig.class.getCanonicalName()).append(" newConsumerConfig = new ")
                .append(ConsumerConfig.class.getCanonicalName()).append("();");
        sb.append("newConsumerConfig.setInterfaceClz(client.getConsumerConfig().getInterfaceClz());");
        sb.append("newConsumerConfig.setAlias(client.getConsumerConfig().getAlias());");

        sb.append(RequestMsg.class.getCanonicalName()).append(" requestMsg = ")
                .append(RequestFactory.class.getCanonicalName()).append(".buildRequest(newConsumerConfig,requestDetail);");

        if(returnType.equals(void.class)){
            sb.append("return;");
        }else {
            sb.append("return (").append(returnType.getCanonicalName()).append(")client.invoke(requestMsg);");
        }

        //end method
        sb.append("}");

        CtMethod ctMethod = CtNewMethod.make(sb.toString(), cc);
        cc.addMethod(ctMethod);
    }


}
