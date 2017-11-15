package com.example.liuyibo.goods.utils.network;
/**
 * Created by Administrator on 2017/5/22.
 */

public class ConConfig {
    public static String ipString = "47.94.231.212";
    public static String portString = "8080";
    public static String getUrl(){
        return "http://"+ipString+":"+portString+"/";
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
