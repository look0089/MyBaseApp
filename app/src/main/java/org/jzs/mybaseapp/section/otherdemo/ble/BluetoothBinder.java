package org.jzs.mybaseapp.section.otherdemo.ble;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Build;
import android.os.Message;

import org.jzs.mybaseapp.section.otherdemo.nrf.UUIDManager;

import java.util.List;
import java.util.UUID;

import androidx.annotation.RequiresApi;

/**
 * Created by Pencilso on 2017/4/20.
 * 蓝牙操作的Binder
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BluetoothBinder extends BluetoothGattCallback implements IBluetoothInterface {
    private static BluetoothBinder bluetoothBinder;
    private final Activity bluetoothService;//service服务
    private final BluetoothAdapter adapter;//蓝牙的适配器
    private List<BlueNotifyListener> notifyList;//监听的集合
    private BluetoothManager bluetoothManager;//蓝牙管理者
    private BluetoothGattService gattService;//通道服务
    private BluetoothGatt bluetoothGatt;

    public static IBluetoothInterface getInstace() {
        return bluetoothBinder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    protected BluetoothBinder(Activity bluetoothService) {
        bluetoothBinder = this;
        this.bluetoothService = bluetoothService;
        bluetoothManager = (BluetoothManager) bluetoothService.getSystemService(Context.BLUETOOTH_SERVICE);
        adapter = bluetoothManager.getAdapter();
    }

    @Override
    public void onDestroy() {
        bluetoothBinder = null;
    }

    @Override
    public <T extends BlueNotifyListener> void addNotifyListener(T notifyListener) {
        if (notifyListener != null)
            notifyList.add(notifyListener);
    }

    @Override
    public void removeNotifyListener(BlueNotifyListener notifyListener) {
        if (notifyListener != null)
            notifyList.remove(notifyListener);
    }

    /**
     * 广播蓝牙监听消息
     * 因为蓝牙发送过来的消息 并不是处于主线程当中的
     * 所以如果直接对蓝牙的数据展示视图的话  会展示不了的 这里的话  封装到主线程当中遍历回调数据
     *
     * @param notify
     */
    public void traverseListener(Message notify) {
        NotifyThread notifyThread = new NotifyThread();//创建一个遍历线程
        notifyThread.setListeners(notifyList);
        notifyThread.setNotify(notify);
//        HandleUtils.getInstace().post(notifyThread);
    }

    /**
     * 系统的蓝牙是否已经打开
     *
     * @return
     */
    @Override
    public boolean isEnable() {
        return adapter.isEnabled();
    }

    @Override
    public void enableBluetooth(boolean enable) {
        if (enable)
            adapter.enable();
        else
            adapter.disable();
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    @Override
    public void startScanner(BluetoothAdapter.LeScanCallback callback) {
        adapter.startLeScan(callback);
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    @Override
    public void stopScanner(BluetoothAdapter.LeScanCallback callback) {
        adapter.stopLeScan(callback);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void connectDevices(String address) {
        BluetoothDevice remoteDevice = adapter.getRemoteDevice(address);
        BluetoothGatt bluetoothGatt = remoteDevice.connectGatt(bluetoothService, false, this);
    }

    /**
     * 蓝牙设备状态的监听
     *
     * @param gatt
     * @param status
     * @param newState 蓝牙的状态被改变
     */
    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);
        Message message = new Message();
        switch (newState) {//对蓝牙反馈的状态进行判断
            case BluetoothProfile.STATE_CONNECTED://已链接
                message.what = BlueCodeUtils.BLUETOOTH_STATE_CONNECT;
                gatt.discoverServices();
                break;
            case BluetoothProfile.STATE_DISCONNECTED://已断开
                message.what = BlueCodeUtils.BLUETOOTH_STATE_DISCONNECT;
                break;
        }
        traverseListener(message);
        /**
         * 这里还有一个需要注意的，比如说蓝牙设备上有一些通道是一些参数之类的信息，比如最常见的版本号。
         * 一般这种情况 版本号都是定死在某一个通道上，直接读取，也不需要发送指令的。
         * 如果遇到这种情况，一定要在发现服务之后 再去读取这种数据  不要一连接成功就去获取
         */
    }

    /**
     * 发现服务
     *
     * @param gatt
     * @param status
     */
    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);
        gattService = gatt.getService(UUID.fromString(UUIDManager.SERVICE_UUID));// 获取到服务的通道
        bluetoothGatt = gatt;
        //获取到Notify的Characteristic通道 这个根据协议来定  如果设备厂家给的协议不是Notify的话  就不用做以下操作了
        BluetoothGattCharacteristic notifyCharacteristic = gattService.getCharacteristic(UUID.fromString(UUIDManager.NOTIFY_UUID));
        BluetoothUtils.enableNotification(gatt, true, notifyCharacteristic);//注册Notify通知
    }

    /**
     * 向蓝牙写入数据
     *
     * @param data
     */
    @SuppressLint("NewApi")
    @Override
    public boolean writeBluetoothData(String data) {
        if (bluetoothService == null) {
            return false;
        }
        BluetoothGattCharacteristic writeCharact = gattService.getCharacteristic(UUID.fromString(UUIDManager.WRITE_UUID));
        bluetoothGatt.setCharacteristicNotification(writeCharact, true); // 设置监听
        // 当数据传递到蓝牙之后
        // 会回调BluetoothGattCallback里面的write方法
        writeCharact.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
        // 将需要传递的数据 打碎成16进制
        writeCharact.setValue(BluetoothUtils.getHexBytes(data));
        return bluetoothGatt.writeCharacteristic(writeCharact);
    }

    /**
     * 蓝牙Notify推送过来的数据
     *
     * @param gatt
     * @param characteristic
     */
    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
        //如果推送的是十六进制的数据的写法
        String data = BluetoothUtils.bytesToHexString(characteristic.getValue()); // 将字节转化为String字符串
        Message message = new Message();
        message.what = BlueCodeUtils.BLUETOOTH_PUSH_MESSAGE;
        message.obj = data;
        traverseListener(message);//回调数据
