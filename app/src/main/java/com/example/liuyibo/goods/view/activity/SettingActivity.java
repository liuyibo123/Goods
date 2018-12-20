package com.example.liuyibo.goods.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.liuyibo.goods.R;
import com.example.liuyibo.goods.utils.SharedPreferenceUtil;
import com.example.liuyibo.goods.utils.network.ConConfig;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.editText)
    EditText ip;
    @BindView(R.id.editText2)
    EditText port;
    @BindView(R.id.btn_makesure)
    Button btnMakesure;
    @BindView(R.id.btn_cancel)
    Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        ip.setText(ConConfig.getIpString());
        port.setText(ConConfig.getPortString());
    }

    @OnClick({R.id.btn_makesure, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_makesure:
                String ipString = ip.getText().toString();
                String portString = port.getText().toString();
                SharedPreferenceUtil.setString("conconfig","ipString",ipString);
                SharedPreferenceUtil.setString("conconfig","portString",portString);
                ConConfig.setPortString(portString);
                ConConfig.setIpString(ipString);
//                MyRetrofit.resetRetrofit();
                this.finish();
                break;
            case R.id.btn_cancel:
                this.finish();
                break;
        }
    }
}
