package com.example.buiderdream.programmercommunity.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.buiderdream.programmercommunity.R;
import com.example.buiderdream.programmercommunity.base.BaseActivity;
import com.example.buiderdream.programmercommunity.view.fragment.ArticleFragment;
import com.example.buiderdream.programmercommunity.view.fragment.MineFragment;
import com.example.buiderdream.programmercommunity.view.fragment.TopicFragment;


/**
 * Created by Administrator on 2016/12/3.
 * 主页面，实现fragment
 * @author 李秉龙
 */
public class MainActivity extends BaseActivity {
    //定义FragmentTabHost对象
    private FragmentTabHost tabHost;
    //定义一个布局
    private LayoutInflater layoutInflater;

    //定义数组来存放user的Fragment界面
    private Class fragmentArray[] = {TopicFragment.class, ArticleFragment.class, MineFragment.class};
    //定义数组来存放的按钮图片
    private int tabImageViewArray[] = {R.mipmap.icon_theme, R.mipmap.icon_article,
            R.mipmap.icon_mine};
    //Tab选项卡的文字
    private String tabtTextViewArray[] = {"话题", "文章", "我的"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    private void initView() {
        //实例化布局对象
        layoutInflater = LayoutInflater.from(this);
        //实例化TabHost对象，得到TabHost
        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.fl_pageContent);



        //得到fragment的个数
        int count = fragmentArray.length;
        for (int i = 0; i < count; i++) {
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = tabHost.newTabSpec(tabtTextViewArray[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            tabHost.addTab(tabSpec, fragmentArray[i], null);
            //设置Tab按钮的背景
            tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
        }
    }
    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.main_tab_select_view, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.img_tabIcon);
        imageView.setImageResource(tabImageViewArray[index]);
        TextView textView = (TextView) view.findViewById(R.id.tv_tabText);
        textView.setText(tabtTextViewArray[index]);
        return view;
    }
}
