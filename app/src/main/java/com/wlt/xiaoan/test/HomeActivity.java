package com.wlt.xiaoan.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * author：Administrator on 2017/6/7 10:07
 * company: xxxx
 * email：1032324589@qq.com
 */

public class HomeActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_home);
    }
    public void onConfig(View v){
        Intent intent = new Intent(this,ConfigDeviceTest.class);
        startActivity(intent);
    }
    public void onCheck(View v){
        Intent intent = new Intent(this,CheckDeviceTest.class);
        startActivity(intent);
    }
}
