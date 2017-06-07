package com.wlt.xiaoan.test;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by bob on 2015/1/30.
 */
public class App extends Application implements TextToSpeech.OnInitListener{

    public static App app;
    public TextToSpeech textToSpeech;

    //public static DeviceDao getDeviceDao() {
    //    return deviceDao;
    //}

    //private static DeviceDao deviceDao;
    private List<Activity> mList = new LinkedList<Activity>();

    public App() {
        app = this;
    }

    public static synchronized App getInstance() {
        if (app == null) {
            app = new App();
        }
        return app;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        //deviceDao= new DeviceDao(this);
        textToSpeech = new TextToSpeech(this, this);
    }
    public void add(String str)
    {
        textToSpeech.addSpeech(str,"test");
        speak(str);
    }
    public void speak(String text)
    {
        if(!textToSpeech.isSpeaking())
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH,null);
    }
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }
    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //System.exit(0);
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.CHINA);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.ERROR) {
                Toast.makeText(this, "数据丢失或语言不支持", Toast.LENGTH_SHORT).show();
            }
            if (result == TextToSpeech.LANG_AVAILABLE) {
                Toast.makeText(this, "支持该语言", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(this, "初始化成功", Toast.LENGTH_SHORT).show();
        }
    }
}
