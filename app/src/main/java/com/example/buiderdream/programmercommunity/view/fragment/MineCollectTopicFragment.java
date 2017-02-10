package com.example.buiderdream.programmercommunity.view.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.buiderdream.programmercommunity.R;
import com.example.buiderdream.programmercommunity.base.BaseFragment;
import com.example.buiderdream.programmercommunity.constants.ConstantUtils;
import com.example.buiderdream.programmercommunity.entity.ThemeInfo;
import com.example.buiderdream.programmercommunity.entity.TopicCollection;
import com.example.buiderdream.programmercommunity.entity.UserInfo;
import com.example.buiderdream.programmercommunity.https.OkHttpClientManager;
import com.example.buiderdream.programmercommunity.utils.CommonAdapter;
import com.example.buiderdream.programmercommunity.utils.DateUtils;
import com.example.buiderdream.programmercommunity.utils.GsonUtils;
import com.example.buiderdream.programmercommunity.utils.SharePreferencesUtil;
import com.example.buiderdream.programmercommunity.utils.ViewHolder;
import com.example.buiderdream.programmercommunity.view.activity.ThemeDetailedActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by Administrator on 2017/1/5.
 */

public class MineCollectTopicFragment extends BaseFragment implements AdapterView.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener{
    private View mineCollectTopicFragment;
    private ListView lv_coll_topic;
    private SwipeRefreshLayout sr_refresh; //下拉刷新
    private ArrayList<TopicCollection> topicCollLists;
    private CommonAdapter<TopicCollection> adapter;
    private UserInfo userInfo;
    private Context context;
    private MineCollectTopicFragmentHandler handler;
    private View emptyView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mineCollectTopicFragment==null){
            context =getActivity();
            handler = new MineCollectTopicFragmentHandler();
            mineCollectTopicFragment = inflater.inflate(R.layout.fragment_collect_topic, null);
            lv_coll_topic = (ListView) mineCollectTopicFragment.findViewById(R.id.lv_coll_topic);
            //刷新&加载
            sr_refresh = (SwipeRefreshLayout) mineCollectTopicFragment.findViewById(R.id.sr_refresh);
            sr_refresh.setOnRefreshListener(this);
            sr_refresh.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright),
                    getResources().getColor(android.R.color.holo_green_light),
                    getResources().getColor(android.R.color.holo_orange_light),
                    getResources().getColor(android.R.color.holo_red_light));
            emptyView = mineCollectTopicFragment.findViewById(R.id.emptyView);
        }
        return mineCollectTopicFragment;
    }
    @Override
    public void onResume() {
        super.onResume();
        initListView();
        doCollTopic();
    }

    /**
     * 获取用户收藏数据
     */
    private void doCollTopic() {
        userInfo =(UserInfo) SharePreferencesUtil.readObject(context, ConstantUtils.USER_LOGIN_INFO);
        if (userInfo==null){
            return;
        }
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        StringBuffer buffer = new StringBuffer(ConstantUtils.USER_ADDRESS).append(ConstantUtils.COLL_REGISTER);
        Map<String,String> map = new HashMap<>();
        map.put("type","getUserCollTopic");
        map.put("userid",userInfo.getUserid()+"");
        String url = OkHttpClientManager.attachHttpGetParams(buffer.toString(),map);
        manager.getAsync(url, new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(context,"加载网路数据失败！",Toast.LENGTH_SHORT).show();
                sr_refresh.setRefreshing(false);
            }

            @Override
            public void requestSuccess(String result) throws Exception {

                topicCollLists =  GsonUtils.jsonToArrayList(result,TopicCollection.class);
                handler.sendEmptyMessage(ConstantUtils.TOPIC_COLL_GET_DATA);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ThemeInfo themeInfo = new ThemeInfo();
        TopicCollection topic = topicCollLists.get(i);
        ThemeInfo.NodeBean node = new ThemeInfo.NodeBean();
        ThemeInfo.MemberBean member  = new ThemeInfo.MemberBean();
        member.setUsername(topic.getT_username());
        member.setAvatar_mini(topic.getT_imageurl());
        node.setTitle(topic.getT_title());
        node.setTitle(topic.getTypeString());
        themeInfo.setReplies(topic.getT_replies());
        themeInfo.setCreated(Long.valueOf(topic.getT_createtime()));
        themeInfo.setNode(node);
        themeInfo.setMember(member);
        themeInfo.setContent(topic.getT_content());
        ThemeDetailedActivity.actionStart(context,themeInfo);
    }

    @Override
    public void onRefresh() {
        doCollTopic();
    }


    class MineCollectTopicFragmentHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ConstantUtils.TOPIC_COLL_GET_DATA:
                    sr_refresh.setRefreshing(false);
                    initListView();
                    break;
            }
        }
    }

    /**
     * 加载ListView数据
     */
    private void initListView() {
        if (topicCollLists==null){
            return;
        }
        adapter =new CommonAdapter<TopicCollection>(context,topicCollLists,R.layout.item_topiclist_content) {
            @Override
            public void convert(ViewHolder helper, TopicCollection item) {
                StringBuffer b = new StringBuffer(ConstantUtils.HTTP).append(item.getT_imageurl());
                helper.setImageByUrl(R.id.img_userHead, b.toString());
                helper.setText(R.id.tv_node_title, item.getT_title());
                helper.setText(R.id.tv_userName, item.getT_username());
                helper.setText(R.id.tv_topic_type, ">" + item.getTypeString());
                helper.setText(R.id.tv_node_time, DateUtils.converTime(Long.valueOf(item.getT_createtime())));
                helper.setText(R.id.tv_browseNum, "  " + item.getT_replies() + "  ");
            }
        };
        lv_coll_topic.setAdapter(adapter);
        lv_coll_topic.setOnItemClickListener(this);
        lv_coll_topic.setEmptyView(emptyView);
        lv_coll_topic.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
        StringBuffer buffer = new StringBuffer(ConstantUtils.USER_ADDRESS).append(ConstantUtils.COLL_REGISTER);
        Map<String,String> map = new HashMap<>();
        map.put("type","deleteUserCollTopic");
        map.put("userID",userInfo.getUserid()+"");
        map.put("collTopicID",topicCollLists.get(index).getCollection_topic_id()+"");
        String url = OkHttpClientManager.attachHttpGetParams(buffer.toString(),map);
        manager.getAsync(url, new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(context,"加载网路数据失败！",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) throws Exception {

                topicCollLists =  GsonUtils.jsonToArrayList(result,TopicCollection.class);
                handler.sendEmptyMessage(ConstantUtils.TOPIC_COLL_GET_DATA);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ViewGroup parent = (ViewGroup) mineCollectTopicFragment.getParent();
        if (parent!=null){
            parent.removeView(mineCollectTopicFragment);
        }
    }
}
