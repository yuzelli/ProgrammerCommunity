package com.example.buiderdream.programmercommunity.view.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buiderdream.programmercommunity.R;
import com.example.buiderdream.programmercommunity.adapter.BannerAdapter;
import com.example.buiderdream.programmercommunity.base.BaseFragment;
import com.example.buiderdream.programmercommunity.constants.ConstantUtils;
import com.example.buiderdream.programmercommunity.entity.Article;
import com.example.buiderdream.programmercommunity.entity.ArticleInfo;
import com.example.buiderdream.programmercommunity.entity.UserInfo;
import com.example.buiderdream.programmercommunity.https.OkHttpClientManager;
import com.example.buiderdream.programmercommunity.utils.CommonAdapter;
import com.example.buiderdream.programmercommunity.utils.DateUtils;
import com.example.buiderdream.programmercommunity.utils.SharePreferencesUtil;
import com.example.buiderdream.programmercommunity.utils.ViewHolder;
import com.example.buiderdream.programmercommunity.view.activity.ArticleContentActivity;
import com.example.buiderdream.programmercommunity.view.activity.LoginActivity;
import com.example.buiderdream.programmercommunity.view.activity.PersonCompileActivity;
import com.example.buiderdream.programmercommunity.widgets.RoundImageView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Request;

/**
 * Created by Administrator on 2016/12/7.
 * 文章的Fragment
 *
 * @author 李秉龙
 */

public class ArticleFragment extends BaseFragment implements View.OnTouchListener, ViewPager.OnPageChangeListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener,View.OnClickListener {
    private View articleFragmentView;
    private Context context;
    private ArticleHandler handler;
    private RoundImageView img_userHead;
    private SwipeRefreshLayout sr_refresh;  //下拉刷新
    private ListView lv_article;    //文章列表
    private ViewPager vp_picture;   //图片轮播
    private TextView tv_vp_title;   //图片轮播的简介
    private LinearLayout ll_Point;

