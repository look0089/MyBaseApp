package org.jzs.mybaseapp.section.demo

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import org.jzs.mybaseapp.R
import org.jzs.mybaseapp.common.system.AppConfig
import org.jzs.mybaseapp.common.utils.AppLog
import org.jzs.mybaseapp.common.utils.GlideUtils
import org.jzs.mybaseapp.common.utils.glide.GlideCatchUtil
import org.jzs.mybaseapp.databinding.ActivityGlideBinding


class GlideActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityGlideBinding

    val bigPic: String = "http://img1.3lian.com/2015/a1/34/d/119.jpg"
    val baiduLogo: String = "https://www.baidu.com/img/bd_logo1.png"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_glide)
        initOnClick()
        mBinding.btn.text = "缓存大小:" + GlideCatchUtil.getInstance().getCacheSize(this@GlideActivity)

        AppLog.e(AppConfig.APP_SDCARD_DIR)
        AppLog.e(getCacheDir().toString() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR)
    }

    fun ivClick(v: View) {
        if (Build.VERSION.SDK_INT < 21) {
            Toast.makeText(this@GlideActivity, "21+ only, keep out", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(this@GlideActivity, ActivityTransitionToActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@GlideActivity, v, "transition")
            startActivity(intent, options.toBundle())
        }
    }

    fun initOnClick() {
        //淡出淡入
        mBinding.btn1.setOnClickListener {
            GlideUtils.setCrossFadeImage(bigPic, mBinding.iv1)
        }
        //无动画
        mBinding.btn2.setOnClickListener {
            GlideUtils.setImage(bigPic, mBinding.iv1)
        }
        //加载监听
        mBinding.btn3.setOnClickListener {
        }
        //45°圆角
        mBinding.btn4.setOnClickListener {
            GlideUtils.setRoundedCornersImage(bigPic, mBinding.iv1, 180)
        }
        //圆形头像
        mBinding.btn5.setOnClickListener {
            GlideUtils.setCircleCropImage(bigPic, mBinding.iv1)
        }
        //加载大图
        mBinding.btn6.setOnClickListener {
        }
        //加载失败
        mBinding.btn7.setOnClickListener {
            GlideUtils.setCircleCropImage("12312312", mBinding.iv1)
        }
        mBinding.btn.setOnClickListener {
            GlideCatchUtil.getInstance().clearCacheDiskSelf()
            mBinding.btn.setText(GlideCatchUtil.getInstance().getCacheSize())
        }
    }
}
