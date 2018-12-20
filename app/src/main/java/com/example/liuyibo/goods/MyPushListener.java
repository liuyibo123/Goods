package com.example.liuyibo.goods;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.liuyibo.goods.utils.network.MyGoodsDao;

import cn.bmob.push.PushConstants;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyPushListener  extends BroadcastReceiver {
    private  NotificationManager notificationManager=(NotificationManager)MyApplication.getContext().getSystemService(NOTIFICATION_SERVICE);
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            Log.d("bmob", "客户端收到推送内容："+intent.getStringExtra("msg"));
            MyGoodsDao dao = MyGoodsDao.getInstance();
            dao.forceGetAll();
            Intent i=new Intent(MyApplication.getContext(),MainActivity.class);//设置为跳转页面准备的Intent
            //针对意图的包装对象,在下面就是通知被点击时激活的组件对象(上下文,请求码,意图对象,标识符)
            PendingIntent pendingIntent=PendingIntent.getActivity(MyApplication.getContext(), 0, intent, 0);

            Notification.Builder builder =new Notification.Builder(MyApplication.getContext());//实例化通知栏构造器Notification.Builder，参数必填（Context类型），为创建实例的上下文
            builder.setContentTitle("有新的数据变更")
            .setContentText(intent.getStringExtra("msg"))
                    .setContentIntent(pendingIntent).setTicker("新的推送")
            .setLargeIcon(BitmapFactory.decodeResource(MyApplication.getContext().getResources(),R.drawable.ic_car));
            if(android.os.Build.VERSION.SDK_INT>= android.os.Build.VERSION_CODES.LOLLIPOP) {
                builder.setVisibility(Notification.VISIBILITY_PUBLIC)
                        .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
                        .setCategory(Notification.CATEGORY_MESSAGE)
                        .setFullScreenIntent(pendingIntent, true)//将Notification变为悬挂式Notification
                        .setSmallIcon(R.drawable.ic_car);//设置小图标
            }else{
                builder.setSmallIcon(R.drawable.ic_car);//设置小图标
            }
            Notification notification = builder.build();
            notificationManager.notify(1,notification);


        }
    }
}
