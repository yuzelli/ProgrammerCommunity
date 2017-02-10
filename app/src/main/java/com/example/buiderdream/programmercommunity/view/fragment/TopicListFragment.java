package com.example.buiderdream.programmercommunity.view.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ant.liao.GifView;
import com.example.buiderdream.programmercommunity.R;
import com.example.buiderdream.programmercommunity.base.BaseFragment;
import com.example.buiderdream.programmercommunity.constants.ConstantUtils;
import com.example.buiderdream.programmercommunity.entity.ThemeInfo;
import com.example.buiderdream.programmercommunity.entity.TopicTheme;
import com.example.buiderdream.programmercommunity.https.OkHttpClientManager;
import com.example.buiderdream.programmercommunity.utils.CommonAdapter;
import com.example.buiderdream.programmercommunity.utils.DateUtils;
import com.example.buiderdream.programmercommunity.utils.GsonUtils;
import com.example.buiderdream.programmercommunity.utils.SharePreferencesUtil;
import com.example.buiderdream.programmercommunity.utils.ViewHolder;
import com.example.buiderdream.programmercommunity.view.activity.ThemeDetailedActivity;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import okhttp3.Request;

/**
 * Created by Administrator on 2016/12/14.
 * topicList视图
 * @author 李秉龙
 */

public class TopicListFragment extends BaseFragment implements AdapterView.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener {
    private View topiclistFragmentView;
    private SwipeRefreshLayout sr_refresh; //下拉刷新
    private ListView lv_topic_list;   //节点列表
    private List<TopicTheme> topicThemeList;//节点的列表的数据
    private List<ThemeInfo> themeListData;//节点下对应的每个主题的列表
    private int pagePosition;    //当前选择的页面对应节点在topicThemeList的position
    private GifView gif_loading;  //数据加载中
    private CommonAdapter<ThemeInfo> adapter;
    private Context context;
    private TopicHandle handle;
    private View emptyView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (topiclistFragmentView == null) {
            topiclistFragmentView = inflater.inflate(R.layout.fragment_topiclist, null);
            //刷新&加载
            sr_refresh = (SwipeRefreshLayout) topiclistFragmentView.findViewById(R.id.sr_refresh);
            sr_refresh.setOnRefreshListener(this);
            sr_refresh.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright),
                    getResources().getColor(android.R.color.holo_green_light),
                    getResources().getColor(android.R.color.holo_orange_light),
                    getResources().getColor(android.R.color.holo_red_light));

            emptyView = topiclistFragmentView.findViewById(R.id.emptyView);
            lv_topic_list = (ListView) topiclistFragmentView.findViewById(R.id.lv_topic_list);
            gif_loading = (GifView) topiclistFragmentView.findViewById(R.id.gif_loading);
            context = getActivity();
            handle = new TopicHandle(TopicListFragment.this);
//            获得当前page对应的position
            pagePosition = FragmentPagerItem.getPosition(getArguments());
//            获取sharedPreference中的信息
            topicThemeList = (List<TopicTheme>) SharePreferencesUtil.readObject(context, "topicThemeSP");
// 设置Gif图片源
            gif_loading.setGifImage(R.drawable.gif_loading);
// 设置加载方式：先加载后显示、边加载边显示、只显示第一帧再显示
            gif_loading.setGifImageType(GifView.GifImageType.COVER);
            lv_topic_list.setEmptyView(gif_loading);
            doRequestData();
        }
        return topiclistFragmentView;
    }



    private void doRequestData() {
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        StringBuffer buffer = new StringBuffer(ConstantUtils.V2EX_MAINFRAME_ADDRESS).append(topicThemeList.get(pagePosition).getThemeURL());
        manager.getAsync(buffer.toString(), new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(context,"加载网路数据失败！",Toast.LENGTH_SHORT).show();
                gif_loading.setVisibility(View.GONE);
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                themeListData = GsonUtils.jsonToArrayList(result, ThemeInfo.class);
                handle.sendEmptyMessage(ConstantUtils.TOPICLISTFRAGMENT_GET_DATA);
                gif_loading.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ThemeDetailedActivity.actionStart(context, themeListData.get(i));
    }

    @Override
    public void onRefresh() {
       handle.sendEmptyMessage(ConstantUtils.PULL_REFRESH);
    }


    public class TopicHandle extends Handler {

        WeakReference<TopicListFragment> weakReference;

        public TopicHandle(TopicListFragment fragment) {
            weakReference = new WeakReference<TopicListFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantUtils.TOPICLISTFRAGMENT_GET_DATA:
                    initListView();
                    sr_refresh.setRefreshing(false);
                    break;
                case ConstantUtils.PULL_REFRESH:
                    doRequestData();
                    break;
            }
        }
    }

    private void initListView() {
        adapter = new CommonAdapter<ThemeInfo>(context, themeListData, R.layout.item_topiclist_content) {
            @Override
            public void convert(ViewHolder helper, ThemeInfo item) {
                StringBuffer b = new StringBuffer(ConstantUtils.HTTP).append(item.getMember().getAvatar_mini());
                helper.setImageByUrl(R.id.img_userHead, b.toString());
                helper.setText(R.id.tv_node_title, item.getTitle());
                helper.setText(R.id.tv_userName, item.getMember().getUsername());
                helper.setText(R.id.tv_topic_type, ">" + item.getNode().getTitle());
                helper.setText(R.id.tv_node_time, DateUtils.converTime(item.getCreated()));

            }
        };
        lv_topic_list.setAdapter(adapter);
        lv_topic_list.setEmptyView(emptyView);
        lv_topic_list.setOnItemClickListener(this);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        ViewGroup parent = (ViewGroup) topiclistFragmentView.getParent();
        if (parent!=null){
            parent.removeView(topiclistFragmentView);
        }
    }
}
