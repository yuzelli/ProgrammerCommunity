package com.example.buiderdream.programmercommunity.view.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buiderdream.programmercommunity.R;
import com.example.buiderdream.programmercommunity.base.BaseActivity;
import com.example.buiderdream.programmercommunity.constants.ConstantUtils;
import com.example.buiderdream.programmercommunity.entity.UserInfo;
import com.example.buiderdream.programmercommunity.https.OkHttpClientManager;
import com.example.buiderdream.programmercommunity.utils.ImageUtils;
import com.example.buiderdream.programmercommunity.utils.LxQiniuUploadUtils;
import com.example.buiderdream.programmercommunity.utils.SharePreferencesUtil;
import com.example.buiderdream.programmercommunity.widgets.CityPicker;
import com.example.buiderdream.programmercommunity.widgets.RoundImageView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import com.example.buiderdream.programmercommunity.constants.ConstantUtils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.utils.UrlSafeBase64;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.Request;

public class PersonCompileActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rl_head;         //头像
    private RelativeLayout rl_userName;    //用户名
    private RelativeLayout rl_phoneNum;    //注册电话
    private RelativeLayout rl_address;     //用户地址
    private RelativeLayout rl_technology;   //热衷技术
    private RelativeLayout rl_introduce;    //个人介绍
    private Button btn_finish;             //完成
    private ImageView rw_head;             //头像
    private TextView tv_userName;           //用户名
    private TextView tv_phoneNum;           //注册电话
    private TextView tv_address;           //用户地址
    private TextView tv_technology;           //热衷技术
    private TextView tv_introduce;           //个人介绍
    private ImageView img_back;

    private Context context;
    private UserInfo userInfo;
    private PersonCompileHandler handler;

    /**
     * 定义三种状态
     */
    private static final int HEAD_PORTRAIT_PIC = 1;//相册
    private static final int HEAD_PORTRAIT_CAM = 2;//相机
    private static final int HEAD_PORTRAIT_CUT = 3;//图片裁剪
    private File photoFile;
    private Bitmap photoBitmap;

    private PopupWindow mPopWindow;
    private CityPicker cityPicker;
    private String addressCur;
    private String userHeadImgUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_compile);
        context = this;
        handler = new PersonCompileHandler();
        initView();
        initData();
    }

    private void initView() {
        rl_head = (RelativeLayout) this.findViewById(R.id.rl_head);
        rl_userName = (RelativeLayout) this.findViewById(R.id.rl_userName);
        rl_phoneNum = (RelativeLayout) this.findViewById(R.id.rl_phoneNum);
        rl_address = (RelativeLayout) this.findViewById(R.id.rl_address);
        rl_technology = (RelativeLayout) this.findViewById(R.id.rl_technology);
        rl_introduce = (RelativeLayout) this.findViewById(R.id.rl_introduce);
        btn_finish = (Button) this.findViewById(R.id.btn_finish);
        rw_head = (ImageView) this.findViewById(R.id.rw_head);
        tv_userName = (TextView) this.findViewById(R.id.tv_userName);
        tv_phoneNum = (TextView) this.findViewById(R.id.tv_phoneNum);
        tv_address = (TextView) this.findViewById(R.id.tv_address);
        tv_technology = (TextView) this.findViewById(R.id.tv_technology);
        tv_introduce = (TextView) this.findViewById(R.id.tv_introduce);
        img_back = (ImageView) this.findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        rl_head.setOnClickListener(this);
        rl_userName.setOnClickListener(this);
        rl_phoneNum.setOnClickListener(this);
        rl_address.setOnClickListener(this);
        rl_technology.setOnClickListener(this);
        rl_introduce.setOnClickListener(this);
        btn_finish.setOnClickListener(this);
    }

    //    用户加载数据
    private void initData() {
        userInfo = (UserInfo) SharePreferencesUtil.readObject(context, ConstantUtils.USER_LOGIN_INFO);
        if (userInfo == null) {
            return;
        }

        if (userInfo.getU_imageurl()!=null){
            userHeadImgUrl = userInfo.getU_imageurl();
            ImageLoader.getInstance().loadImage(userInfo.getU_imageurl(), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    rw_head.setImageBitmap(bitmap);

                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
        }
        tv_userName.setText(userInfo.getU_name());
        tv_phoneNum.setText(userInfo.getU_phone());
        tv_address.setText(userInfo.getU_address());
        tv_technology.setText(userInfo.getU_technology());
        tv_introduce.setText(userInfo.getU_introduction());
    }


    //显示Dialog选择拍照还是从相册选择
    private void showPhotoDialog() {
        final Dialog dialog = new Dialog(this, R.style.PhotoDialog);
        final View view = LayoutInflater.from(PersonCompileActivity.this).inflate(R.layout.diallog_personal_head_select, null);
        dialog.setContentView(view);
        TextView tv_PhotoGraph = (TextView) view.findViewById(R.id.tv_personal_photo_graph);
        TextView tv_PhotoAlbum = (TextView) view.findViewById(R.id.tv_personal_photo_album);
        TextView tv_Cancel = (TextView) view.findViewById(R.id.tv_cancel);

        tv_PhotoGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhotoGraph();
            }
        });

        tv_PhotoAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhotoAlbum();
            }
        });

        tv_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //设置出现Dialog位置
        Window window = dialog.getWindow();
        // 可以在此设置显示动画
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        dialog.show();
    }

    //打开相册方法
    private void openPhotoAlbum() {
        Intent picIntent = new Intent(Intent.ACTION_PICK, null);
        picIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(picIntent, HEAD_PORTRAIT_PIC);
    }

    //打开相机方法
    private void openPhotoGraph() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!file.exists()) {
                file.mkdirs();
            }
            photoFile = new File(file, System.currentTimeMillis() + "");

            Uri photoUri = Uri.fromFile(photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(intent, HEAD_PORTRAIT_CAM);
        } else {

            Toast.makeText(this, "请确认已经插入SD卡", Toast.LENGTH_SHORT).show();
        }
    }

    //回调函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case HEAD_PORTRAIT_CAM:
                    startPhotoZoom(Uri.fromFile(photoFile));
                    break;
                case HEAD_PORTRAIT_PIC:
                    if (data == null || data.getData() == null) {
                        return;
                    }
                    startPhotoZoom(data.getData());
                    break;
                case HEAD_PORTRAIT_CUT:
                    if (data != null) {
                        photoBitmap = data.getParcelableExtra("data");
                        rw_head.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        rw_head.setImageBitmap(photoBitmap);
                        try {
                            File SDCardRoot = Environment.getExternalStorageDirectory();
                            if (ImageUtils.saveBitmap2file(photoBitmap)) {

                                String photoPath = SDCardRoot + ConstantUtils.AVATAR_FILE_PATH;
                                //doUploadPicture(photoPath);
                                final String StouserHeadImgName =  userInfo.getU_phone() +"_"+ System.currentTimeMillis();

                                LxQiniuUploadUtils.uploadPic("yuzelloroom", photoPath,StouserHeadImgName, new LxQiniuUploadUtils.UploadCallBack() {
                                    @Override
                                    public void sucess(String url) {
                                        Toast.makeText(context, url, Toast.LENGTH_SHORT).show();
                                        userHeadImgUrl = ConstantUtils.QN_IMG_ADDRESS+StouserHeadImgName;
                                    }

                                    @Override
                                    public void fail(String key, ResponseInfo info) {
                                        Toast.makeText(context, "shibeile", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    break;
            }
        }
        if (requestCode == ConstantUtils.PERSON_COMPILE_REQUESTCODE && resultCode == ConstantUtils.PERSON_COMPILE_RESULTCODE) {
            String title = data.getStringExtra("title");
            if (title.equals("用户名：")) {
                tv_userName.setText(data.getStringExtra("result"));
            }
            if (title.equals("个人简介：")) {
                tv_introduce.setText(data.getStringExtra("result"));
            }
            if(title.equals("热衷技术：")){
                tv_technology.setText(data.getStringExtra("result"));
            }

        }
    }

    /**
     * 打开系统图片裁剪功能
     *
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true); //黑边
        intent.putExtra("scaleUpIfNeeded", true); //黑边
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, HEAD_PORTRAIT_CUT);
    }


    /**
     * 地区选择popupwindoew
     */
    private void showPopupWindow() {
        View contentView = LayoutInflater.from(PersonCompileActivity.this).inflate(R.layout.show_popup_select_address, null);
        mPopWindow = new PopupWindow(contentView);
        mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setContentView(contentView);//设置包含视图

        View rootview = LayoutInflater.from(PersonCompileActivity.this).inflate(R.layout.activity_person_compile, null);

        TextView btn_cancel = (TextView) contentView.findViewById(R.id.btn_cancel);
        TextView btn_addFinish = (TextView) contentView.findViewById(R.id.btn_addFinish);
        btn_cancel.setOnClickListener(this);
        btn_addFinish.setOnClickListener(this);
        cityPicker = (CityPicker) contentView.findViewById(R.id.citypicker);
        cityPicker.setOnSelectingListener(new CityPicker.OnSelectingListener() {

            @Override
            public void selected(boolean selected) {
                // TODO Auto-generated method stub
                addressCur = cityPicker.getCity_string();
            }
        });
        // 控制popupwindow点击屏幕其他地方消失
        mPopWindow.setBackgroundDrawable(this.getResources().getDrawable(
                R.drawable.bg_popupwindow));// 设置背景图片，不能在布局中设置，要通过代码来设置
        mPopWindow.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上
        mPopWindow.setAnimationStyle(R.style.contextPopupAnim);//设置动画
        mPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);//设置模式，和Activity的一样，覆盖，调整大小。
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_head:
                showPhotoDialog();
                break;
            case R.id.rl_userName:
                setEdit("用户名：");
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.rl_phoneNum:
                Toast.makeText(context, "用户注册手机号不能更改！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_address:
                showPopupWindow();
                break;
            case R.id.rl_technology:
                setEdit("热衷技术：");
                break;
            case R.id.rl_introduce:
                setEdit("个人简介：");
                break;
            case R.id.btn_finish:
                doRequstUserData();

                break;
            case R.id.btn_cancel:
                mPopWindow.dismiss();
                break;
            case R.id.btn_addFinish:
                tv_address.setText(addressCur);
                break;
            default:
                break;
        }
    }

    /**
     * 修改
     */
    private void doRequstUserData() {
        String userName = tv_userName.getText().toString();
        String userPhone = tv_phoneNum.getText().toString();
        String userAddress = tv_address.getText().toString();
        String userIntroduce = tv_introduce.getText().toString();
        String userTechnology = tv_technology.getText().toString();

        OkHttpClientManager manager = OkHttpClientManager.getInstance();
        StringBuffer buffer = new StringBuffer(ConstantUtils.USER_ADDRESS).append(ConstantUtils.USER_REGISTER);
        Map<String,String> map = new HashMap<>();
        map.put("type","update");
        map.put("userid",userInfo.getUserid()+"");
        map.put("userName",userName);
        map.put("userPhone",userPhone);
        map.put("userPassWord",userInfo.getU_password());
        map.put("imageurl",userHeadImgUrl);
        map.put("userAddress",userAddress);
        map.put("userIntroduction",userIntroduce);
        map.put("technology",userTechnology);

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
                handler.sendEmptyMessage(ConstantUtils.PERSONCOMPILE_UP_DATA);
            }
        });

    }

    /**
     * 跳转到编辑页面
     */
    private void setEdit(String title) {
        Intent intent = new Intent(PersonCompileActivity.this, EditActivity.class);
        intent.putExtra("title", title);
        startActivityForResult(intent, ConstantUtils.PERSON_COMPILE_REQUESTCODE);
    }


    /**
     * 跳转
     *
     * @param context
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PersonCompileActivity.class);
        context.startActivity(intent);
    }
   class PersonCompileHandler extends Handler{
       @Override
       public void handleMessage(Message msg) {
           super.handleMessage(msg);
           switch (msg.what){
               case ConstantUtils.PERSONCOMPILE_UP_DATA:
                   SharePreferencesUtil.saveObject(context,ConstantUtils.USER_LOGIN_INFO,userInfo);
                   finish();
                   break;
               default:
                   break;
           }
       }
   }

}
