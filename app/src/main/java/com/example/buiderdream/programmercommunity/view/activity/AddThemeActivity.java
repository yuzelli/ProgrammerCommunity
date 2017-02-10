package com.example.buiderdream.programmercommunity.view.activity;

import android.animation.LayoutTransition;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buiderdream.programmercommunity.R;
import com.example.buiderdream.programmercommunity.base.BaseActivity;
import com.example.buiderdream.programmercommunity.entity.ThemeInfo;
import com.example.buiderdream.programmercommunity.entity.TopicTheme;
import com.example.buiderdream.programmercommunity.utils.CommonAdapter;
import com.example.buiderdream.programmercommunity.utils.DensityUtils;
import com.example.buiderdream.programmercommunity.utils.SharePreferencesUtil;
import com.example.buiderdream.programmercommunity.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import static android.graphics.Color.GRAY;

/**
 * Created by Administrator on 2016/12/18.
 * 添加主题页面
 *
 * @author 李秉龙
 */
public class AddThemeActivity extends BaseActivity implements AdapterView.OnItemClickListener {
   private ImageView img_back;   //后退
    private GridView gv_defaultTheme;    //默认主题
    private GridLayout gv_addTheme;   //用户新增的主题
    private GridView gv_developLanguage;   //开发语言
    private GridView gv_technology;     //技术前沿
    private ImageView img_saveNewTheme;   //保存用户选择
    private LayoutTransition mTransition;  //动画
    private Context context;

    //默认主题
    private String[] defaultThemeTitle = {"最新", "最热", "酷工作", "问与答", "程序员", "开源软件", "github"};
    private String[] defaultThemeURL = {"/api/topics/latest.json", "/api/topics/hot.json", "/api/topics/show.json?node_id=43", "/api/topics/show.json?node_id=12",
            "/api/topics/show.json?node_id=300", "/api/topics/show.json?node_id=395", "/api/topics/show.json?node_id=772"};
    //    默认节点列表
    private List<TopicTheme> topicThemeList;
    private CommonAdapter<TopicTheme> defaultThemeAdapter;
    //开发语言
    private String[] developLanguageTheme = {"html", "mysql", "php", "java", "python", "css", "javascript", "jquery"};
    private String[] developLanguageURL = {"/api/topics/show.json?node_id=60", "/api/topics/show.json?node_id=61",
            "/api/topics/show.json?node_id=62", "/api/topics/show.json?node_id=63", "/api/topics/show.json?node_id=90"
            , "/api/topics/show.json?node_id=139", "/api/topics/show.json?node_id=146", "/api/topics/show.json?node_id=425"};
    //    开发语言列表
    private List<TopicTheme> developLanguageList;
    private CommonAdapter<TopicTheme> developLanguageAdapter;
    //技术前沿
    private String[] technologyTheme = {"搜索引擎", "信息安全", "编程框架", "前端开发", "移动开发", "虚拟现实", "云计算", "项目管理"};
    private String[] technologyURL = {"/api/topics/show.json?node_id=109", "/api/topics/show.json?node_id=206",
            "/api/topics/show.json?node_id=416", "/api/topics/show.json?node_id=791", "/api/topics/show.json?node_id=847"
            , "/api/topics/show.json?node_id=906", "/api/topics/show.json?node_id=104", "/api/topics/show.json?node_id=739"};
    //    技术前沿列表
    private List<TopicTheme> technologyList;
    private CommonAdapter<TopicTheme> technologyAdapter;

