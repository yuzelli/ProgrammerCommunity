package com.example.buiderdream.programmercommunity.view.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buiderdream.programmercommunity.R;
import com.example.buiderdream.programmercommunity.base.BaseFragment;
import com.example.buiderdream.programmercommunity.constants.ConstantUtils;
import com.example.buiderdream.programmercommunity.entity.UserInfo;
import com.example.buiderdream.programmercommunity.utils.SharePreferencesUtil;
import com.example.buiderdream.programmercommunity.view.activity.PersonCompileActivity;
import com.example.buiderdream.programmercommunity.view.activity.ProtocolActivity;
import com.example.buiderdream.programmercommunity.view.activity.LoginActivity;
import com.example.buiderdream.programmercommunity.widgets.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

/**
 * Created by Administrator on 2016/12/7.
 * 我的Fragment
 *
 * @author 李秉龙
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {
    private View mineFragmentView;

    private RoundImageView img_userHeadMax; //中心大头像
    private RoundImageView img_userHead; //中心大头像
    private TextView tv_personal_nickname;   //用户昵称
    private TextView tv_personal_introduce;   //用户简介
    private Button btn_compile;   //编辑用户
    private Button btn_exit;   //退出用户
    private Button btn_login;   //登录用户
    private ViewPager vp_userCollection;  //用户收藏
    private boolean userLoginFlag = false;  //用户是否登录
    private UserInfo userInfo;
    private Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mineFragmentView == null) {
            context = getActivity();
            mineFragmentView = inflater.inflate(R.layout.fragment_mine, null);
            img_userHeadMax = (RoundImageView) mineFragmentView.findViewById(R.id.img_userHeadMax);
            img_userHead = (RoundImageView) mineFragmentView.findViewById(R.id.img_userHead);
            tv_personal_nickname = (TextView) mineFragmentView.findViewById(R.id.tv_personal_nickname);
            tv_personal_introduce = (TextView) mineFragmentView.findViewById(R.id.tv_personal_introduce);
            img_userHead.setOnClickListener(this);
            btn_compile = (Button) mineFragmentView.findViewById(R.id.btn_compile);
            btn_exit = (Button) mineFragmentView.findViewById(R.id.btn_exit);
            btn_login = (Button) mineFragmentView.findViewById(R.id.btn_login);
            btn_login.setOnClickListener(this);
            btn_compile.setOnClickListener(this);
            btn_exit.setOnClickListener(this);

        }

        return mineFragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
        initViewPager();
    }

    /**
     * 判断用户是否登录，来确定布局
     */
    private void initView() {
        userInfo = (UserInfo) SharePreferencesUtil.readObject(context, ConstantUtils.USER_LOGIN_INFO);
        if (userInfo != null) {
            userLoginFlag = true;
        } else {
            userLoginFlag = false;
        }
        if (userLoginFlag) {
            btn_login.setVisibility(View.GONE);
            btn_exit.setVisibility(View.VISIBLE);
            btn_compile.setVisibility(View.VISIBLE);
            tv_personal_introduce.setVisibility(View.VISIBLE);
            if (userInfo.getU_imageurl()!=null){
                ImageLoader.getInstance().loadImage(userInfo.getU_imageurl(), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        img_userHeadMax.setImageBitmap(bitmap);
                        img_userHead.setImageBitmap(bitmap);

                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });
            }
            tv_personal_nickname.setText(userInfo.getU_name());
            tv_personal_introduce.setText(userInfo.getU_introduction());
        } else {
            btn_exit.setVisibility(View.GONE);
            btn_compile.setVisibility(View.GONE);
            tv_personal_introduce.setVisibility(View.GONE);
            btn_login.setVisibility(View.VISIBLE);
            tv_personal_nickname.setText("用户未登录");
        }
    }

    /**
     * 加载用户收藏的布局
     */
    private void initViewPager() {
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getChildFragmentManager(), FragmentPagerItems.with(context)
                .add("收藏话题", MineCollectTopicFragment.class)
                .add("收藏文章", MineCollectArticleFragment.class)
                .create());
        vp_userCollection = (ViewPager) mineFragmentView.findViewById(R.id.vp_userCollection);
        vp_userCollection.setAdapter(adapter);

        SmartTabLayout vp_mine_tab = (SmartTabLayout) mineFragmentView.findViewById(R.id.vp_mine_tab);
        vp_mine_tab.setViewPager(vp_userCollection);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_compile:
                PersonCompileActivity.actionStart(context);
                break;
            case R.id.btn_exit:
                SharePreferencesUtil.saveObject(context, ConstantUtils.USER_LOGIN_INFO, null);
                initView();
                break;
            case R.id.btn_login:
                LoginActivity.actionStart(context);
                break;
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
    @Override
    public void onDestroy() {
        super.onDestroy();
        ViewGroup parent = (ViewGroup) mineFragmentView.getParent();
        if (parent!=null){
            parent.removeView(mineFragmentView);
        }
    }
}
