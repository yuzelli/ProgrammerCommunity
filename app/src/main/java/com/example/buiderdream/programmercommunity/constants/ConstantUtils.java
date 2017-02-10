package com.example.buiderdream.programmercommunity.constants;

/**
 * Created by Administrator on 2016/12/6.
 * 常量类
 * @author 李秉龙
 */

public class ConstantUtils
{
    //闪屏页
    public static final int SPLASH_START_ACTIVITY = 0x00001000;
    //话题list
    public static final int TOPICLISTFRAGMENT_GET_DATA = 0x00001001;
    //话题的详细的页面
    public static final int THEMEDETAILED_GET_DATA = 0x00001002;
    //下拉刷新
    public static final int PULL_REFRESH = 0x00001003;
    //文章list
    public static final int ARTICLEFRAGMENT_GET_DATA = 0x00001004;
    //文章list加载以前数据
    public static final int ARTICLEFRAGMENT_GET_BEFORE_DATA = 0x00001005;
    //文章list加载以前数据
    public static final int ARTICLECONTENT_GET_DATA = 0x00001006;


    //用户注册成功
    public static final int REGISTER_GET_DATA = 0x00001007;
    //用户登录成功
    public static final int LOGIN_GET_DATA = 0x00001008;
    //用户设置页面requestCode
    public static final int PERSON_COMPILE_REQUESTCODE = 0x00001009;
    //用户编辑页面resultCode
    public static final int PERSON_COMPILE_RESULTCODE = 0x00001010;
    //用户修改信息成功
    public static final int PERSONCOMPILE_UP_DATA = 0x00001011;
    //上传主题
    public static final int THEME_UP_DATA = 0x00001012;
    //上传文章
    public static final int ARTICLE_UP_DATA = 0x00001013;
    //收藏的主题获取数据
    public static final int TOPIC_COLL_GET_DATA = 0x00001014;
    //收藏的文章获取数据
    public static final int ARTICLE_COLL_GET_DATA = 0x00001015;
    /**
     * v2ex
     */
    //主机地址
    public static final String V2EX_MAINFRAME_ADDRESS = "https://www.v2ex.com";
    public static final String HTTP = "http:";
    public static final String V2EX_THEME_ANSWER = "/api/replies/show.json";
    /**
     * 知乎
     */
    //主机地址
    public static final String ZH_MAINFRAME_ADDRESS = "http://news-at.zhihu.com";
    //最新
    public static final String ZH_ARTICLE_NEWS = "/api/4/news/latest";
    //之前数据
    public static final String ZH_ARTICLE_BEFOR = "/api/4/news/before/";
    //文章
    public static final String ZH_ARTICLE = "/api/4/news/";

//    SharedPerference
    //是否是用户第一次登录
    public static final String FIRST_LOGIN_SP = "PhoneHelperFirstLoginShared";
    public static final String FIRST_LOGIN_KEY = "firstUse" ;
    //登录用户信息
    public static final String USER_LOGIN_INFO = "UserInfo";
    //自己的后台
    public static final String USER_ADDRESS = "http://192.168.21.157:8080/CPbackstageService/";
    //用户操作
    public static final String USER_REGISTER = "userServlet";
    //主题
    public static final String TOPIC_REGISTER = "topicServlet";
    //收藏主题
    public static final String COLL_REGISTER = "collTopicServlet";
    //文章
    public static final String ARTICLE_REGISTER = "articleServlet";
    //收藏文章
    public static final String COLL_ARTICLE_REGISTER = "collArtucleServlet";

    //用户头像存放文件名
    public static final String AVATAR_FILE_PATH = "/userHeadImg.jpg";
    //七牛
    public static final String QN_ACCESSKEY = "1lz3oyLnZAMG3r0o6hsRUY_U45E58nb9-Q2mCzp8";
    public static final String QN_SECRETKEY = "1no9Tx1bAHSOC0g3xABHhsYXPbRKX_v3o_uGI0Nv";
    public static final String QN_IMG_ADDRESS = "http://ojterpx44.bkt.clouddn.com/";


}
