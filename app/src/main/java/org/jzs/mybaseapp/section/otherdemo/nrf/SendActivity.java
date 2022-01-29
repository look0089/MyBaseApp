package org.jzs.mybaseapp.section.otherdemo.nrf;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;
import android.os.Bundle;

import org.jzs.mybaseapp.R;
import org.jzs.mybaseapp.databinding.ActivityConnectDetailBinding;

import androidx.databinding.DataBindingUtil;

public class SendActivity extends Activity {
    public static final String TAG = "DetailActivity";

    public ActivityConnectDetailBinding mBinding;
    public String deviceAddress = "";
    public BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    public BluetoothDevice device;
    private BluetoothGatt mBluetoothGatt;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("蓝牙打印");
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_connect_detail);
        deviceAddress = getIntent().getStringExtra("address");
        device = bluetoothAdapter.getRemoteDevice("C8:65:F9:8F:CA:1F");
    }

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        /**
         * 连接状态改变回调
         * @param gatt
         * @param status
         * @param newState
         */
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                //阅读连接的远程设备的RSSI。
                gatt.readRemoteRssi();
                // 发现远程设备提供的服务及其特性和描述符
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            }
        }

        /**
         * 当远程设备的远程服务列表，特征和描述符已被更新，即已发现新服务时，调用回调。表示可以与之通信了。
         * @param gatt
         * @param status
         */
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
//                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
//            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }
    };


    public void connection() {
        device = bluetoothAdapter.getRemoteDevice("C8:65:F9:8F:CA:1F");
        device.connectGatt(this, false, mGattCallback);
    }
}
