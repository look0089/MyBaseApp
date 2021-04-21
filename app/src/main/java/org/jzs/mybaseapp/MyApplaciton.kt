package org.jzs.mybaseapp

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Environment
import com.wf.AssetFileManager
import com.wf.wffrdualcamapp
import com.wf.wffrjni
import com.wf.wffrjniID
import com.ximalaya.ting.android.opensdk.constants.ConstantsOpenSdk
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import org.jzs.mybaseapp.common.system.AppConfig
import org.jzs.mybaseapp.common.system.SharedPreferencesUtil
import org.jzs.mybaseapp.common.utils.AppManager
import org.jzs.mybaseapp.common.utils.GlobalExceptionHandler

/**
 * Created by Jzs on 2017/7/19.
 * 全局Applaciton
 */
class MyApplaciton : Application() {
    override fun onCreate() {
        super.onCreate()
        // 初始化参数
        instance = this
        AppConfig.init(this)
        // 全局异常捕获
        GlobalExceptionHandler.getInstance().register(this)
        initXimalaya()
//        wrr_init()
    }

    /**
     * 初始化喜马拉雅
     */
    private fun initXimalaya() {
        ConstantsOpenSdk.isDebug = true
        CommonRequest.getInstanse().init(this, "a603bf6d3b53ecc7fb89dafc2f0554a0")
        //打开https 请求设置
        CommonRequest.getInstanse().useHttps = true
    }

    /**
     * 退出应用程序
     */
    fun AppExit(context: Context) {
        try {
            AppManager.getInstance().finishAllActivity()
            val activityMgr = context
                    .getSystemService(ACTIVITY_SERVICE) as ActivityManager
            activityMgr.killBackgroundProcesses(context.packageName)
            // restartPackage(context.getPackageName());
            System.exit(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        const val TAG = "MyApplaciton"
        private var instance: MyApplaciton? = null

        @JvmStatic
        fun getInstance(): MyApplaciton? {
            return if (instance == null) {
                instance = MyApplaciton()
                instance
            } else {
                instance
            }
        }
    }

    /**
     * ei算法SDK初始化
     */
    fun wrr_init() {
        if (!AssetFileManager(this).checkFilesExist()) {
            AssetFileManager(this).copyFilesFromAssets() //copies files from assests to cache folder
        }
        wffrdualcamapp.finish_state = 1
        wffrjni.SetRecognitionThreshold(SharedPreferencesUtil.getSP().getInt("threshold", wffrjni.GetRecognitionThreshold().toInt()).toFloat())
        wffrdualcamapp.setAssetPath(AssetFileManager.wffrBasePath + "/")
        wffrjni.EnableImageSaveForDebugging(0)
        wffrjni.setSaveDetectedFaceFlag(1, Environment.getExternalStorageDirectory().absolutePath + "/dualcam/wffrdb")
//        wffrjni.saveEnrollImages(1)//启用或禁用将注册图像保存在数据库中
        wffrjniID.SetRecognitionThreshold(30f) //人证检测两图片相似度阀值设置 范围[30-100]默认70
        wffrjni.SetMaskFaceClassifyThreshold(0.75f) //口罩阀值初始化 值越高越严格
        wffrjni.SetSingleCamSpoofThreshold(-30.0f) // 活体检测阀值 值越高越严格
        wffrjni.SetDetectionAlgoType(3);
        wffrjni.SetDetectionOnlyMode(1)
        wffrjni.initialize(
                AssetFileManager.wffrBasePath + "/",
                640,
                480,
                640,
                0,
                wffrdualcamapp.recognitionSpoofing
        )
        wffrjni.Release()
        wffrjni.SetMinFaceDetectionSizePercent(5)
    }
}