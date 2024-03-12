package org.jzs.mybaseapp.section.otherdemo.nrf

import android.bluetooth.*
import android.bluetooth.BluetoothAdapter.LeScanCallback
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.telephony.IccOpenLogicalChannelResponse
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.delay
import org.jzs.mybaseapp.R
import org.jzs.mybaseapp.common.livedata.LiveDataBus
import org.jzs.mybaseapp.common.utils.AsyncUtils
import org.jzs.mybaseapp.databinding.ActivityConnectDetailBinding
import org.jzs.mybaseapp.section.demo.test.HomeViewModel
import org.jzs.mybaseapp.section.otherdemo.xinzhongxin.bletester.DeviceConnect
import org.jzs.mybaseapp.section.otherdemo.xinzhongxin.utils.DateUtil
import org.jzs.mybaseapp.section.otherdemo.xinzhongxin.utils.Utils
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {
    companion object {
        const val TAG = "DetailActivity"
        private val serUUID = UUID.fromString("0000fe60-0000-1000-8000-00805f9b34fb")
        private val writeUUID = UUID.fromString("0000fe61-0000-1000-8000-00805f9b34fb")
        private val readUUID = UUID.fromString("0000fe62-0000-1000-8000-00805f9b34fb")
    }

    lateinit var mBinding: ActivityConnectDetailBinding
    var deviceAddress = "EF:AF:2C:F2:B1:F2"
    var bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    lateinit var device: BluetoothDevice
    lateinit var mBluetoothGatt: BluetoothGatt
    lateinit var mLeScanCallback: LeScanCallback
    var comNum = 0
    private val adminViewModel: HomeViewModel by lazy { ViewModelProvider(this).get(HomeViewModel::class.java) }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.title = "蓝牙打印"
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_connect_detail)
        intent.getStringExtra(DeviceConnect.EXTRAS_DEVICE_ADDRESS)?.let {
            deviceAddress = it
        }
        device = bluetoothAdapter.getRemoteDevice(deviceAddress)!!
        mBinding.tvName.text = "设备名称:" + device.getName() + "\n" + "设备地址:" + device.getAddress()
        mBinding.btnConnect.setOnClickListener {
            comNum = 0
            mBinding.btnConnect.text = "发送中.."
            mBinding.btnConnect.setTextColor(Color.GRAY)
            device = bluetoothAdapter.getRemoteDevice(deviceAddress)
            mBinding.tvName.text = "设备名称:" + device.getName() + "\n" + "设备地址:" + device.getAddress()
            bluetoothAdapter.cancelDiscovery()
            AsyncUtils.doAsync(this) { connection() }
        }
        mBinding.btnConnect1.setOnClickListener { v: View? ->
            comNum = 1
            mBinding.btnConnect1.text = "发送中.."
            mBinding.btnConnect1.setTextColor(Color.GRAY)
            bluetoothAdapter.cancelDiscovery()
            AsyncUtils.doAsync(this) { connection() }
        }
        mBinding.btnSend.setOnClickListener {
            loopSend()
        }
        mBinding.btnSendMm.setOnClickListener {
            loopSendMM()
        }
        LiveDataBus.get().with("GATT_SUCCESS", Boolean::class.java).observe(this) { b: Boolean? ->
            Log.e(TAG, "LiveDataBus GATT_SUCCESS")
            val gattChar = mBluetoothGatt.getService(serUUID).getCharacteristic(readUUID)
            mBluetoothGatt.readCharacteristic(gattChar)
            mBluetoothGatt.setCharacteristicNotification(gattChar, true)
            val gattCharw = mBluetoothGatt.getService(serUUID).getCharacteristic(writeUUID)
            when (comNum) {
                0 -> gattCharw.setValue("01")
                1 -> gattCharw.setValue(mBinding.etCommand.text.toString())
                else -> {
                }
            }
            //            gattCharw.setValue("01");
//                    gattCharw.setValue(str2Byte("01"));
            AsyncUtils.doAsync(this) {
                mBluetoothGatt.writeCharacteristic(gattCharw)
                it.postDelayed({ disconnect() }, 1500)
            }
            mBinding.btnConnect.text = "亮灯"
            mBinding.btnConnect.setTextColor(Color.BLACK)
            mBinding.btnConnect1.text = "发送"
            mBinding.btnConnect1.setTextColor(Color.BLACK)
        }
        mLeScanCallback = LeScanCallback { device, rssi, scanRecord ->
            if (deviceAddress == device.address) {
                mBinding.tvRssi.text = "信号强度:$rssi"
                Log.e("onLeScan", device.address + ":" + rssi + "")
            }
        }
        adminViewModel.launch {
            while (true) {
                delay(1000)
                var time = DateUtil.getCurrentDatatime()
                mBinding.tvTime.text = time
            }
        }
