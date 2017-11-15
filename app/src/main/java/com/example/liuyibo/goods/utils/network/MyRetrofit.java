package com.example.liuyibo.goods.utils.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2017/6/18.
 */

public class MyRetrofit {
    public static Retrofit retrofit= new Retrofit.Builder()
            .baseUrl(ConConfig.getUrl())
                            .addConverterFactory(GsonConverterFactory.create())
                            .addConverterFactory(ScalarsConverterFactory.create())
            .build();
    public static void resetRetrofit(){
        retrofit = new Retrofit.Builder()
                .baseUrl(ConConfig.getUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }
    public static RequestService requestService = retrofit.create(RequestService.class);

}
