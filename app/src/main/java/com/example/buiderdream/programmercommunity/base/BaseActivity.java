package com.example.buiderdream.programmercommunity.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
/**
 *Created by Administrator on 2016/12/3.
 * 配置baseActivity
 * @author 李秉龙
 */
import com.example.buiderdream.programmercommunity.R;
import com.example.buiderdream.programmercommunity.utils.ActivityCollectorUtil;

public class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        ActivityCollectorUtil.addActivity(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollectorUtil.removeActivity(this);
    }
}
