package com.example.liuyibo.goods.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.liuyibo.goods.MyApplication;
import com.example.liuyibo.goods.R;
import com.example.liuyibo.goods.entity.Goods;
import com.example.liuyibo.goods.utils.UUID;
import com.example.liuyibo.goods.utils.network.MyRetrofit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewActivity extends AppCompatActivity {
    private final String TAG = "MainActivity ";
    private int flag = 0;
    private long id;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.et_dw)
    EditText etDw;
    @BindView(R.id.et_category)
    EditText etCategory;
    @BindView(R.id.et_bz)
    EditText etBz;
    @BindView(R.id.btn_sure)
    Button btnSure;
    @BindView(R.id.btn_cancel)
    Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String json = intent.getStringExtra("current");
        if(json!=null&&!"".equals(json)){
            flag = 1;
            Log.d(TAG, "json: "+json);
            Goods goods = JSON.parseObject(json,Goods.class);
            etName.setText(goods.getName());
            etBz.setText(goods.getBz());
            etCategory.setText(goods.getCategory());
            etDw.setText(goods.getDw());
            etPrice.setText(goods.getPrice());
            id = goods.getId();
        }
    }

    @OnClick({R.id.btn_sure, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_sure:
                if(flag==1){
                    //todo delete
                    Call<String> call = MyRetrofit.requestService.delete(id);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Log.d(TAG, "onResponse: 删除原有东西成功");
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                }
                Goods goods = new Goods();
                goods.setName(etName.getText().toString());
                goods.setBz(etBz.getText().toString());
                goods.setCategory(etCategory.getText().toString());
                goods.setDw(etDw.getText().toString());
                goods.setPrice(etPrice.getText().toString());
                Log.d("Goods", "onViewClicked: "+goods.getBz()+goods.getCategory()+goods.getDw()+goods.getName()+goods.getPrice()+goods.getId());
                Call<String> call = MyRetrofit.requestService.addnew(goods);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if("1".equals(response.body())){
                            Toast.makeText(MyApplication.getContext(),"保存成功",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(MyApplication.getContext(),"保存失败",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(MyApplication.getContext(),"保存失败",Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case R.id.btn_cancel:
                break;
        }
    }
}
