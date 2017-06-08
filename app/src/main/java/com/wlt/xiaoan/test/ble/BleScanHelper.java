package com.wlt.xiaoan.test.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.widget.Toast;

import com.wlt.xiaoan.test.App;
import com.wlt.xiaoan.test.R;
import com.wlt.xiaoan.test.utils.Logger;

/**
 * author：Administrator on 2017/6/7 10:17
 * company: xxxx
 * email：1032324589@qq.com
 */
public class BleScanHelper {

    private Context context;
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler = new Handler();
    private boolean mScanning = false;
    private LeScanCallback leScanCallback;

    public static BleScanHelper instance = new BleScanHelper();

    public static BleScanHelper getInstance() {
        return instance;
    }

    private BleScanHelper() {

    }

    public void initialize(Context context) {
        this.context = context;
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(context, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            return;
        }
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Logger.e("Unable to initialize BluetoothManager.");
                return;
            }
        }
        if(mBluetoothAdapter == null)
            mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Logger.e("Unable to obtain a BluetoothAdapter.");
            return;
        }
        iniBle();
    }
    private void iniBle() {
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scanLeDevice(true);
                }
            },1000);
        }else {
            scanLeDevice(true);
        }
    }
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            App.getInstance().speak("正在打开蓝牙设备");
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }
    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            //scanBleForResult(device);
            // 发现小米3必须加以下的这3个语句，否则不更新数据，而三星的机子s3则没有这个问题
            /*if (mScanning == true) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            }*/
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //如何应用退至后台，发送异常通知
                    leScanCallback.LeScan(device,rssi,scanRecord);
                }
            }).start();
        }
    };
    public void stopScan(){
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
        mBluetoothAdapter = null;
    }
    public void startScan(){
        mBluetoothAdapter.startLeScan(mLeScanCallback);
    }

    public void registerLeScan(LeScanCallback leScanCallback) {
        this.leScanCallback = leScanCallback;
    }

    interface LeScanCallback{
        void LeScan(BluetoothDevice device, int rssi, byte[] scanRecord);
    }
}
