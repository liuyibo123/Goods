package com.example.liuyibo.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.liuyibo.goods.dao.DaoSession;
import com.example.liuyibo.goods.dao.GoodsDao;
import com.example.liuyibo.goods.dao.GreenDaoManager;
import com.example.liuyibo.goods.entity.Goods;
import com.example.liuyibo.goods.utils.network.MyRetrofit;
import com.example.liuyibo.goods.view.activity.AddNewActivity;
import com.example.liuyibo.goods.view.recyclerview.GoodsAdapter;
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

    @BindView(R.id.recycler)
    RecyclerView recycler;
    private List<Goods> goodsList = null;
    @BindView(R.id.search)
    SearchView search;
    @BindView(R.id.add)
    FloatingActionButton add;
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
        search = findViewById(R.id.search);
        add = findViewById(R.id.add);
        recycler = findViewById(R.id.recycler);
        menuLabelsRight = findViewById(R.id.menu_labels_right);
        Log.d("MainActivity", "init: "+Config.getAdminFlag());
        if (Config.getAdminFlag() == Config.isNotAdmin) {
            menuLabelsRight.hideMenu(true);
        }
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
        getData();

    }

    @OnClick({R.id.search, R.id.add, R.id.menu_labels_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add:
                startActivity(new Intent(MainActivity.this, AddNewActivity.class));
                break;
            case R.id.menu_labels_right:
                break;
        }
    }

    private void getData() {

        if (goodsDao != null) {
            List<Goods> goodsList1 = goodsDao.loadAll();
            if (goodsList1.size() > 0) {
                goodsList = goodsDao.loadAll();
                initRecyclerView();
                return;
            }
        }
        Call<List<Goods>> call = MyRetrofit.requestService.getAll();
        call.enqueue(new Callback<List<Goods>>() {
            @Override
            public void onResponse(Call<List<Goods>> call, Response<List<Goods>> response) {
                goodsList = response.body();
                initRecyclerView();
            }

            @Override
            public void onFailure(Call<List<Goods>> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), t.getMessage(), Toast.LENGTH_SHORT);
            }
        });

    }

    private void initRecyclerView() {
        GoodsAdapter adapter = new GoodsAdapter(goodsList);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(MainActivity.this);
        recycler.setLayoutManager(manager);
        adapter.setOnPopupmenuItemClickListener(new GoodsAdapter.PopupMenuItemClickListener() {
            @Override
            public void click(int itemId, Goods current) {
                switch (itemId){
                    case Menu.FIRST+0:
                        Intent i = new Intent(MainActivity.this,AddNewActivity.class);
                        JSON json = (JSON) JSON.toJSON(current);
                        i.putExtra("current",json.toJSONString());
                        startActivity(i);
                        break;
                    case Menu.FIRST+1:
                        Call<String> call = MyRetrofit.requestService.delete(current.getId());
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Toast.makeText(MyApplication.getContext(),"删除成功",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(MyApplication.getContext(),"删除失败",Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                }
            }
        });
        recycler.setAdapter(adapter);

    }

    private void saveData() {
        if (goodsDao != null && goodsList.size() > 0) {
            for (Goods goods : goodsList) {
                goodsDao.save(goods);
            }
        }
    }
}
