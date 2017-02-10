package com.example.buiderdream.programmercommunity.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.buiderdream.programmercommunity.R;
import com.example.buiderdream.programmercommunity.base.BaseActivity;
import com.example.buiderdream.programmercommunity.constants.ConstantUtils;
import com.example.buiderdream.programmercommunity.entity.ArticleInfo;
import com.example.buiderdream.programmercommunity.entity.UserInfo;
import com.example.buiderdream.programmercommunity.https.OkHttpClientManager;
import com.example.buiderdream.programmercommunity.utils.SharePreferencesUtil;
import com.example.buiderdream.programmercommunity.widgets.ObservableWebView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;


/**
 * Created by Administrator on 2017/1/3.
 * 文章的展示页面
 *
 * @author 李秉龙
 */
public class ArticleContentActivity extends BaseActivity implements View.OnClickListener {
    private String articleID;    //传过来的文章id
    private String imgURL;    //传过来的文章图片
    private String title;    //传过来的文章标题
    private Context context;
    private ObservableWebView web_article;    //内容
    private ArticleInfo articleInfo;
    private ArticleContentHandler handler;
    private StringBuffer htmlBuffer;
    private  ImageView img_article;
    private ImageView img_back;
    private ImageView img_collection;
    private UserInfo userInfo;

    private String article_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_content);
        context = this;
        Intent intent = getIntent();
        articleID = intent.getStringExtra("articleID");
        imgURL = intent.getStringExtra("imgURL");
        title = intent.getStringExtra("title");
        handler = new ArticleContentHandler();
        initView();
        doRequestData();
    }

    private void initView() {
        web_article = (ObservableWebView) this.findViewById(R.id.web_article);
        img_article = (ImageView) this.findViewById(R.id.img_article);
        img_back = (ImageView) this.findViewById(R.id.img_back);
        img_collection = (ImageView) this.findViewById(R.id.img_collection);
        img_back.setOnClickListener(this);
        img_collection.setOnClickListener(this);
        web_article.setOnScrollChangedCallback(new ObservableWebView.OnScrollChangedCallback() {
            @Override
            public void onScroll(int dx, int dy) {
                img_article.layout(img_article.getLeft(),img_article.getTop()-dy,img_article.getRight(),img_article.getBottom()-dy);
            }
        });
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.icon_loading_64px)
                .showImageOnFail(R.mipmap.icon_error_64px)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(imgURL, img_article, options);
    }

    /**
     * 请求网络数据
     */
    private void doRequestData() {
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        StringBuffer buffer = new StringBuffer(ConstantUtils.ZH_MAINFRAME_ADDRESS).append(ConstantUtils.ZH_ARTICLE).append(articleID);
        manager.getAsync(buffer.toString(), new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(context, "加载网路数据失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                Gson gson = new Gson();
                articleInfo = gson.fromJson(result, ArticleInfo.class);
                handler.sendEmptyMessage(ConstantUtils.ARTICLECONTENT_GET_DATA);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.img_collection:
                doUpDataArticle();
                break;
            default:
                break;
        }
    }

    private void doUpDataArticle() {
        userInfo  =(UserInfo) SharePreferencesUtil.readObject(context, ConstantUtils.USER_LOGIN_INFO);
        if (userInfo == null) {
            Toast.makeText(context,"用户未登录",Toast.LENGTH_SHORT).show();
            LoginActivity.actionStart(context);
            return;
        }
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        StringBuffer buffer = new StringBuffer(ConstantUtils.USER_ADDRESS).append(ConstantUtils.ARTICLE_REGISTER);
        Map<String,String> map = new HashMap<>();
        map.put("type","addArticle");
        map.put("number",articleID);
        map.put("a_title",title);
        map.put("a_imgurl",imgURL);


        String url = OkHttpClientManager.attachHttpGetParams(buffer.toString(),map);

        manager.getAsync(url, new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(context,"加载网路数据失败！",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) throws Exception {

                JSONObject object = new JSONObject(result);
                article_ID = object.getString("articleID");
                handler.sendEmptyMessage(ConstantUtils.ARTICLE_UP_DATA);
            }
        });
    }


    class ArticleContentHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantUtils.ARTICLECONTENT_GET_DATA:
                    updateView();
                    break;
                case ConstantUtils.ARTICLE_UP_DATA:
                    doCollArticle();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 收藏
     */
    private void doCollArticle() {
        if(articleID.equals("-1")){
            Toast.makeText(context,"失败了",Toast.LENGTH_SHORT).show();
            return;
        }
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        StringBuffer buffer = new StringBuffer(ConstantUtils.USER_ADDRESS).append(ConstantUtils.COLL_ARTICLE_REGISTER);
        Map<String,String> map = new HashMap<>();
        map.put("type","addCollArticle");
        map.put("articleid",article_ID);
        map.put("userID",userInfo.getUserid()+"");
        String url = OkHttpClientManager.attachHttpGetParams(buffer.toString(),map);
        manager.getAsync(url, new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(context,"加载网路数据失败！",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) throws Exception {

                JSONObject object = new JSONObject(result);
                int cTopicID =Integer.valueOf( object.getString("cTopicID"));
                if (cTopicID>0){
                    Toast.makeText(context,"收藏成功！",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * 更新界面
     */
    private void updateView() {
        String htmlStr="";
        if (articleID!=null){
            htmlBuffer = new StringBuffer("<html><head>");
            for (String css : articleInfo.getCss()){
                htmlBuffer.append("<link type=\"text/css\" rel=\"stylesheet\" href=\"").append(css).append("\">");
            }

            htmlBuffer.append("</head><body>").append(articleInfo.getBody()).append("</body></html>");
            String regex = "<div class\\S+holder\\\">[\\s+]</div>";
            String regex2 = "<div class\\S+holder\\\"></div>";
            htmlStr = htmlBuffer.toString();
            htmlStr = htmlStr.replace(regex, "<img src=\""+articleInfo.getImage()+"\" width=\"100%\" />");
            htmlStr =htmlStr.replace(regex2, "<img src=\""+articleInfo.getImage()+"\" width=\"100%\"/>");
        }
        web_article.loadDataWithBaseURL(null,htmlStr, "text/html", "utf-8", null);
    }


    /**
     * 跳转
     *
     * @param context
     * @param articleID
     */
    public static void actionStart(Context context, String articleID,String imgURL,String title) {
        Intent intent = new Intent(context, ArticleContentActivity.class);
        intent.putExtra("articleID", articleID);
        intent.putExtra("imgURL", imgURL);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }
}