//        requestLocation(this) { Thread { bluetoothAdapter.startLeScan(mLeScanCallback) }.start() }
    }

    override fun onDestroy() {
        super.onDestroy()
        loopSS = false
        bluetoothAdapter.stopLeScan(mLeScanCallback)
        bluetoothAdapter.cancelDiscovery()
    }

    private val mGattCallback: BluetoothGattCallback = object : BluetoothGattCallback() {
        /**
         * 连接状态改变回调
         * @param gatt
         * @param status
         * @param newState
         */
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.e(TAG, "Connected to GATT server.")
                //                Log.i(TAG, "Attempting to start service discovery:" + mBluetoothGatt.discoverServices());
                //阅读连接的远程设备的RSSI。
                gatt.readRemoteRssi()
                // 发现远程设备提供的服务及其特性和描述符
                gatt.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            }
        }

        /**
         * 当远程设备的远程服务列表，特征和描述符已被更新，即已发现新服务时，调用回调。表示可以与之通信了。
         * @param gatt
         * @param status
         */
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e(TAG, "onServicesDiscovered")
                val bluetoothGattServices = gatt.services
                //发现服务是可以在这里查找支持的所有服务
                for (ser in bluetoothGattServices) {
                    val uuid = ser.uuid.toString()
                    Log.d(
                        TAG,
                        "BluetoothGattService Name=" + if (Utils.attributes.containsKey(uuid)) Utils.attributes[uuid] else "Unknown Service"
                    )
                    Log.d(TAG, "BluetoothGattService uuid=$uuid")
                    val gattService = mBluetoothGatt.getService(ser.uuid)
                    mBluetoothGatt.readRemoteRssi()
                    val gattchars = gattService.characteristics
                    for (c in gattchars) {
                        val currentCharData = HashMap<String, String?>()
                        val uuidStr = c.uuid.toString()
                        currentCharData["Name"] =
                            if (Utils.attributes.containsKey(uuidStr)) Utils.attributes[uuidStr] else "Unknown Characteristics"
                        Log.d(
                            TAG,
                            "BluetoothGattCharacteristic Name=" + if (Utils.attributes.containsKey(uuidStr)) Utils.attributes[uuidStr] else "Unknown Service"
                        )
                        Log.d(TAG, "BluetoothGattCharacteristic uuid=$uuidStr")
                    }
                    Log.d(TAG, "===========================================================================")
                }
                Log.e(TAG, "GATT_SUCCESS")
