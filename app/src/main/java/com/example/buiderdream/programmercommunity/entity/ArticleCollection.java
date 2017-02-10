package com.example.buiderdream.programmercommunity.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/17.
 */

public class ArticleCollection implements Serializable {


    /**
     * a_imgurl : http://pic1.zhimg.com/a92b88a959b02720ffa8700c6492ae0c.jpg
     * a_title : 星座、MBTI 和大五人格，什么样的人格测量才算准？
     * articled : 3
     * collection_art_id : 13
     * z_number : 9154078
     */

    private String a_imgurl;
    private String a_title;
    private int articled;
    private int collection_art_id;
    private int z_number;

    public String getA_imgurl() {
        return a_imgurl;
    }

    public void setA_imgurl(String a_imgurl) {
        this.a_imgurl = a_imgurl;
    }

    public String getA_title() {
        return a_title;
    }

    public void setA_title(String a_title) {
        this.a_title = a_title;
    }

    public int getArticled() {
        return articled;
    }

    public void setArticled(int articled) {
        this.articled = articled;
    }

    public int getCollection_art_id() {
        return collection_art_id;
    }

    public void setCollection_art_id(int collection_art_id) {
        this.collection_art_id = collection_art_id;
    }

    public int getZ_number() {
        return z_number;
    }

    public void setZ_number(int z_number) {
        this.z_number = z_number;
    }
}
