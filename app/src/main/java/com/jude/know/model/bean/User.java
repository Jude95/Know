package com.jude.know.model.bean;

import java.io.Serializable;

/**
 * Created by Mr.Jude on 2015/7/29.
 */
public class User implements Serializable{
    private String id;
    private String name;
    private String face;
    private String token;

    public User(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

}
