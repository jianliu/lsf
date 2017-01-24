package com.liuj.lsf.demo.mock;

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

    public User getUser(int id) {
        System.out.println("println userId:"+id);
        User user = new User();
        user.setId(2);
        user.setName("夜半小夜曲");
        return user;
    }

    public void amVoid() {
        System.out.println("in method amVoid");
    }

    public User findByUser(User user) {
        System.out.println("in method findByUser" + user.getId());
        return user;
    }
}
