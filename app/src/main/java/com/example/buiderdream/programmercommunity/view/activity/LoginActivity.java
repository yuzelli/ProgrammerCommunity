package com.example.buiderdream.programmercommunity.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buiderdream.programmercommunity.R;
import com.example.buiderdream.programmercommunity.base.BaseActivity;
import com.example.buiderdream.programmercommunity.constants.ConstantUtils;
import com.example.buiderdream.programmercommunity.entity.UserInfo;
import com.example.buiderdream.programmercommunity.https.OkHttpClientManager;
import com.example.buiderdream.programmercommunity.utils.LoginUtils;
import com.example.buiderdream.programmercommunity.utils.SharePreferencesUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_userPhone;   //用户账号
    private EditText et_passWord;   //用户密码
    private Button btn_login;      //登录按钮
    private TextView tv_register;   //注册按钮
    private RelativeLayout activity_login;  //根布局
    private Context context;
    private UserInfo userInfo;
    private LoginHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        handler = new LoginHandler();
        initView();
    }

    private void initView() {
        et_userPhone = (EditText) this.findViewById(R.id.et_userPhone);
        et_passWord = (EditText) this.findViewById(R.id.et_passWord);
        btn_login = (Button) this.findViewById(R.id.btn_login);
        tv_register = (TextView) this.findViewById(R.id.tv_register);
        activity_login = (RelativeLayout) this.findViewById(R.id.activity_login);
        btn_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        activity_login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                doLogin();
                break;
            case R.id.tv_register:
                RegisterActivity.actionStart(context);
            default:
                break;
        }

    }

    private void doLogin() {
        String userPhone = et_userPhone.getText().toString();
        String passWord = et_passWord.getText().toString();
        if (!LoginUtils.isPhoneEnable(userPhone)) {
            et_userPhone.setText("");
            return;
        }
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        StringBuffer buffer = new StringBuffer(ConstantUtils.USER_ADDRESS).append(ConstantUtils.USER_REGISTER);
        Map<String, String> map = new HashMap<>();
        map.put("type", "verification");
        map.put("userPhone", userPhone);
        map.put("userPassWord", passWord);
        String url = OkHttpClientManager.attachHttpGetParams(buffer.toString(), map);
        manager.getAsync(url, new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(context, "加载网路数据失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                Gson gson = new Gson();
                JSONObject object = new JSONObject(result);
                String flag = object.getString("result");
                if (flag.equals("true")) {
                    String body = object.getString("userInfo");
                    userInfo = gson.fromJson(body, UserInfo.class);
                    handler.sendEmptyMessage(ConstantUtils.LOGIN_GET_DATA);
                } else {
                    Toast.makeText(context, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * 跳转
     *
     * @param context
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    class LoginHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantUtils.LOGIN_GET_DATA:
                    SharePreferencesUtil.saveObject(context, ConstantUtils.USER_LOGIN_INFO, userInfo);
                    finish();
                    break;
                default:
                    break;
            }
        }
    }
}