//       String data =  characteristic.getStringValue(0); // 使用场景  例如某个通道里面静态的定死了某一个值，就用这种写法获取 直接获取到String类型的数据
    }

    /**
     * 这里有一个坑  一定要注意，如果设备返回数据用的不是Notify的话 一定要注意这个问题
     * 这个方法是 向蓝牙设备写出数据成功之后回调的方法，写出成功之后干嘛呢？ 主动去蓝牙获取数据，没错，自己主动去READ通道获取蓝牙数据
     * 如果用的是Notify的话 不用理会该方法   写出到蓝牙之后  等待Notify的监听 即onCharacteristicChanged方法回调。
     *
     * @param gatt
     * @param characteristic
     * @param status
     */
    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicWrite(gatt, characteristic, status);
        if (status == BluetoothGatt.GATT_SUCCESS) { //写出成功 接下来  该去读取蓝牙设备的数据了
            //这里的READUUID 应该是READ通道的UUID 不过我这边的设备没有用到READ通道  所以我这里就注释了 具体使用 视情况而定
//            BluetoothGattCharacteristic readCharact = gattService.getCharacteristic(UUID.fromString(READUUID));
//            gatt.readCharacteristic(readCharact);
        }
    }

    /**
     * 调用读取READ通道后返回的数据回调
     * 比如说 在onCharacteristicWrite里面调用 gatt.readCharacteristic(readCharact);之后 会回调该方法
     *
     * @param gatt
     * @param characteristic
     * @param status
     */
    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicRead(gatt, characteristic, status);
        //如果推送的是十六进制的数据的写法
        String data = BluetoothUtils.bytesToHexString(characteristic.getValue()); // 将字节转化为String字符串
        Message message = new Message();
        message.what = BlueCodeUtils.BLUETOOTH_PUSH_MESSAGE;
        message.obj = data;
        traverseListener(message);//回调数据
//       String data =  characteristic.getStringValue(0); // 使用场景  例如某个通道里面静态的定死了某一个值，就用这种写法获取 直接获取到String类型的数据
    }
}