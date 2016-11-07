package com.liuj.lsf.mock;

import java.io.Serializable;

/**
 * Created by liuj on 2016/11/7.
 */
public class User implements Serializable{

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
