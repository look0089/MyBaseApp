package org.jzs.mybaseapp.section.otherdemo.notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.SpannableString;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.jzs.mybaseapp.common.base.EventBusEntity;

@SuppressLint("NewApi")
public class NLService extends NotificationListenerService {
    public static final String TAG = "NLService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "——————————————————————————————NLService启动——————————————————————————————");
        EventBusEntity e = new EventBusEntity();
        e.creat = true;
        EventBus.getDefault().postSticky(e);

        StatusBarNotification[] activeNotifications = getActiveNotifications();
        if (activeNotifications != null) {
            for (int i = 0; i < activeNotifications.length; i++) {
                StatusBarNotification sbn = activeNotifications[i];

                Bundle extras = sbn.getNotification().extras;
                // 获取接收消息的抬头
                String notificationTitle = extras.getString(Notification.EXTRA_TITLE);
                // 获取接收消息的内容
                String notificationText = extras.getString(Notification.EXTRA_TEXT);
                //图标
                Icon largeIcon = sbn.getNotification().getLargeIcon();

                EventBusEntity entity = new EventBusEntity();
                entity.notifiIcon = largeIcon;
                entity.title = notificationTitle;
                entity.content = notificationText;
                EventBus.getDefault().postSticky(entity);
            }
        }
    }

    // 在收到消息时触发
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        // TODO Auto-generated method stub
        Bundle extras = sbn.getNotification().extras;
        // 获取接收消息APP的包名
        String notificationPkg = sbn.getPackageName();
        // 获取接收消息的抬头
        String notificationTitle = extras.getString(Notification.EXTRA_TITLE);
        // 获取接收消息的内容
        Object notificationText = extras.getString(Notification.EXTRA_TEXT);
        if (notificationText instanceof SpannableString) {
            Log.d(TAG, "is SpannableString ...." + ((SpannableString) notificationText).subSequence(0, ((SpannableString) notificationText).length()));
        } else {
            Log.d(TAG, "showMsg packageName=" + notificationPkg + ",title=" + notificationTitle + ",msgText=" + notificationText);
        }
        Icon largeIcon = sbn.getNotification().getLargeIcon();
        EventBusEntity entity = new EventBusEntity();
        entity.notifiIcon = largeIcon;
        entity.title = notificationTitle;
        entity.content = (String) notificationText;
        EventBus.getDefault().postSticky(entity);

        Log.i("XSL_Test", "Notification posted " + notificationTitle + " & " + notificationText);
    }

    // 在删除消息时触发
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        // TODO Auto-generated method stub
        Bundle extras = sbn.getNotification().extras;
        // 获取接收消息APP的包名
        String notificationPkg = sbn.getPackageName();
        // 获取接收消息的抬头
        String notificationTitle = extras.getString(Notification.EXTRA_TITLE);
        // 获取接收消息的内容
        String notificationText = extras.getString(Notification.EXTRA_TEXT);

        Log.i("XSL_Test", "Notification removed " + notificationTitle + " & " + notificationText);
    }
}