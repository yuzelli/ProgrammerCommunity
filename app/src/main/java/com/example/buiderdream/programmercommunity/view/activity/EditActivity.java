package com.example.buiderdream.programmercommunity.view.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.buiderdream.programmercommunity.R;
import com.example.buiderdream.programmercommunity.base.BaseActivity;
import com.example.buiderdream.programmercommunity.constants.ConstantUtils;

public class EditActivity extends BaseActivity implements View.OnClickListener{
    private TextView tv_title;
    private ImageView img_back;
    private EditText et_input;
    private Button btn_cancel;
    private Button btn_finish;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        initView();
    }

    private void initView() {
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        img_back = (ImageView) this.findViewById(R.id.img_back);
        et_input = (EditText) this.findViewById(R.id.et_input);
        btn_cancel = (Button) this.findViewById(R.id.btn_cancel);
        btn_finish = (Button) this.findViewById(R.id.btn_finish);
        img_back.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_finish.setOnClickListener(this);

        tv_title.setText(title);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back:
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_finish:
                String content = et_input.getText().toString();
                if (content!=null){
                    Intent intent = new Intent();
                    intent.putExtra("result",content);
                    intent.putExtra("title",title);
                    //设置回传的意图p
                    setResult(ConstantUtils.PERSON_COMPILE_RESULTCODE, intent);
                }
                finish();
                break;
            default:
                break;
        }
    }
}
