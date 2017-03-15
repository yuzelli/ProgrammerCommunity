package com.example.buiderdream.programmercommunity.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.buiderdream.programmercommunity.R;
import com.example.buiderdream.programmercommunity.base.BaseFragment;
import com.example.buiderdream.programmercommunity.constants.ConstantUtils;
import com.example.buiderdream.programmercommunity.entity.TopicTheme;
import com.example.buiderdream.programmercommunity.entity.UserInfo;
import com.example.buiderdream.programmercommunity.utils.SharePreferencesUtil;
import com.example.buiderdream.programmercommunity.view.activity.AddThemeActivity;
import com.example.buiderdream.programmercommunity.view.activity.LoginActivity;
import com.example.buiderdream.programmercommunity.view.activity.PersonCompileActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2016/12/7.
 * 话题Fragment
 * @author 李秉龙
 */

public class TopicFragment extends BaseFragment implements View.OnClickListener,ViewPager.OnPageChangeListener{
    private View topicFragmentView;  //根视图
    private ViewPager vp_topic_viewpager;
    private ImageView img_addTheme;  //添加主题
    private ImageView img_userHead;   //头像
    private FragmentPagerItems.Creator creater;//对节点的动态添加
    private Context context;
    private UserInfo userInfo;
    //默认节点
    private String[] defaultThemeTitle = {"全部","最热","酷工作","问与答","程序员","开源软件","github"} ;
    private String[] defaultThemeURL = {"/api/topics/latest.json","/api/topics/hot.json","/api/topics/show.json?node_id=43","/api/topics/show.json?node_id=12",
            "/api/topics/show.json?node_id=300","/api/topics/show.json?node_id=395","/api/topics/show.json?node_id=772"} ;
//    默认节点列表
    private List<TopicTheme> topicThemeList;
    private static int currentPage = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      if (topicFragmentView==null){
          topicFragmentView = inflater.inflate(R.layout.fragment_topic,container,false);
          vp_topic_viewpager = (ViewPager) topicFragmentView.findViewById(R.id.vp_topic_viewpager);
          img_addTheme = (ImageView) topicFragmentView.findViewById(R.id.img_addTheme);
          img_addTheme.setOnClickListener(this);
          img_userHead = (ImageView) topicFragmentView.findViewById(R.id.img_userHead);
          img_userHead.setOnClickListener(this);
          context = TopicFragment.this.getActivity();
      }
        return topicFragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        addPageView();
         userInfo = (UserInfo) SharePreferencesUtil.readObject(context, ConstantUtils.USER_LOGIN_INFO);
        if (userInfo != null&&userInfo.getU_imageurl()!=null) {
            ImageLoader.getInstance().loadImage(userInfo.getU_imageurl(), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    if (bitmap!=null){
                        img_userHead.setImageBitmap(bitmap);
                    }


                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
        }
    }

    /**
     * 添加ViewPage的方法
     */
    private void addPageView() {

        creater = FragmentPagerItems.with(context);
        //从SharePreferences中读取有那些节点
        topicThemeList = (ArrayList<TopicTheme>) SharePreferencesUtil.readObject(context,"topicThemeSP");

//       如果第一次加载
        if (topicThemeList==null||topicThemeList.size()==0){
            initTopicThemeList();
        }

//        将节点添加到标题中
        for (int i = 0; i < topicThemeList.size(); i++) {
            creater.add(topicThemeList.get(i).getThemeName(), TopicListFragment.class);
        }
        FragmentPagerItemAdapter fragmentadapter = new FragmentPagerItemAdapter(getChildFragmentManager()
                , creater.create());
        vp_topic_viewpager.setAdapter(fragmentadapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) topicFragmentView.findViewById(R.id.viewpager_topicFragment_title);
        viewPagerTab.setViewPager(vp_topic_viewpager);
        viewPagerTab.setOnPageChangeListener(this);
        vp_topic_viewpager.setCurrentItem(currentPage);

    }


    /**
     * 初始化主题列表的方法
     * 默认有七个节点
     */
    private void initTopicThemeList() {
        topicThemeList = new ArrayList<>();
        for (int i = 0;i<defaultThemeTitle.length;i++){
           TopicTheme theme = new TopicTheme(defaultThemeTitle[i],defaultThemeURL[i]);
           topicThemeList.add(theme);
        }
        SharePreferencesUtil.saveObject(context,"topicThemeSP",topicThemeList);
    }
/**
 * 点击回调监听
 * 对添加主题的监听
 */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.img_addTheme:
               AddThemeActivity.actionStart(context);
                break;
            case  R.id.img_userHead:
                if (userInfo!=null) {
                    PersonCompileActivity.actionStart(context);

                    PersonCompileActivity.actionStart(context);
                }else {
                    Toast.makeText(context,"用户未登录！",Toast.LENGTH_SHORT).show();
                    LoginActivity.actionStart(context);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPage = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        ViewGroup parent = (ViewGroup) topicFragmentView.getParent();
        if (parent!=null){
            parent.removeView(topicFragmentView);
        }
    }
}
