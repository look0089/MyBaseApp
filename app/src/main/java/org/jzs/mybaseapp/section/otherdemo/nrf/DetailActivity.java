package org.jzs.mybaseapp.section.otherdemo.nrf;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;

import org.jzs.mybaseapp.R;
import org.jzs.mybaseapp.common.utils.AsyncUtils;
import org.jzs.mybaseapp.databinding.ActivityConnectDetailBinding;
import org.jzs.mybaseapp.section.otherdemo.ble.BluetoothBinder;
import org.jzs.mybaseapp.section.otherdemo.xinzhongxin.utils.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import androidx.databinding.DataBindingUtil;

public class DetailActivity extends Activity {
    public static final String TAG = "DetailActivity";

    public ActivityConnectDetailBinding mBinding;
    public String deviceAddress = "";
    public BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    public BluetoothDevice device;
    private BluetoothGatt mBluetoothGatt;
    private boolean isConnection = false;
    private static BluetoothSocket bluetoothSocket = null;
    private static OutputStream outputStream = null;
    private static final UUID serUUID = UUID.fromString("0000fe60-0000-1000-8000-00805f9b34fb");
    private static final UUID writeUUID = UUID.fromString("0000fe61-0000-1000-8000-00805f9b34fb");
    private static final UUID readUUID = UUID.fromString("0000fe62-0000-1000-8000-00805f9b34fb");

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("蓝牙打印");
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_connect_detail);
//        deviceAddress = getIntent().getStringExtra("address");
        device = bluetoothAdapter.getRemoteDevice("C9:C4:AF:A1:D4:BE");
        mBinding.tvName.setText(device.getName() + "\n" + device.getAddress());

        mBinding.btnConnect.setOnClickListener(v -> {
            bluetoothAdapter.cancelDiscovery();
            AsyncUtils.doAsync(this, c -> {
                connection();
            });
        });

        mBinding.btnSend.setOnClickListener(v -> {
            BluetoothGattCharacteristic gattChar = mBluetoothGatt.getService(serUUID).getCharacteristic(writeUUID);
            //byte[] str = str2Byte("1");
            gattChar.setValue("1");
            mBluetoothGatt.writeCharacteristic(gattChar);
        });
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
                Log.i(TAG, "Connected to GATT server.");
//                Log.i(TAG, "Attempting to start service discovery:" + mBluetoothGatt.discoverServices());
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
                List<BluetoothGattService> bluetoothGattServices = gatt.getServices();
                //发现服务是可以在这里查找支持的所有服务
                for (BluetoothGattService ser : bluetoothGattServices) {
                    String uuid = ser.getUuid().toString();
                    Log.d(TAG, "BluetoothGattService Name=" + (Utils.attributes.containsKey(uuid) ? Utils.attributes.get(uuid) : "Unknown Service"));
                    Log.d(TAG, "BluetoothGattService uuid=" + uuid);
                    BluetoothGattService gattService = mBluetoothGatt.getService(ser.getUuid());
                    mBluetoothGatt.readRemoteRssi();
                    List<BluetoothGattCharacteristic> gattchars = gattService.getCharacteristics();
                    for (BluetoothGattCharacteristic c : gattchars) {
                        HashMap<String, String> currentCharData = new HashMap<String, String>();
                        String uuidStr = c.getUuid().toString();
                        currentCharData.put("Name", Utils.attributes.containsKey(uuidStr) ? Utils.attributes.get(uuidStr) : "Unknown Characteristics");
                        Log.d(TAG, "BluetoothGattCharacteristic Name=" + (Utils.attributes.containsKey(uuidStr) ? Utils.attributes.get(uuidStr) : "Unknown Service"));
                        Log.d(TAG, "BluetoothGattCharacteristic uuid=" + uuidStr);
                    }
                    Log.d(TAG, "===========================================================================");
                }
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
        device = bluetoothAdapter.getRemoteDevice("C9:C4:AF:A1:D4:BE");
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
    }

    public static byte[] str2Byte(String hexStr) {
        int b = hexStr.length() % 2;
        if (b != 0) {
            hexStr = "0" + hexStr;
        }
        String[] a = new String[hexStr.length() / 2];
        byte[] bytes = new byte[hexStr.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            a[i] = hexStr.substring(2 * i, 2 * i + 2);
        }
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(a[i], 16);
        }
        return bytes;
    }
}
