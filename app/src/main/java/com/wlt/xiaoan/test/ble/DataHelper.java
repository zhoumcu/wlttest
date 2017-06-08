package com.wlt.xiaoan.test.ble;

import com.wlt.xiaoan.test.model.BleData;
import com.wlt.xiaoan.test.utils.DataUtils;
import com.wlt.xiaoan.test.utils.DigitalTrans;
import com.wlt.xiaoan.test.utils.Logger;

import java.text.DecimalFormat;

/**
 * author：Administrator on 2016/9/28 08:54
 * company: xxxx
 * email：1032324589@qq.com
 */

public class DataHelper {

    public DataHelper() {

    }

    public static BleData getScanData(byte[] scanRecord){
        Logger.e(":"+ DataUtils.bytesToHexString(scanRecord));
        byte[] data = DataUtils.parseData(scanRecord).datas;
        return getData(data);
    }

    public static BleData getData(byte[] data){
        float voltage = 0.00f,press = 0, temp =0;
        int temp1 = 0,state = 0,rssi = 0;
        String pressStr = "";
        String voltageStr = "";
        DecimalFormat df = new DecimalFormat("#0.0#");
        BleData bleData = new BleData();
        if(data==null) return bleData;
        if(data.length==0) return bleData;
        if(data.length==4) {
            voltage = ((float)(DigitalTrans.byteToAlgorism(data[3])-31)*20/21+160)/100;
            press = ((float) DigitalTrans.byteToAlgorism(data[1])*160)/51/100;
            temp = (float)(DigitalTrans.byteToAlgorism(data[2])-50);
            state = DigitalTrans.byteToBin0x0F(data[0]);
            press = Math.round(press*10)*0.1f;
            voltageStr = df.format(voltage);
        }else if(data.length==9) {
            voltage = ((float)(DigitalTrans.byteToAlgorism(data[3])-31)*20/21+160)/100;
            press = ((float)DigitalTrans.byteToAlgorism(data[1])*160)/51/100;
            temp = (float)(DigitalTrans.byteToAlgorism(data[2])-50);
            state = DigitalTrans.byteToBin0x0F(data[0]);
            press = Math.round(press*10)*0.1f;
            voltageStr = df.format(voltage);
        }
        Logger.e("状态："+state+"\n"+"压力值："+press+"\n"+"温度："+temp+"\n"+"电压"+voltageStr+"");
        //计算异常情况
        bleData.setTemp (temp1);
        bleData.setPress(press);
        bleData.setStringPress(pressStr);
        bleData.setStatus(state);
        bleData.setVoltage(Float.parseFloat(voltageStr));
        bleData.setData(DigitalTrans.byte2hex(data));
        return bleData;
    }
}
