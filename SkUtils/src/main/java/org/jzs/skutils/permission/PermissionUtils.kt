package org.jzs.skutils.permission

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity

import com.tbruyelle.rxpermissions.RxPermissions


@SuppressLint("StaticFieldLeak")
/**
 * @author Jzs created 2017/11/6 0006
 * 使用 rxPermissions 获得动态权限
 */

object PermissionUtils {

    lateinit var a: Activity

    /**
     * 自定义权限
     * 根据需求一次请求多个权限
     */
    @JvmStatic
    fun requestCustom(activity: Activity, l: PermissionListener) {
        RxPermissions(activity)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    if (granted!!) {
                        l.onSuccess()
                    } else {
                        PermissionDialog(a).show()
                    }
                }
    }

    /**
     * 请求相机权限
     */
    @JvmStatic
    fun requestCamera(a: Activity, listener: PermissionListener) {
        RxPermissions(a)
                .request(Manifest.permission.CAMERA)
                .subscribe { granted ->
                    if (granted!!) {
                        listener.onSuccess()
                    } else {
                        PermissionDialog(a).show()
                    }
                }
    }

    /**
     * 请求定位权限
     */
    @JvmStatic
    fun requestLocation(a: Activity, l: PermissionListener) {
        RxPermissions(a)
                .request(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe { granted ->
                    if (granted!!) {
                        l.onSuccess()
                    } else {
                        PermissionDialog(a).show()
                    }
                }
    }

    /**
     * 请求读写权限
     */
    @JvmStatic
    fun requestStorage(a: Activity, l: PermissionListener) {
        RxPermissions(a)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    if (granted!!) {
                        l.onSuccess()
                    } else {
                        PermissionDialog(a).show()
                    }
                }
    }

    /**
     * 请求电话权限
     */
    @JvmStatic
    fun requestPhone(a: Activity, l: PermissionListener) {
        RxPermissions(a)
                .request(Manifest.permission.CALL_PHONE)
                .subscribe { granted ->
                    if (granted!!) {
                        l.onSuccess()
                    } else {
                        PermissionDialog(a).show()
                    }
                }
    }

}