//                LiveDataBus.get().with("GATT_SUCCESS").postValue(true)
                val msg = Message()
                msg.what = 0
                mHandler.sendMessage(msg)
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e(TAG, "onCharacteristicRead")
                val data = characteristic.value
                val stringData = characteristic.getStringValue(0)
                if (data != null && data.size > 0) {
                    val stringBuilder = StringBuilder(data.size)
                    for (byteChar in data) {
                        stringBuilder.append(String.format("%X", byteChar))
                    }
                    if (stringData != null) {
                        Log.e(TAG, stringData)
                    } else {
                        Log.e(TAG, "characteristic.getStringValue is null")
                    }
                }
            }
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            Log.e("tag", "onCharacteristicChanged")
            val data = characteristic.value
            val stringData = characteristic.getStringValue(0)
            if (data != null && data.size > 0) {
                val stringBuilder = StringBuilder(data.size)
                for (byteChar in data) {
                    stringBuilder.append(String.format("%X", byteChar))
                }
                if (stringData != null) {
                    Log.e(TAG, stringData)
                } else {
                    Log.e(TAG, "characteristic.getStringValue is null")
                }
            }
        }
    }

    fun connection() {
        device = bluetoothAdapter.getRemoteDevice(deviceAddress)
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback)
    }

    fun disconnect() {
        Log.e(TAG, "断开")
        mBluetoothGatt.disconnect()
        mBluetoothGatt.close()
    }

    fun str2Byte(hexStr: String): ByteArray {
        var hexStr = hexStr
        val b = hexStr.length % 2
        if (b != 0) {
            hexStr = "0$hexStr"
        }
        val a = arrayOfNulls<String>(hexStr.length / 2)
        val bytes = ByteArray(hexStr.length / 2)
        for (i in bytes.indices) {
            a[i] = hexStr.substring(2 * i, 2 * i + 2)
        }
        for (i in bytes.indices) {
            bytes[i] = a[i]!!.toInt(16).toByte()
        }
        return bytes
    }

    val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 0) {
                mBinding.tvSendTime.text = "发送次数:" + sendTime
                AsyncUtils.doAsync(this) {
                    val gattChar = mBluetoothGatt.getService(serUUID).getCharacteristic(readUUID)
                    mBluetoothGatt.readCharacteristic(gattChar)
                    mBluetoothGatt.setCharacteristicNotification(gattChar, true)
                    val gattCharw = mBluetoothGatt.getService(serUUID).getCharacteristic(writeUUID)
                    gattCharw.setValue("01")
                    mBluetoothGatt.writeCharacteristic(gattCharw)
                    it.postDelayed({ disconnect() }, 1500)
                }
                mBinding.btnConnect.text = "亮灯"
                mBinding.btnConnect.setTextColor(Color.BLACK)
                mBinding.btnConnect1.text = "发送"
                mBinding.btnConnect1.setTextColor(Color.BLACK)
            }
        }
    }
    var sendTime = 0
    var lastMin = ""
    var loopSS = true
    var loopMM = true

    fun loopSend() {
        loopMM = false
        loopSS = true
        sendTime = 0
        val sdf = SimpleDateFormat("ss")
        Thread {
            synchronized(DetailActivity::class.java) {
                while (loopSS) {
                    Thread.sleep(1000)
                    val timestr = sdf.format(System.currentTimeMillis())
                    var time = DateUtil.getCurrentDatatime()
                    Log.e(TAG, time)
                    if (timestr.toInt() % 10 == 0) {
                        if (lastMin == timestr) {
                            Log.e(TAG, "相等")
                            continue
                        }
                        lastMin = timestr
                        sendTime++

                        Log.e(TAG, "发送")
                        connection()
                    }
                }
            }
        }.start()
    }

    fun loopSendMM() {
        loopSS = false
        loopMM = true
        sendTime = 0
        val sdf = SimpleDateFormat("mm")
        Thread {
            synchronized(DetailActivity::class.java) {
                while (loopMM) {
                    Thread.sleep(1000)
                    val timestr = sdf.format(System.currentTimeMillis())
                    var time = DateUtil.getCurrentDatatime()
                    Log.e(TAG, time)
                    if (timestr.toInt() % 10 == 0) {
                        if (lastMin == timestr) {
                            Log.e(TAG, "相等")
                            continue
                        }
                        lastMin = timestr
                        sendTime++

                        Log.e(TAG, "发送")
                        connection()
                    }
                }
            }
        }.start()
    }
}