package org.jzs.mybaseapp

import android.app.ActivityManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.lahm.library.EasyProtectorLib
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import com.plattysoft.leonids.ParticleSystem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jzs.mybaseapp.common.utils.AppLog
import org.jzs.mybaseapp.common.utils.DateFormatUtils
import org.jzs.mybaseapp.common.utils.ToastUtils
import org.jzs.mybaseapp.databinding.ActivityMainBinding
import org.jzs.mybaseapp.section.demo.GlideActivity
import org.jzs.mybaseapp.section.demo.curve.MPActivity
import org.jzs.mybaseapp.section.demo.map.MapActivity
import org.jzs.mybaseapp.section.demo.test.LiveDataActivity
import org.jzs.mybaseapp.section.demo.ximalaya.XimalayaActivity
import org.jzs.mybaseapp.section.live.LiveService
import org.jzs.mybaseapp.section.otherdemo.PackInfoActivity
import org.jzs.mybaseapp.section.otherdemo.coin.Coin2Activity
import org.jzs.mybaseapp.section.otherdemo.notification.NotifiActivity
import org.jzs.mybaseapp.section.otherdemo.video.gsy.PlayActivity
import org.jzs.mybaseapp.section.otherdemo.vpn.VPNActivity
import org.jzs.mybaseapp.section.otherdemo.waterpic.PhotoActivity
import org.jzs.mybaseapp.section.otherdemo.waterpic.ViewDragActivity
import org.jzs.mybaseapp.section.otherdemo.xinzhongxin.bletester.MainActivity
import org.jzs.mybaseapp.section.weightdemo.anime.AnimeActivity
import org.jzs.mybaseapp.section.weightdemo.largepic.LargePicActivity
import org.jzs.mybaseapp.section.weightdemo.permission.PermissionActivity
import org.jzs.mybaseapp.section.weightdemo.permission.PermissionUtils
import org.jzs.mybaseapp.section.weightdemo.photo.SelectPhotoActivity

/**
 * @author Jzs created 2017/8/2
 */
class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    lateinit var mBinding: ActivityMainBinding
    private var mLeakyContainer = arrayListOf<ByteArray>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        //开启保活service
        startService(Intent(this, LiveService::class.java))
        val checkIsRunningInEmulator = EasyProtectorLib.checkIsRunningInEmulator()
        mBinding.tvEum.text = "是否模拟器:$checkIsRunningInEmulator"
        initOnClick()
