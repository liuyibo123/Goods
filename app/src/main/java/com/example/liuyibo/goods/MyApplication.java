package com.example.liuyibo.goods;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;

import com.example.liuyibo.goods.dao.GreenDaoManager;
import com.example.liuyibo.goods.utils.SharedPreferenceUtil;
import com.example.liuyibo.goods.utils.network.ConConfig;

import java.util.logging.SocketHandler;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {
    private static Context context;
    private boolean isInit = false;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        GreenDaoManager.getInstance();
        JPushInterface.init(context);
        if(SharedPreferenceUtil.getString("admin","adminFlag")==null||"".equals(SharedPreferenceUtil.getString("admin","adminFlag"))){
            SharedPreferenceUtil.setString("admin","adminFlag","2");
        }
        Config.setAdminFlag(Integer.parseInt(SharedPreferenceUtil.getString("admin","adminFlag")));
        String ipString = SharedPreferenceUtil.getString("conconfig","ipString");
        String portString = SharedPreferenceUtil.getString("conconfig","portString");
        if(!"".equals(ipString)&&!"".equals(portString)){
            ConConfig.setIpString(ipString);
            ConConfig.setPortString(portString);
        }



    }
    public static Context getContext() {
        return context;
    }

}