    //    新增列表
    private List<TopicTheme> addThemeList;
    private  boolean initViewFlag = true;  //是否初始化布局
    private boolean updateTheme = false; //是否更改了主题
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_theme);
        initView();
    }

    private void initView() {
        gv_defaultTheme = (GridView) this.findViewById(R.id.gv_defaultTheme);
        gv_addTheme = (GridLayout) this.findViewById(R.id.gv_addTheme);
        gv_developLanguage = (GridView) this.findViewById(R.id.gv_developLanguage);
        gv_technology = (GridView) this.findViewById(R.id.gv_technology);
        img_saveNewTheme = (ImageView) this.findViewById(R.id.img_saveNewTheme);
        img_back = (ImageView) this.findViewById(R.id.img_back);
        context = this;
        initAddThemeData();
        initData();
        initGridView();
        initViewFlag = false;

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (updateTheme){
                    notifyUserUpdate();
                }else {
                    finish();
                }
            }
        });
    }

    /**
     * 提示用户主题改变
     */
    private void notifyUserUpdate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示框");
        builder.setMessage("检测到用户更改，是否保存修改？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveUserTheme();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.show();
    }

    /**
     * 初始化用户自定义主题
     */
    private void initAddThemeData() {
        //添加动画
        //默认动画全部开启
        mTransition = new LayoutTransition();
        mTransition.setAnimator(LayoutTransition.APPEARING,mTransition
                .getAnimator(LayoutTransition.APPEARING));
        mTransition
                .setAnimator(LayoutTransition.CHANGE_APPEARING,
                        mTransition.getAnimator(LayoutTransition.CHANGE_APPEARING));
        mTransition.setAnimator(LayoutTransition.DISAPPEARING,mTransition
                .getAnimator(LayoutTransition.DISAPPEARING));
        mTransition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, mTransition
                .getAnimator(LayoutTransition.CHANGE_DISAPPEARING));
        gv_addTheme.setLayoutTransition(mTransition);
        gv_addTheme.setColumnCount(4);

        img_saveNewTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserTheme();
            }
        });

        addThemeList = (ArrayList<TopicTheme>) SharePreferencesUtil.readObject(context,"userSelectThemeSP");
        if (addThemeList==null){
            addThemeList = new ArrayList<>();
        }
        for (TopicTheme theme :addThemeList){
            addItemForAddTheme(theme);
        }

    }

    /**
     * 保存用户选择
     */
    private void saveUserTheme() {
        SharePreferencesUtil.saveObject(context,"userSelectThemeSP",addThemeList);
        ArrayList<TopicTheme> list = new ArrayList<TopicTheme>();
        list.addAll(topicThemeList);
        list.addAll(addThemeList);
        SharePreferencesUtil.saveObject(context,"topicThemeSP",list);
        Toast.makeText(context,"已保存用户选择",Toast.LENGTH_SHORT).show();
        finish();
    }


    /**
     * 初始化gradView的数据
     */
    private void initData() {
//        初始化默认节点数据
        topicThemeList = new ArrayList<>();
        for (int i = 0; i < defaultThemeTitle.length; i++) {
            TopicTheme theme = new TopicTheme(defaultThemeTitle[i], defaultThemeURL[i]);
            topicThemeList.add(theme);
        }
//        初始化开发语言节点数据
        developLanguageList = new ArrayList<>();
        for (int i = 0; i < developLanguageTheme.length; i++) {
            TopicTheme theme = new TopicTheme(developLanguageTheme[i], developLanguageURL[i]);
            developLanguageList.add(theme);
        }
//        初始化技术前沿节点数据
        technologyList = new ArrayList<>();
        for (int i = 0; i < technologyTheme.length; i++) {
            TopicTheme theme = new TopicTheme(technologyTheme[i], technologyURL[i]);
            technologyList.add(theme);
        }
        for (TopicTheme add : addThemeList){
            for (TopicTheme lang :developLanguageList){
                if (add.getThemeName().equals(lang.getThemeName())){
                    lang.setSelected(true);
                }
            }
            for (TopicTheme tech :technologyList){
                if (add.getThemeName().equals(tech.getThemeName())){
                    tech.setSelected(true);
                }
            }
        }

    }

    /**
     * 加载GridView的adapter
     */
    private void initGridView() {
        defaultThemeAdapter = new CommonAdapter<TopicTheme>(this, topicThemeList, R.layout.item_addtheme_tab) {
            @Override
            public void convert(ViewHolder helper, TopicTheme item) {
                TextView tv = helper.getView(R.id.tv_nodeName);
                tv.setText(item.getThemeName());
                tv.setBackgroundColor(ContextCompat.getColor(context,R.color.gainsboro));
                tv.setTextColor(Color.GRAY);
            }
        };
        gv_defaultTheme.setAdapter(defaultThemeAdapter);
        developLanguageAdapter = new CommonAdapter<TopicTheme>(this, developLanguageList, R.layout.item_addtheme_tab) {
            @Override
            public void convert(ViewHolder helper, TopicTheme item) {
                TextView tv = helper.getView(R.id.tv_nodeName);
                tv.setText(item.getThemeName());
                if (item.isSelected()){
                    tv.setBackgroundColor(ContextCompat.getColor(context,R.color.gainsboro));
                    tv.setTextColor(Color.GRAY);
                }else {
                    tv.setBackground(ContextCompat.getDrawable(context,R.drawable.bg_addtheme_tv_border));
                    tv.setTextColor(Color.BLACK);
                }
            }
        };
        gv_developLanguage.setAdapter(developLanguageAdapter);
        gv_developLanguage.setOnItemClickListener(this);
        technologyAdapter = new CommonAdapter<TopicTheme>(this, technologyList, R.layout.item_addtheme_tab) {
            @Override
            public void convert(ViewHolder helper, TopicTheme item) {
                TextView tv = helper.getView(R.id.tv_nodeName);
                tv.setText(item.getThemeName());
                if (item.isSelected()){
                    tv.setBackgroundColor(ContextCompat.getColor(context,R.color.gainsboro));
                    tv.setTextColor(Color.GRAY);
                }else {
                    tv.setBackground(ContextCompat.getDrawable(context,R.drawable.bg_addtheme_tv_border));
                    tv.setTextColor(Color.BLACK);
                }
            }
        };
        gv_technology.setAdapter(technologyAdapter);
        gv_technology.setOnItemClickListener(this);
    }


    public static void actionStart(Context context) {
        Intent intent = new Intent(context, AddThemeActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView tv = (TextView) view.findViewById(R.id.tv_nodeName);
        tv.setBackgroundColor(ContextCompat.getColor(context,R.color.gainsboro));
        tv.setTextColor(Color.GRAY);
        updateTheme = true;
        switch (adapterView.getId()) {
            case R.id.gv_developLanguage:
                addItemForAddTheme(developLanguageList.get(i));
                break;
            case R.id.gv_technology:
                addItemForAddTheme(technologyList.get(i));
                break;
        }
    }

    /**
     * 向gv_addTheme中添加item
     * @param topicTheme
     */
    private void addItemForAddTheme(final TopicTheme topicTheme) {

        //是否是初始化布局
        if (!initViewFlag){
            //是否已经添加
            if (topicTheme.isSelected()){
                Toast.makeText(context,"已经添加了",Toast.LENGTH_SHORT).show();
                return;
            }
            topicTheme.setSelected(true);
            addThemeList.add(topicTheme);
        }
//        添加新的主题
        final TextView item = new TextView(this);
        final RelativeLayout relativeLayout = new RelativeLayout(this);

        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams r_layoutParams = new RelativeLayout.LayoutParams(width/4, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(DensityUtils.dp2px(context,2),DensityUtils.dp2px(context,5),DensityUtils.dp2px(context,2),DensityUtils.dp2px(context,5));
        item.setLayoutParams(layoutParams);
        item.setGravity(Gravity.CENTER);
        item.setTextSize(18);
        item.setText(topicTheme.getThemeName());
        item.setBackground(ContextCompat.getDrawable(this,R.drawable.bg_addtheme_tv_border));
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //移除主题
                gv_addTheme.removeView(relativeLayout);
                topicTheme.setSelected(false);
                addThemeList.remove(topicTheme);
                for (TopicTheme theme:technologyList){
                    if (theme.getThemeName().equals(topicTheme.getThemeName())){
                        theme.setSelected(false);
                    }
                }
                for (TopicTheme theme:developLanguageList){
                    if (theme.getThemeName().equals(topicTheme.getThemeName())){
                        theme.setSelected(false);
                    }
                }
                developLanguageAdapter.notifyDataSetChanged();
                technologyAdapter.notifyDataSetChanged();
            }
        });
        relativeLayout.setLayoutParams(r_layoutParams);
        relativeLayout.addView(item);
        gv_addTheme.addView(relativeLayout, Math.min(0, gv_addTheme.getChildCount()));
    };
}