//      checkAppLive()
    }

    /**
     * 开启一个协程,无限运行打印日志,判断app是否存活
     */
    var time: Int = 0
    fun checkAppLive() {
        GlobalScope.launch {
            while (true) {
                delay(2000)
                time++
                if (time == 5) {
                    Log.e(TAG, time.toString() + "")
                }
            }
        }
    }

    private fun initOnClick() {
        //控件和动画
        mBinding.apply {
            //时间选择器
            btnTime.setOnClickListener {
                val displayList = mutableListOf<Int>()
                displayList.add(DateTimeConfig.YEAR)
                displayList.add(DateTimeConfig.MONTH)
                displayList.add(DateTimeConfig.DAY)
                displayList.add(DateTimeConfig.HOUR)
                displayList.add(DateTimeConfig.MIN)
                displayList.add(DateTimeConfig.SECOND)
                CardDatePickerDialog.builder(this@MainActivity)
                    .setTitle("选择开始时间")
                    .setDisplayType(displayList)
                    .setBackGroundModel(CardDatePickerDialog.CARD)
                    .setDefaultTime(System.currentTimeMillis())
                    .showBackNow(true)
                    .setWrapSelectorWheel(true)
                    .showDateLabel(true)
                    .showFocusDateInfo(true)
                    .setOnChoose("选择") {
                        ToastUtils.showToast(DateFormatUtils.timestampToDate(it, "yyyy-MM-dd HH:mm:ss"))
                    }
                    .setOnCancel("关闭") {
                    }.build().show()

            }
            //图片选择器
            btnSelectPhoto.setOnClickListener {
                startActivity(Intent(this@MainActivity, SelectPhotoActivity::class.java))
            }
            //例子效果
            btnLeonids.setOnClickListener {

//                ParticleSystem(this@MainActivity, 80, R.drawable.star_on, 10000)
//                    .setSpeedModuleAndAngleRange(0f, 0.3f, 0, 0)
//                    .setRotationSpeed(144f)
//                    .setAcceleration(0.00005f, 90)
//                    .emit(mBinding.btnLeonids, 8)

                ParticleSystem(this@MainActivity, 100, R.drawable.star_on, 3000)
                    .setSpeedModuleAndAngleRange(0.05f, 0.2f, 0, 360)
                    .setRotationSpeed(30f)
                    .setAcceleration(0f, 360)
                    .setFadeOut(3000)
                    .oneShot(mBinding.btnLeonids, 200)
            }
            //动画
            btnAnime.setOnClickListener {
                startActivity(Intent(this@MainActivity, AnimeActivity::class.java))
            }
            //大图
            btnLargePic.setOnClickListener {
                startActivity(Intent(this@MainActivity, LargePicActivity::class.java))
            }
            //获取权限
            mBinding.btnPermission.setOnClickListener {
                startActivity(Intent(this@MainActivity, PermissionActivity::class.java))
            }
        }
        //例子Demo
        mBinding.apply {
            //唤起外部地图
            btnMap.setOnClickListener {
                PermissionUtils.requestLocation(this@MainActivity) {
                    startActivity(Intent(this@MainActivity, MapActivity::class.java))
//                    startActivity(Intent(this@MainActivity, MapViewActivity::class.java))
                }
            }
            //mpchart曲线
            btnMpchart.setOnClickListener {
                startActivity(Intent(this@MainActivity, MPActivity::class.java))
            }
            //喜马拉雅SDK
            btnXimalaya.setOnClickListener {
                startActivity(Intent(this@MainActivity, XimalayaActivity::class.java))
            }
            //glide图片加载
            btnGlide.setOnClickListener {
                startActivity(Intent(this@MainActivity, GlideActivity::class.java))
            }
            btnLiveData.setOnClickListener {
                startActivity(Intent(this@MainActivity, LiveDataActivity::class.java))
            }
        }
        //其他功能
        mBinding.apply {
            //蓝牙
            btnBlue.setOnClickListener {
                startActivity(Intent(this@MainActivity, MainActivity::class.java))
            }
            //创建VPN
            btnVpn.setOnClickListener {
                startActivity(Intent(this@MainActivity, VPNActivity::class.java))
            }
            //获取通知栏内容
            btnSystemTitle.setOnClickListener {
                startActivity(Intent(this@MainActivity, NotifiActivity::class.java))
            }
            btnWaterpic.setOnClickListener {
                startActivity(Intent(this@MainActivity, PhotoActivity::class.java))
            }
            btnCamera.setOnClickListener {
                startActivity(Intent(this@MainActivity, ViewDragActivity::class.java))
            }
            btnCoin.setOnClickListener {
                startActivity(Intent(this@MainActivity, Coin2Activity::class.java))
            }
            btnPackinfo.setOnClickListener {
                startActivity(Intent(this@MainActivity, PackInfoActivity::class.java))
            }
            btnVideo.setOnClickListener {
                PermissionUtils.requestStorage(this@MainActivity) {
                    startActivity(Intent(this@MainActivity, PlayActivity::class.java))
                }
            }
            btnOom.setOnClickListener {
                val b = ByteArray(100 * 1000 * 1000)
                mLeakyContainer.add(b)


                val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
                val largeMemoryClass = activityManager.largeMemoryClass
                val memoryClass = activityManager.memoryClass
                val info = ActivityManager.MemoryInfo()
                activityManager.getMemoryInfo(info)
                AppLog.e("Memory", "largeMemoryClass = $largeMemoryClass")
                AppLog.e("Memory", "memoryClass = $memoryClass")
                AppLog.e("Memory", "availMem = " + info.availMem / 1024 / 1024 + "M")
                AppLog.e("Memory", "totalMem = " + info.totalMem / 1024 / 1024 + "M")
                AppLog.e("Memory", "lowMemory = " + info.lowMemory)
            }
        }
    }
}
