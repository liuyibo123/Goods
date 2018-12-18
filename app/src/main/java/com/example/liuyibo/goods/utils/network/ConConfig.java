package com.example.liuyibo.goods.utils.network;
/**
 * Created by Administrator on 2017/5/22.
 */

public class ConConfig {
    public static String ipString = "api2.bmob.cn/1/classes";
    public static String portString = "";
    public static String getUrl(){
        return "https://api2.bmob.cn/1/classes/";
    }

    public static String getIpString() {
        return ipString;
    }

    public static void setIpString(String ipString) {
        ConConfig.ipString = ipString;
    }

    public static String getPortString() {
        return portString;
    }

    public static void setPortString(String portString) {
        ConConfig.portString = portString;
    }
}
