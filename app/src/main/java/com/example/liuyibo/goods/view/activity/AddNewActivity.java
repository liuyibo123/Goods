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
import com.example.liuyibo.goods.MyApplication;
import com.example.liuyibo.goods.R;
import com.example.liuyibo.goods.entity.Goods;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class AddNewActivity extends AppCompatActivity {
    private Goods current = null;
    private final String TAG = "MainActivity ";
    @BindView(R.id.et_id)
    EditText etId;
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
        if (json != null && !"".equals(json)) {
            flag = 1;
            Log.d(TAG, "json: " + json);
            current = JSON.parseObject(json, Goods.class);
            etName.setText(current.getName());
            etBz.setText(current.getBz());
            etCategory.setText(current.getCategory());
            etDw.setText(current.getDw());
            etPrice.setText(current.getPrice());
            etId.setText(current.getIdnumber());
        }
    }

    @OnClick({R.id.btn_sure, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_sure:
                btnSure.setEnabled(false);
                if (flag == 1) {
                    current.setName(etName.getText().toString());
                    current.setBz(etBz.getText().toString());
                    current.setCategory(etCategory.getText().toString());
                    current.setDw(etDw.getText().toString());
                    current.setPrice(etPrice.getText().toString());
                    current.setIdnumber(etId.getText().toString());
                    Log.d(TAG, "onViewClicked: " + current.getBz() + current.getCategory() + current.getDw() + current.getName() + current.getPrice() + current.getIdnumber());
                    current.update(current.getObjectId(),new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(MyApplication.getContext(), "更新成功", Toast.LENGTH_LONG).show();
                                new BmobPushManager().pushMessage("商品"+current.getName()+"有变动,请及时查看");
                                AddNewActivity.this.finish();
                            } else {
                                Toast.makeText(MyApplication.getContext(), "更新失败", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
    }else

    {
        final Goods goods = new Goods();
        goods.setName(etName.getText().toString());
        goods.setBz(etBz.getText().toString());
        goods.setCategory(etCategory.getText().toString());
        goods.setDw(etDw.getText().toString());
        goods.setPrice(etPrice.getText().toString());
        goods.setIdnumber(etId.getText().toString());
        Log.d(TAG, "onViewClicked: " + goods.getBz() + goods.getCategory() + goods.getDw() + goods.getName() + goods.getPrice() + goods.getIdnumber());
        goods.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(MyApplication.getContext(), "保存成功", Toast.LENGTH_LONG).show();
                    new BmobPushManager().pushMessage("新增商品"+goods.getName()+",请及时查看");

                    AddNewActivity.this.finish();
                } else {
                    Toast.makeText(MyApplication.getContext(), "保存失败", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

                break;
            case R.id.btn_cancel:
            etPrice.setText("");
                etDw.setText("");
                etCategory.setText("");
                etBz.setText("");
                etName.setText("");
                break;
}
    }
            }
