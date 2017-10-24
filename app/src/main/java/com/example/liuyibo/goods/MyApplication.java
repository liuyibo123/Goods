package com.example.liuyibo.goods;

import android.app.Application;
import android.content.Context;

import com.example.liuyibo.goods.dao.GreenDaoManager;

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
    }
    public static Context getContext() {
        return context;
    }

}
