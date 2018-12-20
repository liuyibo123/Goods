package com.example.liuyibo.goods.utils.network;

import com.example.liuyibo.goods.entity.Goods;

public interface GoodsDao {
    void forceGetAll();
    void getAll(MyGoodsDao.OnGetDoneListener listener);

    void modifyOne(Goods goods);
    void deleteOne(Goods goods);
    void addNew(Goods goods);
}
