package org.jzs.mybaseapp.section.live;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import org.jzs.mybaseapp.MainActivity;
import org.jzs.mybaseapp.R;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

/**
 * Created by Jzs on 2017/12/21 0021.
 */

public class LiveService extends Service {
    public static final String TAG = "LiveService";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Log.e(TAG, "handleMessage: live");
                    break;
            }
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            Message message = new Message();
            message.what = 1;
            mHandler.sendMessage(message);
            //要做的事情，这里再次调用此Runnable对象，以实现每两秒实现一次的定时器操作
            mHandler.postDelayed(this, 2000);
        }
    };

    public static void toLiveService(Context pContext) {
        Intent intent = new Intent(pContext, LiveService.class);
        pContext.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Log.d(TAG, "MyService: onCreate()");

        showNotification(this);
        mHandler.postDelayed(runnable, 2000);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //屏幕关闭的时候启动一个1像素的Activity，开屏的时候关闭Activity
        final ScreenManager screenManager = ScreenManager.getInstance(LiveService.this);
        ScreenBroadcastListener listener = new ScreenBroadcastListener(this);
        listener.registerListener(new ScreenBroadcastListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                Log.e(TAG, "onScreenOn: ");
                screenManager.finishActivity();
            }

            @Override
            public void onScreenOff() {
                Log.e(TAG, "onScreenOff: ");
                screenManager.startActivity();
            }
        });
        return START_REDELIVER_INTENT;
    }


    /**
     * 显示一个普通的通知
     *
     * @param context 上下文
     */
    public void showNotification(Context context) {
        Notification notification = new NotificationCompat.Builder(context)
                /**设置通知左边的大图标**/
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                /**设置通知右边的小图标**/
                .setSmallIcon(R.mipmap.ic_launcher)
                /**通知首次出现在通知栏，带上升动画效果的**/
                .setTicker("通知来了")
                /**设置通知的标题**/
                .setContentTitle("这是一个通知的标题")
                /**设置通知的内容**/
                .setContentText("这是一个通知的内容这是一个通知的内容")
                /**通知产生的时间，会在通知信息里显示**/
                .setWhen(System.currentTimeMillis())
                /**设置该通知优先级**/
                .setPriority(Notification.PRIORITY_DEFAULT)
                /**设置这个标志当用户单击面板就可以让通知将自动取消**/
                .setAutoCancel(true)
                /**设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)**/
                .setOngoing(false)
                /**向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：**/
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setContentIntent(PendingIntent.getActivity(context, 1, new Intent(context, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT))
                .build();
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
//        /**发起通知**/
//        notificationManager.notify(0, notification);
        startForeground(1, notification);
    }
}
