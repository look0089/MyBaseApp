package org.jzs.mybaseapp

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import com.ximalaya.ting.android.opensdk.constants.ConstantsOpenSdk
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import org.jzs.mybaseapp.common.system.AppConfig
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
}