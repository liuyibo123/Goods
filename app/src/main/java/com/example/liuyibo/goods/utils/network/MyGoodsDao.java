package com.example.liuyibo.goods.utils.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.example.liuyibo.goods.MyApplication;
import com.example.liuyibo.goods.entity.Goods;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.bmob.v3.BmobQuery;

public class MyGoodsDao implements GoodsDao {
    private final String TAG = "MyGoodsDao";
    private List<Goods> goods=new ArrayList<>();
    public interface OnGetDoneListener{
        void done(List<Goods> goods);
    }
    private MyGoodsDao(){

    }
    private static MyGoodsDao dao;
    public static MyGoodsDao getInstance(){
        if(dao==null){
            dao = new MyGoodsDao();
        }
        return dao;
    }

    @Override
    public void forceGetAll() {
        this.goods.clear();
        getFromNetWork(new OnGetDoneListener() {
            @Override
            public void done(List<Goods> goods) {

            }
        });
    }

    @Override
    public void getAll(OnGetDoneListener listener) {
        if(goods!=null&&goods.size()>0){
            listener.done(goods);
        }else{
            String goods="";
            try {
               goods  = getFromFile();
            }catch (Exception e){

            }
            if (goods.length() > 0) {
                List<Goods> goodsList= JSON.parseArray(goods, Goods.class);
                MyGoodsDao.this.goods = goodsList;
                listener.done(goodsList);
            } else {
                getFromNetWork(listener);
            }
        }

    }

    @Override
    public void modifyOne(Goods goods) {

    }

    @Override
    public void deleteOne(Goods goods) {

    }

    @Override
    public void addNew(Goods goods) {

    }

    private void getFromNetWork(final OnGetDoneListener listener) {
       new AsyncTask<Void, Void, List<Goods>>() {
            @Override
            protected List<Goods> doInBackground(Void... voids) {
                List<Goods> list = new ArrayList<>();

                BmobQuery<Goods> query = new BmobQuery<>();
                int i=0;
                Object temp = query.setLimit(100).setSkip(i*100).findObjectsSync(Goods.class);
                while (temp!=null&&temp.toString().length()>10) {
                    Log.d(TAG, "temp.size"+((List) temp).size());
                    list.addAll((Collection<? extends Goods>) temp);
                    i++;
                    temp=query.setLimit(100).setSkip(i*100).findObjectsSync(Goods.class);
                }
                return list;
            }

            @Override
            protected void onPostExecute(List<Goods> goods) {
                super.onPostExecute(goods);
                MyGoodsDao.this.writeIntoFile(JSON.toJSONString(goods));
                MyGoodsDao.this.goods=goods;
                listener.done(goods);
            }
        }.execute();
    }

    private String getFromFile() {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = MyApplication.getContext().openFileInput("goods.txt");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            Log.d(TAG, "getFromFile: "+e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

    private void writeIntoFile(String content) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = MyApplication.getContext().openFileOutput("goods.txt", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(content);
        } catch (IOException e) {
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
