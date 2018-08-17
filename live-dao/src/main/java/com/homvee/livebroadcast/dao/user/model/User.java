package com.homvee.livebroadcast.dao.user.model;

import com.homvee.livebroadcast.dao.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-17 10:54
 */
@Entity
@Table(name = "t_user")
public class User  extends BaseEntity {

    private String userName;
    private String pwd;
    private String authKey;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }
}
