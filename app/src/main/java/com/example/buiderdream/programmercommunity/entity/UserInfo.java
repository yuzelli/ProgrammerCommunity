package com.example.buiderdream.programmercommunity.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/2.
 */

public class UserInfo implements Serializable {

    /**
     * u_address :北京
     * u_imageurl : 1
     * u_introduction : 啦啦啦啦
     * u_name : lbl
     * u_password : 123123123
     * u_phone : 11144443333
     * userid : 6
     */

    private String u_address;
    private String u_imageurl;
    private String u_introduction;
    private String u_name;
    private String u_password;
    private String u_phone;
    private String u_technology;
    private int userid;

    public String getU_technology() {
        return u_technology;
    }

    public void setU_technology(String u_technology) {
        this.u_technology = u_technology;
    }

    public String getU_address() {
        return u_address;
    }

    public void setU_address(String u_address) {
        this.u_address = u_address;
    }

    public String getU_imageurl() {
        return u_imageurl;
    }

    public void setU_imageurl(String u_imageurl) {
        this.u_imageurl = u_imageurl;
    }

    public String getU_introduction() {
        return u_introduction;
    }

    public void setU_introduction(String u_introduction) {
        this.u_introduction = u_introduction;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public String getU_password() {
        return u_password;
    }

    public void setU_password(String u_password) {
        this.u_password = u_password;
    }

    public String getU_phone() {
        return u_phone;
    }

    public void setU_phone(String u_phone) {
        this.u_phone = u_phone;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
