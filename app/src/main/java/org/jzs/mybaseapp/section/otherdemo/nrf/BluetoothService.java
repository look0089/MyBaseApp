package org.jzs.mybaseapp.section.otherdemo.nrf;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.IccOpenLogicalChannelResponse;
import android.util.Log;

import org.jzs.mybaseapp.common.livedata.LiveDataBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BluetoothService {
    private Context context = null;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private ArrayList<BluetoothDevice> unbondDevices = new ArrayList<>();// 用于存放未配对蓝牙设备
    private ArrayList<BluetoothDevice> bondDevices = new ArrayList<>();// 用于存放已配对蓝牙设备

    public BluetoothService(Context context) {
        this.context = context;
        this.initIntentFilter();
    }

    // 设置广播信息过滤
    private void initIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        // 注册广播接收器，接收并处理搜索结果
        context.registerReceiver(receiver, intentFilter);
    }


    public void getConnectedDevices() {
        bluetoothAdapter.getProfileProxy(context, new BluetoothProfile.ServiceListener() {
            @Override
            public void onServiceConnected(int profile, BluetoothProfile proxy) {
                List<BluetoothDevice> mDevices = proxy.getConnectedDevices();
                if (mDevices != null && mDevices.size() > 0) {
                    for (BluetoothDevice device : mDevices) {
                        if (device != null) {
                            Log.e("ss", "device name: " + device.getName());
                        }
                    }
                }
            }

            @Override
            public void onServiceDisconnected(int profile) {

            }
        }, BluetoothProfile.HEADSET);
    }

    /**
     * 添加已绑定蓝牙设备到ListView
     */
    private void addBondDevicesToListView() {
//        BluetoothDevice device = bondDevices.get(position);
//        Intent intent = new Intent();
//        intent.setClassName(context, "org.jzs.mybaseapp.section.otherdemo.bluetooth.PrintDataActivity");
//        intent.putExtra("deviceAddress", device.getAddress());
//        context.startActivity(intent);
    }

    /**
     * 添加未绑定蓝牙设备到ListView
     */
    private void addUnbondDevicesToListView() {
//                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//                        try {
//                            Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
//                            createBondMethod.invoke(unbondDevices.get(position));
//                            // 将绑定好的设备添加的已绑定list集合
//                            bondDevices.add(unbondDevices.get(position));
//                            // 将绑定好的设备从未绑定list集合中移除
//                            unbondDevices.remove(arg2);
//                            addBondDevicesToListView();
//                            addUnbondDevicesToListView();
//                        } catch (Exception e) {
//                            Toast.makeText(context, "配对失败！", Toast.LENGTH_SHORT).show();
//                        }
//                    }
    }


    /**
     * 打开蓝牙
     */
    public void openBluetooth(Activity activity) {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableBtIntent, 1);
    }

    /**
     * 关闭蓝牙
     */
    public void closeBluetooth() {
        this.bluetoothAdapter.disable();
    }

    /**
     * 判断蓝牙是否打开
     *
     * @return boolean
     */
    public boolean isOpen() {
        return this.bluetoothAdapter.isEnabled();
    }


    /**
     * 获取绑定设备
     */
    public void getBindDevices() {
        List<BluetoothDevice> list = new ArrayList<>(bluetoothAdapter.getBondedDevices());
        Log.e("ss", list.size() + "");
        LiveDataBus.get().with("getBindDevices").setValue(list);
    }

    /**
     * 搜索蓝牙设备
     */
    public void searchDevices() {
        this.bondDevices.clear();
        this.unbondDevices.clear();

        // 寻找蓝牙设备，android会将查找到的设备以广播形式发出去
        this.bluetoothAdapter.startDiscovery();
    }

    /**
     * 绑定设备
     */
    public void bindDevice(String address) {
        bluetoothAdapter.cancelDiscovery();
        BluetoothDevice mDevice = bluetoothAdapter.getRemoteDevice(address);
        mDevice.setPin("psw".getBytes());
        mDevice.createBond();
    }

    /**
     * 添加未绑定蓝牙设备到list集合
     *
     * @param device
     */
    public void addUnbindDevices(BluetoothDevice device) {
        System.out.println("未绑定设备名称：" + device.getName());
        if (!this.unbondDevices.contains(device)) {
            this.unbondDevices.add(device);
        }
    }

    /**
     * 添加已绑定蓝牙设备到list集合
     *
     * @param device
     */
    public void addBandDevices(BluetoothDevice device) {
        System.out.println("已绑定设备名称：" + device.getName());
        if (!this.bondDevices.contains(device)) {
            this.bondDevices.add(device);
        }
    }

    /**
     * 蓝牙广播接收器
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("ss", "onReceive" + intent.getAction());
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //判断是否已绑定
                Log.e("ss", "getBondState" + device.getBondState());

                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
//                    addBandDevices(device);
                    LiveDataBus.get().with("bindList").setValue(device);
                } else {
//                    addUnbondDevices(device);
                    LiveDataBus.get().with("unBindList").setValue(device);
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                System.out.println("设备搜索完毕");
            }
            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                System.out.println("ACTION_BOND_STATE_CHANGED");
            }
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
                    System.out.println("--------打开蓝牙-----------");
                } else if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                    System.out.println("--------关闭蓝牙-----------");
                }
            }
        }
    };
}