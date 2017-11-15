package com.example.liuyibo.goods;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.liuyibo.goods.dao.DaoSession;
import com.example.liuyibo.goods.dao.GoodsDao;
import com.example.liuyibo.goods.dao.GreenDaoManager;
import com.example.liuyibo.goods.entity.Goods;
import com.example.liuyibo.goods.utils.SharedPreferenceUtil;
import com.example.liuyibo.goods.utils.network.ConConfig;
import com.example.liuyibo.goods.utils.network.MyRetrofit;
import com.example.liuyibo.goods.view.activity.AddNewActivity;
import com.example.liuyibo.goods.view.activity.LoginActivity;
import com.example.liuyibo.goods.view.activity.SettingActivity;
import com.example.liuyibo.goods.view.recyclerview.GoodsAdapter;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.ui.PushActivity;
import jp.co.recruit_lifestyle.android.widget.BeerSwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.main_swipe)
    BeerSwipeRefreshLayout mainSwipe;
    private List<Goods> goodsList = null;

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
        //startActivity(new Intent(MainActivity.this, PushActivity.class));
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
        if(Config.getAdminFlag()==Config.isAdmin){
            menuLabelsRight.showMenu(true);
        }
    }

    private void init() {

        add = findViewById(R.id.add);
        recycler = findViewById(R.id.recycler);
        menuLabelsRight = findViewById(R.id.menu_labels_right);
        if (Config.getAdminFlag() == Config.isNotAdmin) {
            menuLabelsRight.hideMenu(true);
        }

        daoSession = GreenDaoManager.getInstance().getmDaoSession();
        goodsDao = daoSession.getGoodsDao();
        getData();
        mainSwipe.setOnRefreshListener(new BeerSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Task().execute();
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.setting:
                        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.login:
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        break;
                }
                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting, menu);
        MenuItem menuItem = menu.findItem(R.id.search);//在菜单中找到对应控件的item
        SearchView search = (SearchView) MenuItemCompat.getActionView(menuItem);

        Log.d("Tag", "menu create");
        search.setSubmitButtonEnabled(true);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                List<Goods> searchList = new ArrayList<Goods>();
                for(Goods goods:goodsList){
                    if(goods.getName().contains(s)){
                        searchList.add(goods);
                        continue;
                    }
                    if(goods.getBz().contains(s)||goods.getCategory().contains(s)){
                        searchList.add(goods);
                    }
                }
                initRecyclerView(searchList);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        search.setIconifiedByDefault(false);
        return super.onCreateOptionsMenu(menu);

    }

    private class Task extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... voids) {
            mainSwipe.setRefreshing(true);
            getData();
            //todo beer 流下来

            return new String[0];
        }

        @Override protected void onPostExecute(String[] result) {
            // Call setRefreshing(false) when the list has been refreshed.
            mainSwipe.setRefreshing(false);
            super.onPostExecute(result);
        }
    }

    @OnClick({ R.id.add, R.id.menu_labels_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add:
                menuLabelsRight.hideMenu(true);
                startActivity(new Intent(MainActivity.this, AddNewActivity.class));
                break;
            case R.id.menu_labels_right:
                break;
        }
    }

    private void getData() {
//        if (goodsDao != null&&a==0) {
//            List<Goods> goodsList1 = goodsDao.loadAll();
//            if (goodsList1.size() > 0) {
//                goodsList = goodsDao.loadAll();
//                initRecyclerView();
//                return;
//            }
//        }
        Call<List<Goods>> call = MyRetrofit.requestService.getAll();
        call.enqueue(new Callback<List<Goods>>() {
            @Override
            public void onResponse(Call<List<Goods>> call, Response<List<Goods>> response) {
                goodsList = response.body();
                initRecyclerView(goodsList);
            }

            @Override
            public void onFailure(Call<List<Goods>> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), t.getMessage(), Toast.LENGTH_SHORT);
            }
        });

    }

    private void initRecyclerView(List<Goods> goodsList) {
        GoodsAdapter adapter = new GoodsAdapter(goodsList);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(MainActivity.this);
        recycler.setLayoutManager(manager);
        if(Config.getAdminFlag()==Config.isAdmin){
            adapter.setOnPopupmenuItemClickListener(new GoodsAdapter.PopupMenuItemClickListener() {
                @Override
                public void click(int itemId, Goods current) {
                    switch (itemId) {
                        case Menu.FIRST + 0:
                            Intent i = new Intent(MainActivity.this, AddNewActivity.class);
                            JSON json = (JSON) JSON.toJSON(current);
                            i.putExtra("current", json.toJSONString());
                            startActivity(i);
                            break;
                        case Menu.FIRST + 1:
                            Call<String> call = MyRetrofit.requestService.delete(current.getId());
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    Toast.makeText(MyApplication.getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                                    getData();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Toast.makeText(MyApplication.getContext(), "删除失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                            break;
                    }
                }
            });
        }else{
            adapter.setOnPopupmenuItemClickListener(null);
        }
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
