package com.liuj.lsf.mock;

/**
 *
 * Created by cdliujian1 on 2016/10/31.
 */
public class IServerImpl implements IService{

    public String println(String ha){
        System.out.println(ha);
        return ha+" deis";
    }

    public String printlnException(String n) throws Exception {
        return n + ",miss u";
    }

    @Override
    public User getUser(Integer id) {
        System.out.println("println userId:"+id);
        User user = new User();
        user.setId(2);
        user.setName("夜半小夜曲");
        return user;
    }

}
