package com.example.liuyibo.goods.dao;


import com.example.liuyibo.goods.MyApplication;

/**
 * Created by Administrator on 2017/10/17.
 */
public class GreenDaoManager
{
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    static GreenDaoManager manager;
    private GreenDaoManager()
    {
        init();
    }

    /**
     * 静态内部类，实例化对象使用
     */

    /**
     * 对外唯一实例的接口
     *
     * @return
     */
    public static GreenDaoManager getInstance()
    {
        if(manager==null){
            manager = new GreenDaoManager();
        }
        return manager;
    }

    /**
     * 初始化数据
     */
    private void init()
    {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(MyApplication.getContext(),
                "questions");
        mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }
    public DaoMaster getmDaoMaster()
    {
        return mDaoMaster;
    }
    public DaoSession getmDaoSession()
    {
        return mDaoSession;
    }
    public DaoSession getNewSession()
    {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }
}

