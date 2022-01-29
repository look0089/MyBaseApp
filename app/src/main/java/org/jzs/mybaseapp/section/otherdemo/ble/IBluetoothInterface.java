package org.jzs.mybaseapp.section.otherdemo.ble;


import android.bluetooth.BluetoothAdapter;


/**
 * Created by Pencilso on 2017/4/20.
 */
public interface IBluetoothInterface {
    /**
     * @param notifyListener 添加监听事件
     * @param <T>            BlueNotifyListener监听回调接口
     */
    <T extends BlueNotifyListener> void addNotifyListener(T notifyListener);

    /**
     * @param notifyListener 删除监听接口
     */
    void removeNotifyListener(BlueNotifyListener notifyListener);

    /**
     * 系统是否开启了蓝牙
     *
     * @return
     */
    boolean isEnable();

    /**
     * 打开或者关闭系统蓝牙
     *
     * @param enable
     */
    void enableBluetooth(boolean enable);

    /**
     * 启动扫描
     *
     * @param callback
     */
    void startScanner(BluetoothAdapter.LeScanCallback callback);

    /**
     * 停止扫描
     *
     * @param callback
     */
    void stopScanner(BluetoothAdapter.LeScanCallback callback);

    void onDestroy();

    /**
     * 连接设备
     *
     * @param address
     */
    void connectDevices(String address);

    /**
     * 向蓝牙写出数据
     *
     * @param data
     * @return
     */
    public boolean writeBluetoothData(String data);
}