package com.example.buiderdream.programmercommunity.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/17.
 */

public class TopicCollection implements Serializable{

    /**
     * collection_topic_id : 3
     * t_content : 本人花了两个多月的时间开发了一个接口管理平台，结合了 postman 和 rap 的很多特点，后台用 nodejs mongodb ，前端用 vue bootstrap ，可以团队协作开发， mock 数据，编写文档，内网测试等等，都是可视化操作，期待各位的使用：地址： http://123.57.77.6/    产品介绍地址： http://mp.weixin.qq.com/s/FU2w02ROQw1lm4s2X3ybsQ
     * t_createtime : 1484546821
     * t_imageurl : //cdn.v2ex.co/gravatar/8e054171da06c7973588d4cbe77ea0e3?s=24
     * t_number : 1
     * t_replies : 0
     * t_title : SBDoc:开发的接口管理平台，欢迎大家围观
     * t_username : programmer
     * topicid : 11
     * typeString : 程序员
     */

    private int collection_topic_id;
    private String t_content;
    private String t_createtime;
    private String t_imageurl;
    private int t_number;
    private int t_replies;
    private String t_title;
    private String t_username;
    private int topicid;
    private String typeString;

    public int getCollection_topic_id() {
        return collection_topic_id;
    }

    public void setCollection_topic_id(int collection_topic_id) {
        this.collection_topic_id = collection_topic_id;
    }

    public String getT_content() {
        return t_content;
    }

    public void setT_content(String t_content) {
        this.t_content = t_content;
    }

    public String getT_createtime() {
        return t_createtime;
    }

    public void setT_createtime(String t_createtime) {
        this.t_createtime = t_createtime;
    }

    public String getT_imageurl() {
        return t_imageurl;
    }

    public void setT_imageurl(String t_imageurl) {
        this.t_imageurl = t_imageurl;
    }

    public int getT_number() {
        return t_number;
    }

    public void setT_number(int t_number) {
        this.t_number = t_number;
    }

    public int getT_replies() {
        return t_replies;
    }

    public void setT_replies(int t_replies) {
        this.t_replies = t_replies;
    }

    public String getT_title() {
        return t_title;
    }

    public void setT_title(String t_title) {
        this.t_title = t_title;
    }

    public String getT_username() {
        return t_username;
    }

    public void setT_username(String t_username) {
        this.t_username = t_username;
    }

    public int getTopicid() {
        return topicid;
    }

    public void setTopicid(int topicid) {
        this.topicid = topicid;
    }

    public String getTypeString() {
        return typeString;
    }

    public void setTypeString(String typeString) {
        this.typeString = typeString;
    }
}
