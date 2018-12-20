package com.example.liuyibo.goods;

import android.app.Application;
import android.content.Context;

import com.example.liuyibo.goods.entity.Goods;
import com.example.liuyibo.goods.utils.SharedPreferenceUtil;
import com.example.liuyibo.goods.utils.network.ConConfig;


import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.exception.BmobException;

public class MyApplication extends Application {
    private static Context context;
    private boolean isInit = false;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Bmob.initialize(this, "e6da5c690aa1fff9b80bfe8d6ac361f3");
        BmobQuery.clearAllCachedResults();

        if(SharedPreferenceUtil.getString("admin","adminFlag")==null||"".equals(SharedPreferenceUtil.getString("admin","adminFlag"))){
            SharedPreferenceUtil.setString("admin","adminFlag","2");
        }
        Config.setAdminFlag(Integer.parseInt(SharedPreferenceUtil.getString("admin","adminFlag")));
        BmobInstallationManager.getInstance().initialize(new InstallationListener<BmobInstallation>() {
            @Override
            public void done(BmobInstallation bmobInstallation, BmobException e) {
                if (e == null) {
                } else {

                }
            }
        });
// 启动推送服务
        BmobPush.startWork(this);

    }
    public static Context getContext() {
        return context;
    }

}
