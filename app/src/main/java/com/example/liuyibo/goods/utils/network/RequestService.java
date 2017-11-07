package com.example.liuyibo.goods.utils.network;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.liuyibo.goods.entity.Goods;

import java.util.List;
import java.util.Map;

import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Created by Administrator on 2017/5/10.
 */

public interface RequestService {

//    @FormUrlEncoded
//    @POST("/api/mobile/login/")
//    Call<String> login(@Field("user_name") String username, @Field("password") String password);
//
//    @FormUrlEncoded
//    @POST("/api/mobile/register/")
//    Call<Object> register(@Field("user_name") String username, @Field("code") String code, @Field("password") String password, @Field("repassword") String repassword);
//
//    @FormUrlEncoded
//    @POST("/api/mobile/send_sms/")
//    Call<String> send_sms(@Field("user_name") String phoneNumber);
//
//    @FormUrlEncoded
//    @POST("/api/mobile/credit_data/")
//    Call<JSONArray> credit_data(@Field("p") String page);
    @GET("/selectAll")
    Call<List<Goods>> getAll();
    @POST("/validate")
    Call<String> validate(@Query("username") String username, @Query("password") String password);
    @POST("/addnew")
    Call<String> addnew (@Body Goods goods);
    @GET("/delete")
    Call<String> delete(@Query("id")long id);
}
