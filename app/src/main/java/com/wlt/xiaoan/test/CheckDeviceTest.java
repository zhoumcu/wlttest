package com.wlt.xiaoan.test;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.wlt.xiaoan.test.ble.BleScanHelper;
import com.wlt.xiaoan.test.ble.DataHelper;
import com.wlt.xiaoan.test.model.BleData;

import java.text.DecimalFormat;
import java.util.Timer;

/**
 * author：Administrator on 2017/6/7 10:12
 * company: xxxx
 * email：1032324589@qq.com
 */
public class CheckDeviceTest extends AppCompatActivity implements BleScanHelper.LeScanCallback {
    private TextView topleft_preesure;
    private TextView topleft_temp;
    private TextView topleft_voltage;
    private TextView topleft_releat;
    private TextView tv_complete;
    private TextView tv_note;

    private DecimalFormat df1;
    private DecimalFormat df;
    private int timeCount = 3;
    private Timer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_check_test);
         /*显示App icon左侧的back键*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        initUI();
        //初始化蓝牙传感器
        BleScanHelper.getInstance().initialize(this);
        BleScanHelper.getInstance().registerLeScan(this);
    }

    private void initUI() {
        tv_note = (TextView) findViewById(R.id.tv_notifyMsg);
        topleft_preesure = (TextView) findViewById(R.id.tv_preesure);
        topleft_temp = (TextView) findViewById(R.id.tv_temp);
        topleft_voltage = (TextView) findViewById(R.id.tv_voltage);
        topleft_releat = (TextView) findViewById(R.id.tv_releat);
        tv_complete = (TextView)findViewById(R.id.tv_complete);
        df1=new DecimalFormat("#.##");
        df=new DecimalFormat("#.#");
        //默认开启左前边扫描
        showDialog("蓝牙扫描中...", false);
    }

    private int countComp = 0;
    private int CLEAR_DATA = 1003;
    private boolean isTest = false;
    private String currentTestDevice;
    private String preTestDevice = "";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                tv_note.append(msg.obj+"\n");
            }else if(msg.what==2) {
                tv_note.setText(null);
                countComp++;
                topleft_preesure.setText("--");
                topleft_temp.setText("--");
                topleft_voltage.setText("--");
                topleft_releat.setText("--");
                tv_complete.setText("已完成 "+countComp+" 组测试！");
            }else if(msg.what==3) {
                tv_note.setText(null);
            }else if(msg.what==1002) {
                topleft_releat.setText((int)msg.obj+"");
            }else if(msg.what==CLEAR_DATA) {
                topleft_preesure.setText("--");
                topleft_temp.setText("--");
                topleft_voltage.setText("--");
                topleft_releat.setText("--");
            }else if(msg.what ==101){
                BleData bleData = (BleData) msg.obj;
                currentTestDevice = bleData.getDeviceAddress();
                if(Float.valueOf(bleData.getPress())>=1.5f){
                    isTest = true;
                    preTestDevice = bleData.getDeviceAddress();
                    topleft_preesure.setText(String.valueOf(bleData.getPress()));
                    topleft_temp.setText(String.valueOf(bleData.getTemp()));
                    topleft_voltage.setText(String.valueOf(bleData.getVoltage()));
                    topleft_releat.setText("-"+bleData.getRssi());
                    tv_complete.setText("编号为："+bleData.getDeviceAddress()+"传感器测试正常！");
                    showDialog("气压达到指定测试值！",false);
                }else if(preTestDevice.equals("")){
                    tv_complete.setText("编号为："+bleData.getDeviceAddress()+"传感器测试开始...\n气压需要大于等于1.5Bar时才能显示正常！");
                }
                if(Float.valueOf(bleData.getPress())<=0.5f&&isTest&&!preTestDevice.equals("")) {
                    isTest = false;
                    preTestDevice = "";
                    tv_complete.setText("编号为："+bleData.getDeviceAddress()+"由于气压低压指定值：测试结束！传感器正常！");
                    topleft_preesure.setText("--");
                    topleft_temp.setText("--");
                    topleft_voltage.setText("--");
                    topleft_releat.setText("--");
                    showDialog("测试结束！传感器正常！请更换下一个测试",false);
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BleScanHelper.getInstance().stopScan();
    }

    private void showDialog(String str, boolean isConnect) {
        App.getInstance().speak(str);
        Message msg = new Message();
        msg.what=1;
        msg.obj = str;
        handler.sendMessage(msg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_set:
                tv_note.setText("正在扫描。。。"+"\n");
                handler.sendEmptyMessage(CLEAR_DATA);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void LeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        BleData bleData = DataHelper.getScanData(scanRecord);
        bleData.setRssi(rssi);
        bleData.setDeviceAddress(device.getAddress());
        Message msg = new Message();
        msg.what=101;
        msg.obj = bleData;
        handler.sendMessage(msg);
    }
}
