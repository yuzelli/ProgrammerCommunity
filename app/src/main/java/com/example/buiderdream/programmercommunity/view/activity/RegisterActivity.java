package com.example.buiderdream.programmercommunity.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buiderdream.programmercommunity.R;
import com.example.buiderdream.programmercommunity.base.BaseActivity;
import com.example.buiderdream.programmercommunity.constants.ConstantUtils;
import com.example.buiderdream.programmercommunity.entity.UserInfo;
import com.example.buiderdream.programmercommunity.https.OkHttpClientManager;
import com.example.buiderdream.programmercommunity.utils.ActivityCollectorUtil;
import com.example.buiderdream.programmercommunity.utils.LoginUtils;
import com.example.buiderdream.programmercommunity.utils.SharePreferencesUtil;
import com.example.buiderdream.programmercommunity.widgets.MyPorter;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_userPhone;   //用户注册手机号
    private EditText et_passWord;   //密码
    private EditText et_certainPassWord;  //确认密码
    private EditText et_verification;    //验证码
    private CheckBox cb_agreedProtocol;  //用户协议
    private Button btn_register;   //注册按钮
    private TextView tv_protocol;   //许可协议
    private Context context;
    private UserInfo userInfo;
    private RegisterHandler handler;
    private boolean agreedProtocol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = this;
        handler = new RegisterHandler();
        initView();
    }

    private void initView() {
        et_userPhone = (EditText) this.findViewById(R.id.et_userPhone);
        et_passWord = (EditText) this.findViewById(R.id.et_passWord);
        et_certainPassWord = (EditText) this.findViewById(R.id.et_certainPassWord);
        et_verification = (EditText) this.findViewById(R.id.et_verification);
        cb_agreedProtocol = (CheckBox) this.findViewById(R.id.cb_agreedProtocol);
        tv_protocol = (TextView) this.findViewById(R.id.tv_protocol);
        btn_register = (Button) this.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
        cb_agreedProtocol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                agreedProtocol = b;
            }
        });
    }

    /**
     * 跳转
     *
     * @param context
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                doRegisterUser();
                break;
            case R.id.tv_protocol:
                break;
            default:
                break;
        }
    }

    private void doRegisterUser() {
        String userPhone = et_userPhone.getText().toString();
        String passWord = et_passWord.getText().toString();
        String certainPassWord = et_certainPassWord.getText().toString();
        String verification = et_verification.getText().toString();
        if(!verificationUserInfo(userPhone, passWord, certainPassWord, verification)){
            return;
        }
        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        StringBuffer buffer = new StringBuffer(ConstantUtils.USER_ADDRESS).append(ConstantUtils.USER_REGISTER);
        Map<String,String> map = new HashMap<>();
        map.put("type","adduser");
        map.put("userName",userPhone);
        map.put("userPhone",userPhone);
        map.put("userPassWord",passWord);
        map.put("imageurl","");
        map.put("userAddress","");
        map.put("userIntroduction","");
        map.put("technology","");

        String url = OkHttpClientManager.attachHttpGetParams(buffer.toString(),map);
        manager.getAsync(url, new OkHttpClientManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(context,"加载网路数据失败！",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                Gson gson = new Gson();
                userInfo = gson.fromJson(result,UserInfo.class);
                handler.sendEmptyMessage(ConstantUtils.REGISTER_GET_DATA);
            }
        });
    }

    /**
     * 验证用户输入
     *
     * @param userPhone
     * @param passWord
     * @param certainPassWord
     * @param verification
     */
    private boolean verificationUserInfo(String userPhone, String passWord, String certainPassWord, String verification) {
        boolean flag = true;
        if (!LoginUtils.isPhoneEnable(userPhone)) {
            Toast.makeText(context, "用户手机号输入有误", Toast.LENGTH_SHORT).show();
            et_userPhone.setText("");
            flag = false;
        }
        if (!passWord.equals(certainPassWord)) {
            Toast.makeText(context, "两次密码不一致", Toast.LENGTH_SHORT).show();
            et_certainPassWord.setText("");
            flag = false;
        }
        if(!MyPorter.verification().equals(verification.toLowerCase())){
            Toast.makeText(context, "验证码错误", Toast.LENGTH_SHORT).show();
            et_verification.setText("");
            flag = false;
        }
        if (!agreedProtocol){
            Toast.makeText(context, "请同意用户协议", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        return flag;
    }

    class RegisterHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ConstantUtils.REGISTER_GET_DATA:
                    SharePreferencesUtil.saveObject(context,ConstantUtils.USER_LOGIN_INFO,userInfo);
                    ActivityCollectorUtil.removeTwoActivity();
                    break;
                default:
                    break;
            }
        }
    }
}
