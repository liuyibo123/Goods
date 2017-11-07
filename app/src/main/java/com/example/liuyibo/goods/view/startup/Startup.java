package com.example.liuyibo.goods.view.startup;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.example.liuyibo.goods.MainActivity;
import com.example.liuyibo.goods.R;
import com.example.liuyibo.goods.view.activity.LoginActivity;

/**
 * Created by Administrator on 2017/8/23 0023.
 */

public class Startup extends AppCompatActivity{
    private ImageView welcomeImg = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);
        welcomeImg = (ImageView) this.findViewById(R.id.imageView3);
        AlphaAnimation anima = new AlphaAnimation(0.3f, 1.0f);
        anima.setDuration(2000);// 设置动画显示时间
        welcomeImg.startAnimation(anima);
        anima.setAnimationListener(new AnimationImpl());
    }
    private class AnimationImpl implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
        }
        @Override
        public void onAnimationEnd(Animation animation) {
            skip();
        }
        @Override
        public void onAnimationRepeat(Animation animation) {
        }

    }
    private void skip(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

}
