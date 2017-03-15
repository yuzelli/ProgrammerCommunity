package com.example.buiderdream.programmercommunity.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.buiderdream.programmercommunity.R;
import com.example.buiderdream.programmercommunity.base.BaseActivity;

public class ProtocolActivity extends BaseActivity {
  private ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protocol);
        img_back = (ImageView) this.findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    /**
     * 跳转
     *
     * @param context
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ProtocolActivity.class);
        context.startActivity(intent);
    }
}
