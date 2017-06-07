package com.wlt.xiaoan.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView topleft_voltage = (TextView) findViewById(R.id.text);
        if(Float.valueOf("0.3")>0.3f) {
            topleft_voltage.setText(">0.3");
        }else {
            topleft_voltage.setText("<=0.3");
        }
    }
}
