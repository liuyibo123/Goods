package com.example.liuyibo.goods.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/10/24.
 */
@Entity
public class Goods {
    @Id
    private long id;
    private String name;
    private String price;
    private String dw;
    private String category;
    private String bz;
    private String imageUrl;
    @Generated(hash = 1740596642)
    public Goods(long id, String name, String price, String dw, String category,
            String bz, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.dw = dw;
        this.category = category;
        this.bz = bz;
        this.imageUrl = imageUrl;
    }
    @Generated(hash = 1770709345)
    public Goods() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPrice() {
        return this.price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getDw() {
        return this.dw;
    }
    public void setDw(String dw) {
        this.dw = dw;
    }
    public String getCategory() {
        return this.category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getBz() {
        return this.bz;
    }
    public void setBz(String bz) {
        this.bz = bz;
    }
    public String getImageUrl() {
        return this.imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
