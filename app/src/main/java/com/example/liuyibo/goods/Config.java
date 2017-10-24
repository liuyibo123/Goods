package com.example.liuyibo.goods;

/**
 * Created by Administrator on 2017/10/24.
 */

public class Config {
    private final int isAdmin = 1;
    private final int isNotAdmin = 2;
    private static int adminFlag;
    public static int getAdminFlag() {
        return adminFlag;
    }
    public static void setAdminFlag(int adminFlag) {
        Config.adminFlag = adminFlag;
    }
}
