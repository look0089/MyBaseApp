package org.jzs.mybaseapp.section.demo.map

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import org.jzs.mybaseapp.R
import org.jzs.mybaseapp.common.utils.ToastUtils
import org.jzs.mybaseapp.databinding.MapNavagationSheetBinding
import java.net.URISyntaxException


/**
 * Created by Jzs on 2018/7/27.
 */
class BottomPopDialog(context: Context?) : Dialog(context!!, R.style.custom_dialog) {

    lateinit var binding: MapNavagationSheetBinding

    var LAT: String = "39.9"
    var LON: String = "116.4"

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.map_navagation_sheet, null, false)
        setContentView(binding.root)

        //百度地图
        binding.baiduBtn.setOnClickListener {
            if (isAppInstalled(context, "com.baidu.BaiduMap")) {//传入指定应用包名
                try {
                    val intent = Intent.getIntent("intent://map/direction?" +
                            "destination=latlng:" + LAT + "," + LON + "|name:我的目的地" +    //终点
                            "&mode=driving&" +     //导航路线方式
                            "&src=appname#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end")
                    context.startActivity(intent) //启动调用
                } catch (e: URISyntaxException) {
                    e.printStackTrace()
                }
            } else {//未安装
                ToastUtils.showToast("您尚未安装百度地图")
            }
            dismiss()
        }

        //高德地图
        binding.gaodeBtn.setOnClickListener {
            if (isAppInstalled(context, "com.autonavi.minimap")) {
                try {
                    var intent = Intent.getIntent("androidamap://navi?sourceApplication=MyBaseApp&poiname=我的目的地&lat=" + LAT + "&lon=" + LON + "&dev=0");
                    context.startActivity(intent);
                } catch (e: URISyntaxException) {
                    e.printStackTrace(); }
            } else {
                ToastUtils.showToast("您尚未安装高德地图")
                var uri = Uri.parse("market://details?id=com.autonavi.minimap")
                var intent = Intent(Intent.ACTION_VIEW, uri)
                context.startActivity(intent)
            }
            dismiss()
        }

        //腾讯地图
        binding.tencentBtn.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.addCategory(Intent.CATEGORY_DEFAULT)

            //将功能Scheme以URI的方式传入data
            val uri = Uri.parse("qqmap://map/routeplan?type=drive&to=我的目的地&tocoord=" + LAT + "," + LON)
            intent.data = uri
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                //启动该页面即可
                context.startActivity(intent)
            } else {
                ToastUtils.showToast("您尚未安装腾讯地图")
            }
            dismiss()
        }

        binding.cancelBtn2.setOnClickListener {
            dismiss()
        }
    }

    fun isAppInstalled(context: Context, packageName: String): Boolean {
        val packageManager = context.packageManager
        //取得所有的PackageInfo
        val pinfo = packageManager.getInstalledPackages(0)
        val pName = arrayListOf<String>()
        if (pinfo != null) {
            for (i in pinfo.indices) {
                val pn = pinfo[i].packageName
                pName.add(pn)
            }
        }
        //判断包名是否在系统包名列表中
        return pName.contains(packageName)
    }
}


