package org.jzs.mybaseapp.section.otherdemo.ble;

import android.app.Service;
import android.content.Intent;
import android.net.VpnService;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import org.jzs.mybaseapp.common.utils.ToastUtils;

import androidx.annotation.Nullable;

/**
 * Created by Jzs on 2018/6/26.
 */
public class BLEService extends Service {

    private BluetoothBinder bluetoothBinder;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("BLEService", "onBind");
        return null;
    }
}
