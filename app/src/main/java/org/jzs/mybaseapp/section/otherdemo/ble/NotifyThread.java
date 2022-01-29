package org.jzs.mybaseapp.section.otherdemo.ble;

/**
 * Created by Jzs on 2022/1/21.
 */

import android.os.Message;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Pencilso on 2017/4/25.
 * 处理回调所有接口
 */
public class NotifyThread implements Runnable {
    private List<BlueNotifyListener> listeners;
    private Message notify;

    @Override
    public void run() {
        if (notify == null || listeners == null)
            return;
        try {
            Iterator<BlueNotifyListener> iterator = listeners.iterator();
            while (iterator.hasNext()) {
                BlueNotifyListener next = iterator.next();
                if (next == null)
                    iterator.remove();
                else
                    next.onNotify(notify);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setListeners(List<BlueNotifyListener> listeners) {
        this.listeners = listeners;
    }

    public void setNotify(Message notify) {
        this.notify = notify;
    }
}