package org.jzs.mybaseapp.section.otherdemo.nrf

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.telephony.IccOpenLogicalChannelResponse
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.jzs.mybaseapp.R
import org.jzs.mybaseapp.databinding.ActivityConnectDetailBinding
import org.jzs.mybaseapp.section.weightdemo.permission.PermissionUtils
import java.util.*
import kotlin.math.log

class ConnectDetailActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityConnectDetailBinding
    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    lateinit var device: BluetoothDevice
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "蓝牙"
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_connect_detail)
        PermissionUtils.requestBlePermission(this) {
            intent.getStringExtra("address")?.let { deviceAddress ->
                device = bluetoothAdapter.getRemoteDevice(deviceAddress)
                mBinding.tvName.text = device.name + "\n" + device.address
            }
        }
//        device.uuids.forEach {
//            Log.e("ss", it.uuid.toString())
//        }
    }
}