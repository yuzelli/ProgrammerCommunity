package com.example.buiderdream.programmercommunity.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ant.liao.GifView;
import com.example.buiderdream.programmercommunity.R;
import com.example.buiderdream.programmercommunity.base.BaseActivity;
import com.example.buiderdream.programmercommunity.constants.ConstantUtils;
import com.example.buiderdream.programmercommunity.entity.Answer;
import com.example.buiderdream.programmercommunity.entity.ThemeInfo;
import com.example.buiderdream.programmercommunity.entity.UserInfo;
import com.example.buiderdream.programmercommunity.https.OkHttpClientManager;
import com.example.buiderdream.programmercommunity.utils.CommonAdapter;
import com.example.buiderdream.programmercommunity.utils.DateUtils;
import com.example.buiderdream.programmercommunity.utils.GsonUtils;
import com.example.buiderdream.programmercommunity.utils.SharePreferencesUtil;
import com.example.buiderdream.programmercommunity.utils.ViewHolder;
import com.example.buiderdream.programmercommunity.widgets.FitListView;


import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by Administrator on 2016/12/18.
 * 主题的详细页面
 *
 * @author 李秉龙
 */
public class ThemeDetailedActivity extends BaseActivity implements View.OnClickListener {
    private ThemeInfo themeInfo;   //传过来的节点信息

    private ImageView img_back;   //回退
    private ImageView img_userHead;   //用户头像
    private ImageView img_collection;   //收藏
    private TextView tv_userName;  //用户名
    private TextView tv_node_time;  //更新时间
    private TextView tv_browseNum;  //回复数量
    private TextView tv_topic_type;  //节点分类
    private TextView tv_node_title;  //节点标题
    private TextView tv_node_content;  //节点内容
    private TextView tv_title;  //主题分类标题

    private GifView gif_loading;
    private FitListView lv_node_answer; //回复ListView
    private List<Answer> nodeAnswerList;  //回复列表
    private CommonAdapter<Answer> adapter;