    private BannerAdapter adapter;   //图片轮播adapter
    private ArrayList<ImageView> bannerImageDates;   //图片轮播的图片
    private View bottomView;
    private int currentIndex = 300;   //图片下标
    private long lastTime;           //上一次图片滚动时间
    private Article article;         //网络数据
    private CommonAdapter<Article.StoriesBean> artAdapter;
    private static int loctionTime = 0;
    private boolean bottomViewClick = true;  //底部加载更多是否可以点击
    private UserInfo userInfo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (articleFragmentView == null) {
            context = getActivity();
            handler = new ArticleHandler();
            articleFragmentView = inflater.inflate(R.layout.fragment_article, null);
            sr_refresh = (SwipeRefreshLayout) articleFragmentView.findViewById(R.id.sr_refresh);
            vp_picture = (ViewPager) articleFragmentView.findViewById(R.id.vp_picture);
            tv_vp_title = (TextView) articleFragmentView.findViewById(R.id.tv_vp_title);
            lv_article = (ListView) articleFragmentView.findViewById(R.id.lv_article);
            ll_Point = (LinearLayout) articleFragmentView.findViewById(R.id.ll_Point);
            sr_refresh.setOnRefreshListener(this);
            img_userHead = (RoundImageView) articleFragmentView.findViewById(R.id.img_userHead);
            img_userHead.setOnClickListener(this);
            sr_refresh.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright),
                    getResources().getColor(android.R.color.holo_green_light),
                    getResources().getColor(android.R.color.holo_orange_light),
                    getResources().getColor(android.R.color.holo_red_light));

            //给listView添加底部视图
            bottomView = getActivity().getLayoutInflater().inflate(R.layout.view_listview_bottom, null);
            TextView tv_more = (TextView) bottomView.findViewById(R.id.tv_more);
            tv_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (bottomViewClick) {
                        doRequestBeforeData();
                        bottomViewClick = false;
                    }
                }
            });
            lv_article.addFooterView(bottomView);
            doRequestData();
        }

        return articleFragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
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
    private void doRequestBeforeData() {
        int a = DateUtils.beforeDate(loctionTime);
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        StringBuffer buffer = new StringBuffer(ConstantUtils.ZH_MAINFRAME_ADDRESS).append(ConstantUtils.ZH_ARTICLE_BEFOR).append(DateUtils.beforeDate(loctionTime));
        manager.getAsync(buffer.toString(), new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(context, "加载网路数据失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                Gson gson = new Gson();
                article.getStories().addAll(gson.fromJson(result, Article.class).getStories());
                handler.sendEmptyMessage(ConstantUtils.ARTICLEFRAGMENT_GET_BEFORE_DATA);
            }
        });
    }

    private void doRequestData() {
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        StringBuffer buffer = new StringBuffer(ConstantUtils.ZH_MAINFRAME_ADDRESS).append(ConstantUtils.ZH_ARTICLE_NEWS);
        manager.getAsync(buffer.toString(), new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(context, "加载网路数据失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                Gson gson = new Gson();
                article = gson.fromJson(result, Article.class);
                handler.sendEmptyMessage(ConstantUtils.ARTICLEFRAGMENT_GET_DATA);
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentIndex = position;
        lastTime = System.currentTimeMillis();
        //设置轮播文字改变
        final int index = position % bannerImageDates.size();
        bannerImageDates.get(index).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArticleContentActivity.actionStart(context, article.getTop_stories().get(index).getId() + "", article.getTop_stories().get(index).getImage(), article.getTop_stories().get(index).getTitle());
            }
        });
        tv_vp_title.setText(article.getTop_stories().get(index).getTitle());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    /**
     * 更新图片轮播
     */
    private void updataBanner() {
        bannerImageDates = new ArrayList<>();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.icon_loading_64px)
                .showImageOnFail(R.mipmap.icon_error_64px)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        for (int i = 0; i < article.getTop_stories().size(); i++) {
            ImageView img = new ImageView(context);
            //显示图片的配置
            ImageLoader.getInstance().displayImage(article.getTop_stories().get(i).getImage(), img, options);
            img.setScaleType(ImageView.ScaleType.FIT_XY);
            bannerImageDates.add(img);
        }
        adapter = new BannerAdapter(context, bannerImageDates);
        vp_picture.setOnTouchListener(this);
        vp_picture.setAdapter(adapter);
        vp_picture.setCurrentItem(300);
        vp_picture.addOnPageChangeListener(this);
        handler.postDelayed(runnableForBanner, 2000);
        addPoint();
        vp_picture.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                monitorPoint(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    // 设置轮播时间间隔
    private Runnable runnableForBanner = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() - lastTime >= 3000) {
                vp_picture.setCurrentItem(currentIndex);
                currentIndex++;
                lastTime = System.currentTimeMillis();
            }
            handler.postDelayed(runnableForBanner, 3000);
        }
    };

    /**
     * 加载listView
     */
    private void updataListView() {
        artAdapter = new CommonAdapter<Article.StoriesBean>(context, article.getStories(), R.layout.item_article_list) {
            @Override
            public void convert(ViewHolder helper, Article.StoriesBean item) {
                helper.setText(R.id.tv_article_title, item.getTitle());
                if (item.getImages().get(0) != null && item.getImages().size() != 0) {
                    helper.setImageByUrl(R.id.img_article, item.getImages().get(0));
                } else {
                    ImageView imgV = helper.getView(R.id.img_article);
                    imgV.setVisibility(View.GONE);
                }
            }
        };
        lv_article.setAdapter(artAdapter);
        lv_article.setOnItemClickListener(this);


        setListViewHeightBasedOnChildren(lv_article, bottomView);
    }

    /**
     * 计算listView的高度
     *
     * @param listView
     * @param bottomView
     */
    public void setListViewHeightBasedOnChildren(ListView listView, View bottomView) {
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
        //底部视图的RootView
        int bottomHeight = bottomView.getMeasuredHeight();
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + bottomHeight;
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 添加小圆点
     */
    private void addPoint() {
        // 1.根据图片多少，添加多少小圆点
       ll_Point.removeAllViews();

        for (int i = 0; i < article.getTop_stories().size(); i++) {
            LinearLayout.LayoutParams pointParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i < 1) {
                pointParams.setMargins(0, 0, 0, 0);
            } else {
                pointParams.setMargins(10, 0, 0, 0);
            }
            ImageView iv = new ImageView(context);
            iv.setLayoutParams(pointParams);
            iv.setBackgroundResource(R.drawable.point_normal);
            ll_Point.addView(iv);
        }
        ll_Point.getChildAt(0).setBackgroundResource(R.drawable.point_select);
    }

    /**
     * 判断小圆点
     *
     * @param position
     */
    private void monitorPoint(int position) {
        int current = (position - 300) % article.getTop_stories().size();
        for (int i = 0; i < article.getTop_stories().size(); i++) {
            if (i == current) {
                ll_Point.getChildAt(current).setBackgroundResource(
                        R.drawable.point_select);
            } else {
                ll_Point.getChildAt(i).setBackgroundResource(
                        R.drawable.point_normal);
            }
        }

    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessage(ConstantUtils.PULL_REFRESH);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ArticleContentActivity.actionStart(context, article.getStories().get(i).getId() + "", article.getStories().get(i).getImages().get(0), article.getStories().get(i).getTitle());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_userHead:
                if (userInfo!=null) {
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

    class ArticleHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantUtils.ARTICLEFRAGMENT_GET_DATA:
                    updataBanner();
                    updataListView();
                    sr_refresh.setRefreshing(false);
                    break;
                case ConstantUtils.ARTICLEFRAGMENT_GET_BEFORE_DATA:
                    artAdapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(lv_article, bottomView);
                    loctionTime++;
                    bottomViewClick = true;
                    break;
                case ConstantUtils.PULL_REFRESH:
                    doRequestData();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ViewGroup parent = (ViewGroup) articleFragmentView.getParent();
        if (parent != null) {
            parent.removeView(articleFragmentView);
        }
    }
}
