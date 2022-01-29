package org.jzs.mybaseapp.section.otherdemo.nrf

import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.jzs.mybaseapp.R
import org.jzs.mybaseapp.common.BindingAdapter.BaseBindingAdapter
import org.jzs.mybaseapp.common.BindingAdapter.BaseBindingVH
import org.jzs.mybaseapp.common.livedata.LiveDataBus
import org.jzs.mybaseapp.databinding.ActivityNrfConnectBinding
import org.jzs.mybaseapp.databinding.ItemBluetoothBinding
import kotlin.math.log

/**
 * @author Jzs created 2017/8/2
 */
class nRFConnectActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityNrfConnectBinding
    lateinit var bluetoothService: BluetoothService
    lateinit var mAdapter: BaseBindingAdapter<BluetoothDevice, ItemBluetoothBinding>
    lateinit var mUnBindAdapter: BaseBindingAdapter<BluetoothDevice, ItemBluetoothBinding>
    private val unbindDevices = arrayListOf<BluetoothDevice>() // 用于存放未配对蓝牙设备
    private val bindDevices = arrayListOf<BluetoothDevice>() // 用于存放已配对蓝牙设备


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "蓝牙"
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_nrf_connect)
        bluetoothService = BluetoothService(this)
        initView()
        initLiveData()
        bluetoothService.getBindDevices()
    }

    private fun initView() {
        if (bluetoothService.isOpen) {
            mBinding.btnOpen.text = "关闭蓝牙"
        } else {
            mBinding.btnOpen.text = "打开蓝牙"
        }
        mBinding.btnFinish.setOnClickListener { finish() }
        mBinding.btnOpen.setOnClickListener {
            if (bluetoothService.isOpen) {
                bluetoothService.closeBluetooth()
                mBinding.btnOpen.text = "打开蓝牙"
            } else {
                bluetoothService.openBluetooth(this)
                mBinding.btnOpen.text = "关闭蓝牙"
            }
        }
        mBinding.btnSearch.setOnClickListener {
            Log.e("ss", "ss")
            if (!bluetoothService.isOpen) {
                Log.e("ss", "蓝牙没开")
                return@setOnClickListener
            }
            bindDevices.clear()
            unbindDevices.clear()
            mAdapter.notifyDataSetChanged()
            mUnBindAdapter.notifyDataSetChanged()
            bluetoothService.searchDevices()
        }

        mBinding.tvGet.setOnClickListener {
            bluetoothService.getConnectedDevices()
        }

        mUnBindAdapter = object :
            BaseBindingAdapter<BluetoothDevice, ItemBluetoothBinding>(this, unbindDevices, R.layout.item_bluetooth) {
            override fun onBindViewHolder(holder: BaseBindingVH<ItemBluetoothBinding>, position: Int) {
                //★super一定不要删除
                super.onBindViewHolder(holder, position)
                val binding = holder.binding
                val item = unbindDevices[position]
                binding.tvName.text = item.name + " " + item.address
                binding.tvName.setOnClickListener {
//                    bluetoothService.bindDevice(item.address)
                    val intent = Intent(this@nRFConnectActivity, DetailActivity::class.java)
                    intent.putExtra("address", item.address)
                    startActivity(intent)
                }
            }
        }
        mBinding.lvUnbind.adapter = mUnBindAdapter

        mAdapter = object : BaseBindingAdapter<BluetoothDevice, ItemBluetoothBinding>(
            this, bindDevices, R.layout.item_bluetooth
        ) {
            override fun onBindViewHolder(holder: BaseBindingVH<ItemBluetoothBinding>, position: Int) {
                //★super一定不要删除
                super.onBindViewHolder(holder, position)
                val binding = holder.binding
                val item = bindDevices[position]
                binding.tvName.text = item.name + " " + item.address
                binding.tvName.setOnClickListener {
                    val intent = Intent(this@nRFConnectActivity, DetailActivity::class.java)
                    intent.putExtra("address", item.address)
                    startActivity(intent)
                }
            }
        }
        mBinding.lvBind.adapter = mAdapter
    }

    fun initLiveData() {
        LiveDataBus.get().with("bindList")
            .observe(this, {
                val device = it as BluetoothDevice
                bindDevices.add(device)
                mAdapter.notifyItemChanged(bindDevices.size - 1)
            })
        LiveDataBus.get().with("getBindDevices")
            .observe(this, {
                bindDevices.addAll(it as List<BluetoothDevice>)
                mAdapter.notifyDataSetChanged()
            })
        LiveDataBus.get().with("unBindList")
            .observe(this, {
                val device = it as BluetoothDevice
                unbindDevices.add(device)
                mUnBindAdapter.notifyItemChanged(unbindDevices.size - 1)
            })
    }
}