    private Context context;
    private ThemeDetailHandler handler;
    private static int floorIndex = 1;
    private static int floorAll = 1;
    private String TopicID = null;
    private UserInfo userInfo;
    private View emptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_detailed);
        context = this;
        handler = new ThemeDetailHandler(ThemeDetailedActivity.this);
        Intent intent = getIntent();
        themeInfo = (ThemeInfo) intent.getSerializableExtra("themeInfo");
        floorAll = themeInfo.getReplies();
        floorIndex = themeInfo.getReplies() - 1;
        initView();
        initListView();
        doRequestData();
    }

    private void initView() {
        img_back = (ImageView) this.findViewById(R.id.img_back);
        emptyView = this.findViewById(R.id.emptyView);
        img_userHead = (ImageView) this.findViewById(R.id.img_userHead);
        tv_userName = (TextView) this.findViewById(R.id.tv_userName);
        tv_node_time = (TextView) this.findViewById(R.id.tv_node_time);
        tv_browseNum = (TextView) this.findViewById(R.id.tv_browseNum);
        tv_topic_type = (TextView) this.findViewById(R.id.tv_topic_type);
        tv_node_title = (TextView) this.findViewById(R.id.tv_node_title);
        tv_node_content = (TextView) this.findViewById(R.id.tv_node_content);
        lv_node_answer = (FitListView) this.findViewById(R.id.lv_node_answer);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        img_collection = (ImageView) this.findViewById(R.id.img_collection);
        img_collection.setOnClickListener(this);
        gif_loading = (GifView) this.findViewById(R.id.gif_loading);
// 设置Gif图片源
        gif_loading.setGifImage(R.drawable.gif_loading);
// 添加监听器
        gif_loading.setOnClickListener(this);
// 设置加载方式：先加载后显示、边加载边显示、只显示第一帧再显示
        gif_loading.setGifImageType(GifView.GifImageType.COVER);
        img_back.setOnClickListener(this);
        tv_title.setText(themeInfo.getNode().getTitle());


        tv_userName.setText(themeInfo.getMember().getUsername());
        tv_node_time.setText(DateUtils.converTime(themeInfo.getCreated()));
        tv_browseNum.setText("  " + themeInfo.getReplies() + "  ");
        tv_topic_type.setText(">" + themeInfo.getNode().getTitle());

        tv_node_title.setText(themeInfo.getTitle());
        tv_node_content.setText(themeInfo.getContent());
        //显示图片的配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.icon_loading_64px)
                .showImageOnFail(R.mipmap.icon_error_64px)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        StringBuffer b = new StringBuffer(ConstantUtils.HTTP).append(themeInfo.getMember().getAvatar_mini());
        ImageLoader.getInstance().displayImage(b.toString(), img_userHead, options);
    }

    private void doRequestData() {
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        StringBuffer buffer = new StringBuffer(ConstantUtils.V2EX_MAINFRAME_ADDRESS).append(ConstantUtils.V2EX_THEME_ANSWER);
        Map<String, String> map = new HashMap<>();
        map.put("topic_id", themeInfo.getId() + "");
        String url = OkHttpClientManager.attachHttpGetParams(buffer.toString(), map);
        manager.getAsync(url, new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(context, "加载网路数据失败！", Toast.LENGTH_SHORT).show();
                gif_loading.setVisibility(View.GONE);
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                gif_loading.setVisibility(View.GONE);
                nodeAnswerList = GsonUtils.jsonToArrayList(result, Answer.class);
                for (int i = 0; i < nodeAnswerList.size(); i++) {
                    nodeAnswerList.get(i).setFloor(i + 1);
                }
                handler.sendEmptyMessage(ConstantUtils.THEMEDETAILED_GET_DATA);
            }
        });
    }

    private void initListView() {
        if (nodeAnswerList == null) {
            nodeAnswerList = new ArrayList<>();
        }
        adapter = new CommonAdapter<Answer>(context, nodeAnswerList, R.layout.item_theme_answer) {
            @Override
            public void convert(ViewHolder helper, Answer item) {
                StringBuffer b = new StringBuffer(ConstantUtils.HTTP).append(item.getMember().getAvatar_mini());
                helper.setImageByUrl(R.id.img_userHead, b.toString());
                helper.setText(R.id.tv_userName, item.getMember().getUsername());
                helper.setText(R.id.tv_releaseDate, DateUtils.converTime(item.getCreated()));
                helper.setText(R.id.tv_floorNum, item.getFloor() + "");
                helper.setText(R.id.tv_answer, item.getContent());
            }
        };
        lv_node_answer.setAdapter(adapter);
        lv_node_answer.setEmptyView(emptyView);
        setListViewHeightBasedOnChildren(lv_node_answer);
    }

    /**
     * 计算listView的高度
     *
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 点击事件处理
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_collection:
                doUpdataTheme();
                break;
            default:
                break;
        }

    }

    /**
     * 上传主题信息
     */
    private void doUpdataTheme() {
        userInfo = (UserInfo) SharePreferencesUtil.readObject(context, ConstantUtils.USER_LOGIN_INFO);
        if (userInfo == null) {
            Toast.makeText(context, "用户未登录", Toast.LENGTH_SHORT).show();
            LoginActivity.actionStart(context);
            return;
        }
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        StringBuffer buffer = new StringBuffer(ConstantUtils.USER_ADDRESS).append(ConstantUtils.TOPIC_REGISTER);
        Map<String, String> map = new HashMap<>();
        map.put("type", "addTopic");
        map.put("number", themeInfo.getId() + "");
        map.put("title", themeInfo.getTitle());
        map.put("t_type", themeInfo.getNode().getTitle());
        map.put("imageurl", themeInfo.getMember().getAvatar_mini());
        map.put("username", themeInfo.getNode().getName());
        map.put("createtime", themeInfo.getCreated() + "");
        map.put("t_replies", themeInfo.getReplies() + "");
        map.put("content", themeInfo.getContent());

        String url = OkHttpClientManager.attachHttpGetParams(buffer.toString(), map);

        manager.getAsync(url, new OkHttpClientManager.DataCallBack() {


            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(context, "加载网路数据失败！", Toast.LENGTH_SHORT).show();
                gif_loading.setVisibility(View.GONE);
            }

            @Override
            public void requestSuccess(String result) throws Exception {

                JSONObject object = new JSONObject(result);
                TopicID = object.getString("topicID");
                handler.sendEmptyMessage(ConstantUtils.THEME_UP_DATA);
                gif_loading.setVisibility(View.GONE);
            }
        });

    }

    /**
     * 跳转
     *
     * @param context
     * @param themeInfo
     */
    public static void actionStart(Context context, ThemeInfo themeInfo) {
        Intent intent = new Intent(context, ThemeDetailedActivity.class);
        intent.putExtra("themeInfo", themeInfo);
        context.startActivity(intent);
    }

    class ThemeDetailHandler extends Handler {
        WeakReference<ThemeDetailedActivity> weakReference;

        public ThemeDetailHandler(ThemeDetailedActivity fragment) {
            weakReference = new WeakReference<ThemeDetailedActivity>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantUtils.THEMEDETAILED_GET_DATA:
                    initListView();
                    break;
                case ConstantUtils.THEME_UP_DATA:
                    doCollectionTheme();
                    break;
            }
        }
    }

    /**
     * 为对应的用户收藏文章
     */
    private void doCollectionTheme() {
        if (TopicID.equals("-1")) {
            Toast.makeText(context, "失败了", Toast.LENGTH_SHORT).show();
            return;
        }
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        StringBuffer buffer = new StringBuffer(ConstantUtils.USER_ADDRESS).append(ConstantUtils.COLL_REGISTER);
        Map<String, String> map = new HashMap<>();
        map.put("type", "addCollTopic");
        map.put("topicID", TopicID);
        map.put("userID", userInfo.getUserid() + "");

        String url = OkHttpClientManager.attachHttpGetParams(buffer.toString(), map);

        manager.getAsync(url, new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(context, "加载网路数据失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) throws Exception {

                JSONObject object = new JSONObject(result);
                int cTopicID = Integer.valueOf(object.getString("cTopicID"));
                if (cTopicID > 0) {
                    Toast.makeText(context, "收藏成功！", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
