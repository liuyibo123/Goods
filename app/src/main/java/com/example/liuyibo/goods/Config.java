package com.example.liuyibo.goods;

/**
 * Created by Administrator on 2017/10/24.
 */

public class Config {
    public  static final int isAdmin = 1;
    public  static final int isNotAdmin = 2;
    public  static int adminFlag=2;
    public static int getAdminFlag() {
        return adminFlag;
    }
    public static void setAdminFlag(int adminFlag) {
        Config.adminFlag = adminFlag;
    }
}
