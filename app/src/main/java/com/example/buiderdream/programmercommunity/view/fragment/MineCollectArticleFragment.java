package com.example.buiderdream.programmercommunity.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.buiderdream.programmercommunity.R;
import com.example.buiderdream.programmercommunity.base.BaseFragment;
import com.example.buiderdream.programmercommunity.constants.ConstantUtils;
import com.example.buiderdream.programmercommunity.entity.ArticleCollection;
import com.example.buiderdream.programmercommunity.entity.ThemeInfo;
import com.example.buiderdream.programmercommunity.entity.TopicCollection;
import com.example.buiderdream.programmercommunity.entity.UserInfo;
import com.example.buiderdream.programmercommunity.https.OkHttpClientManager;
import com.example.buiderdream.programmercommunity.utils.CommonAdapter;
import com.example.buiderdream.programmercommunity.utils.DateUtils;
import com.example.buiderdream.programmercommunity.utils.GsonUtils;
import com.example.buiderdream.programmercommunity.utils.SharePreferencesUtil;
import com.example.buiderdream.programmercommunity.utils.ViewHolder;
import com.example.buiderdream.programmercommunity.view.activity.AddThemeActivity;
import com.example.buiderdream.programmercommunity.view.activity.ArticleContentActivity;
import com.example.buiderdream.programmercommunity.view.activity.ThemeDetailedActivity;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by Administrator on 2017/1/5.
 */

public class MineCollectArticleFragment extends BaseFragment implements AdapterView.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener{
    private View mineCollectArticleFragment;
    private ListView lv_article;
    private SwipeRefreshLayout sr_refresh;
    private ArrayList<ArticleCollection> articleCollLists;
    private CommonAdapter<ArticleCollection> adapter;
    private UserInfo userInfo;
    private Context context;
    private MineCollectArticleFragmentHandler handler;
    private View emptyView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mineCollectArticleFragment==null){
            context = getActivity();
            handler = new MineCollectArticleFragmentHandler();
            mineCollectArticleFragment = inflater.inflate(R.layout.fragment_collect_article, null);
            lv_article = (ListView) mineCollectArticleFragment.findViewById(R.id.lv_article);
            //刷新&加载
            sr_refresh = (SwipeRefreshLayout) mineCollectArticleFragment.findViewById(R.id.sr_refresh);
            sr_refresh.setOnRefreshListener(this);
            sr_refresh.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright),
                    getResources().getColor(android.R.color.holo_green_light),
                    getResources().getColor(android.R.color.holo_orange_light),
                    getResources().getColor(android.R.color.holo_red_light));
            emptyView = mineCollectArticleFragment.findViewById(R.id.emptyView);

        }
        return mineCollectArticleFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        initListView();
        doCollArticle();
    }
    /**
     * 获取用户收藏数据
     */
    private void doCollArticle() {
        userInfo =(UserInfo) SharePreferencesUtil.readObject(context, ConstantUtils.USER_LOGIN_INFO);
        if (userInfo==null){
            return;
        }
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        StringBuffer buffer = new StringBuffer(ConstantUtils.USER_ADDRESS).append(ConstantUtils.COLL_ARTICLE_REGISTER);
        Map<String,String> map = new HashMap<>();
        map.put("type","getUserCollArticle");
        map.put("userid",userInfo.getUserid()+"");
        String url = OkHttpClientManager.attachHttpGetParams(buffer.toString(),map);
        manager.getAsync(url, new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(context,"加载网路数据失败！",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) throws Exception {

                articleCollLists =  GsonUtils.jsonToArrayList(result,ArticleCollection.class);

                handler.sendEmptyMessage(ConstantUtils.ARTICLE_COLL_GET_DATA);
            }
        });
    }
    

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ArticleContentActivity.actionStart(context,articleCollLists.get(i).getZ_number()+"",articleCollLists.get(i).getA_imgurl(),articleCollLists.get(i).getA_title());

    }

    @Override
    public void onRefresh() {
        doCollArticle();
    }


    /**
     * 加载ListView数据
     */
    private void initListView() {
        if (articleCollLists==null){
            return;
        }
        adapter =new CommonAdapter<ArticleCollection>(context,articleCollLists,R.layout.item_article_list) {
            @Override
            public void convert(ViewHolder helper, ArticleCollection item) {
                helper.setText(R.id.tv_article_title,item.getA_title());
                helper.setImageByUrl(R.id.img_article,item.getA_imgurl());
            }
        };
        lv_article.setAdapter(adapter);
        lv_article.setOnItemClickListener(this);
        lv_article.setEmptyView(emptyView);
        lv_article.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
               ShowDiaLog(i);
                return true;
            }
        });

    }

    /**
     * show删除用户收藏dialog
     * @param index
     */
    private void ShowDiaLog(final int index) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("提示框");
        builder.setMessage("是否删除？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                doDeleteUserColl(index);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    /**
     * 删除用户收藏
     */
    private void doDeleteUserColl(int index) {
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        StringBuffer buffer = new StringBuffer(ConstantUtils.USER_ADDRESS).append(ConstantUtils.COLL_ARTICLE_REGISTER);
        Map<String,String> map = new HashMap<>();
        map.put("type","deleteUserCollArticle");
        map.put("userID",userInfo.getUserid()+"");
        map.put("collAricleID",articleCollLists.get(index).getCollection_art_id()+"");
        String url = OkHttpClientManager.attachHttpGetParams(buffer.toString(),map);
        manager.getAsync(url, new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(context,"加载网路数据失败！",Toast.LENGTH_SHORT).show();
                sr_refresh.setRefreshing(false);
            }

            @Override
            public void requestSuccess(String result) throws Exception {

                articleCollLists =  GsonUtils.jsonToArrayList(result,ArticleCollection.class);
                handler.sendEmptyMessage(ConstantUtils.ARTICLE_COLL_GET_DATA);
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        ViewGroup parent = (ViewGroup) mineCollectArticleFragment.getParent();
        if (parent!=null){
            parent.removeView(mineCollectArticleFragment);
        }
    }

    class MineCollectArticleFragmentHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ConstantUtils.ARTICLE_COLL_GET_DATA:

                    sr_refresh.setRefreshing(false);
                    initListView();
                    break;
            }
        }
    }
}
