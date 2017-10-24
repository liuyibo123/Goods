package com.example.liuyibo.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;


import com.example.liuyibo.goods.dao.DaoMaster;
import com.example.liuyibo.goods.dao.DaoSession;
import com.example.liuyibo.goods.dao.GoodsDao;
import com.example.liuyibo.goods.dao.GreenDaoManager;
import com.example.liuyibo.goods.entity.Goods;
import com.example.liuyibo.goods.utils.network.MyRetrofit;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.ui.PushActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private List<Goods> goodsList = null;
    @BindView(R.id.search)
    SearchView search;
    @BindView(R.id.add)
    FloatingActionButton add;
    @BindView(R.id.menu_labels_right)
    FloatingActionMenu menuLabelsRight;
    private DaoSession daoSession;
    private GoodsDao goodsDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        startActivity(new Intent(MainActivity.this, PushActivity.class));
        init();
    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        daoSession = GreenDaoManager.getInstance().getmDaoSession();
        goodsDao = daoSession.getGoodsDao();
    }

    @OnClick({R.id.search, R.id.add, R.id.menu_labels_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add:
                startActivity(new Intent(MainActivity.this,null));
                break;
            case R.id.menu_labels_right:
                break;
        }
    }
    private void getData(){

        if(goodsDao!=null){
            List<Goods> goodsList1 = goodsDao.loadAll();
            if(goodsList1.size()>0){
                goodsList = goodsDao.loadAll();
                return;
            }
        }
        Call<List<Goods>> call= MyRetrofit.requestService.getAll();
        call.enqueue(new Callback<List<Goods>>() {
            @Override
            public void onResponse(Call<List<Goods>> call, Response<List<Goods>> response) {
                goodsList = response.body();
            }

            @Override
            public void onFailure(Call<List<Goods>> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(),t.getMessage(),Toast.LENGTH_SHORT);
            }
        });

    }
    private void saveData(){
        if(goodsDao!=null&&goodsList.size()>0){
            for(Goods goods:goodsList){
                goodsDao.save(goods);
            }
        }
    }
}
