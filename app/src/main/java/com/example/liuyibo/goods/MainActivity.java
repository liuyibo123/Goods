package com.example.liuyibo.goods;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import com.example.liuyibo.goods.entity.Goods;
import com.example.liuyibo.goods.utils.SharedPreferenceUtil;
import com.example.liuyibo.goods.utils.network.GoodsDao;
import com.example.liuyibo.goods.utils.network.MyGoodsDao;
import com.example.liuyibo.goods.view.activity.AddNewActivity;
import com.example.liuyibo.goods.view.activity.LoginActivity;
import com.example.liuyibo.goods.view.activity.SettingActivity;
import com.example.liuyibo.goods.view.recyclerview.GoodsAdapter;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import cn.bmob.v3.listener.UpdateListener;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.main_swipe)
    SmartRefreshLayout mainSwipe;
    private List<Goods> goodsList = new ArrayList<>();
    @BindView(R.id.add)
    FloatingActionButton add;
    FloatingActionMenu menuLabelsRight;
    private GoodsDao dao = MyGoodsDao.getInstance();
private final String TAG ="MainActivity";
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
        goodsList.clear();
        getData(null);
        if(Config.getAdminFlag()==Config.isAdmin){
            menuLabelsRight.showMenu(true);
        }
    }

    private void init() {
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MyApplication.getContext());
        boolean isOpened = managerCompat.areNotificationsEnabled();
        if(!isOpened){
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Intent intent = new Intent();
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("app_package", getPackageName());
                intent.putExtra("app_uid", getApplicationInfo().uid);
                startActivity(intent);
            } else if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }
        add = findViewById(R.id.add);
        recycler = findViewById(R.id.recycler);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(MainActivity.this);
        recycler.setLayoutManager(manager);
        menuLabelsRight = findViewById(R.id.menu_labels_right);
        if (Config.getAdminFlag() == Config.isNotAdmin) {
            menuLabelsRight.hideMenu(true);
        }

//        getData(null);
        mainSwipe.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getData(refreshlayout);
                refreshlayout.finishRefresh();
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
                        if(Config.getAdminFlag()==Config.isAdmin)
                        {
                            Toast.makeText(MainActivity.this,"已经登陆，不能重复登陆",Toast.LENGTH_SHORT).show();
                            break;
                        }
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        break;
                    case R.id.logout:
                        if(Config.getAdminFlag()==Config.isNotAdmin)
                        {
                            Toast.makeText(MainActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
                            break;
                        }
                        SharedPreferenceUtil.clearString("admin");
                        Config.setAdminFlag(Config.isNotAdmin);
                        menuLabelsRight.hideMenu(true);
                        Toast.makeText(MainActivity.this,"注销成功",Toast.LENGTH_SHORT).show();

                        break;
                }
                return true;
            }
        });

//        getAllGoods();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting, menu);
        MenuItem menuItem = menu.findItem(R.id.search);//在菜单中找到对应控件的item
        SearchView search = (SearchView) menuItem.getActionView();

        Log.d("Tag", "menu create");
        search.setSubmitButtonEnabled(true);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String s) {
                if(goodsList.size()<600){
                    Toast.makeText(MyApplication.getContext(),"请稍后重试",Toast.LENGTH_SHORT).show();
                    MainActivity.this.mainSwipe.autoRefresh();
                    return false;
                }
                dao.getAll(new MyGoodsDao.OnGetDoneListener() {
                    @Override
                    public void done(List<Goods> goods) {
                        goodsList=goods;
                        List<Goods> searchList = new ArrayList<>();
                        for(Goods goods1:goodsList){
                            if(goods1.getName().contains(s)){
                                searchList.add(goods1);
                                continue;
                            }
                            if(goods1.getBz().contains(s)||goods1.getCategory().contains(s)){
                                searchList.add(goods1);
                            }
                        }
                        initRecyclerView(searchList,true);
                    }
                });
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

    private void getData(final RefreshLayout refreshLayout) {
//        if (goodsDao != null&&a==0) {
//            List<Goods> goodsList1 = goodsDao.loadAll();
//            if (goodsList1.size() > 0) {
//                goodsList = goodsDao.loadAll();
//                initRecyclerView();
//                return;
//            }
//        }
        Log.d("MainActivity", "getData: 进入getData");
        final ProgressDialog pd1 = new ProgressDialog(this);
        pd1.setTitle("正在更新数据");
        pd1.setIcon(R.mipmap.ic_launcher);
        pd1.setMessage("数据更新中......");
        pd1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd1.setCancelable(true);
        pd1.setIndeterminate(true);
        pd1.show();
        dao.getAll(new MyGoodsDao.OnGetDoneListener(){
            @Override
            public void done(List<Goods> goods) {
                goodsList =goods ;
                initRecyclerView(goodsList,false);
                if(refreshLayout!=null){
                    refreshLayout.finishRefresh();
                }
                pd1.hide();
            }
        });
//        Call<String> call = MyRetrofit.requestService.getAll();
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                Log.d("MainActivity", "onResponse: "+response.body());
//                goodsList = JSON.parseArray(response.body(),Goods.class);
//                initRecyclerView(goodsList);
//                if(refreshLayout!=null){
//                    refreshLayout.finishRefresh();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Toast.makeText(MyApplication.getContext(), t.getMessage(), Toast.LENGTH_SHORT);
//            }
//        });

    }

    private void initRecyclerView(List<Goods> goodsList1,boolean isSearch) {
        final GoodsAdapter adapter = new GoodsAdapter(goodsList1);
//        if(!isSearch){
//            adapter.setScrollEndListener(new GoodsAdapter.OnScrollEndListener() {
//                @Override
//                public void onscrollend(List<Goods> goods, int skip) {
//                    final BmobQuery<Goods> goodsQuery = new BmobQuery<>();
//                    boolean isCache = goodsQuery.hasCachedResult(Goods.class);
//                    if(isCache){
//                        goodsQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);   // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
//                    }else{
//                        goodsQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);   // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
//                    }
//                    goodsQuery.setLimit(100).setSkip(skip*100).findObjects(new FindListener<Goods>() {
//                        @Override
//                        public void done(List<Goods> list, BmobException e) {
//                            if(e==null){
//                                goodsList.addAll(list);
//                                adapter.notifyDataSetChanged();
//                            }else{
//                                Toast.makeText(MyApplication.getContext(),"查询失败",Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    });
//                }
//            });
//        }

        if(Config.getAdminFlag()==Config.isAdmin){
            adapter.setOnPopupmenuItemClickListener(new GoodsAdapter.PopupMenuItemClickListener() {
                @Override
                public void click(int itemId, final Goods current) {
                    switch (itemId) {
                        case Menu.FIRST + 0:
                            Intent i = new Intent(MainActivity.this, AddNewActivity.class);
                            JSON json = (JSON) JSON.toJSON(current);
                            i.putExtra("current", json.toJSONString());
                            startActivity(i);
                            break;
                        case Menu.FIRST + 1:
                            current.delete(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(MyApplication.getContext(), "删除成功", Toast.LENGTH_LONG).show();
                                        new BmobPushManager().pushMessage("商品"+current.getName()+"被删除,请及时查看");

                                        MainActivity.this.onResume();
                                    } else {
                                        Toast.makeText(MyApplication.getContext(), "删除失败", Toast.LENGTH_LONG).show();
                                    }
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

}
