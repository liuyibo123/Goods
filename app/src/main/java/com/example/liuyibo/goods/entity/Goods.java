package com.example.liuyibo.goods.entity;


import cn.bmob.v3.BmobObject;


public class Goods  extends BmobObject {
    private String name;
    private String price;
    private String dw;
    private String category;
    private String bz;
    private String imageUrl;
    private String idnumber;
    public String getName() {
        return this.name;
    }
    public Goods setName(String name) {
        this.name = name;
        return this;
    }
    public String getPrice() {
        return this.price;
    }
    public Goods setPrice(String price) {
        this.price = price;
        return this;
    }
    public String getDw() {
        return this.dw;
    }
    public Goods setDw(String dw) {
        this.dw = dw;
        return this;
    }
    public String getCategory() {
        return this.category;
    }
    public Goods setCategory(String category) {
        this.category = category;
        return this;
    }
    public String getBz() {
        return this.bz;
    }
    public Goods setBz(String bz) {
        this.bz = bz;
        return this;
    }
    public String getImageUrl() {
        return this.imageUrl;
    }
    public Goods setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
    public String getIdnumber() {
        return this.idnumber;
    }
    public Goods setIdnumber(String idnumber) {
        this.idnumber = idnumber;
        return this;
    }
    
}
