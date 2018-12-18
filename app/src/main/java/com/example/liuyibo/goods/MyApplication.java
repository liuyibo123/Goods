package com.example.liuyibo.goods;

import android.app.Application;
import android.content.Context;

import com.example.liuyibo.goods.entity.Goods;
import com.example.liuyibo.goods.utils.SharedPreferenceUtil;
import com.example.liuyibo.goods.utils.network.ConConfig;


import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;

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
//        String ipString = SharedPreferenceUtil.getString("conconfig","ipString");
//        String portString = SharedPreferenceUtil.getString("conconfig","portString");
//        if(!"".equals(ipString)&&!"".equals(portString)){
//            ConConfig.setIpString(ipString);
//            ConConfig.setPortString(portString);
//        }



    }
    public static Context getContext() {
        return context;
    }

}
