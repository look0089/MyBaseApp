package org.jzs.skutils.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.WindowManager

/**
 * 屏幕相关的辅助类
 */
class ScreenUtils private constructor() {

    init {
        throw UnsupportedOperationException("cannot be instantiated")
    }

    companion object {

        fun getScreenDensity(context: Context): Float {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val outMetrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(outMetrics)
            return outMetrics.density
        }

        /**
         * 获得屏幕宽度
         *
         * @param context
         * @return
         */
        fun getScreenWidth(context: Context): Int {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val outMetrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(outMetrics)
            return outMetrics.widthPixels
        }

        /**
         * 获得屏幕高度
         *
         * @param context
         * @return
         */
        fun getScreenHeight(context: Context): Int {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val outMetrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(outMetrics)
            return outMetrics.heightPixels
        }

        /**
         * 获得状态栏的高度
         *
         * @param context
         * @return
         */
        fun getStatusHeight(context: Context): Int {

            var statusHeight = -1
            try {
                val clazz = Class.forName("com.android.internal.R\$dimen")
                val `object` = clazz.newInstance()
                val height = Integer.parseInt(clazz.getField("status_bar_height").get(`object`).toString())
                statusHeight = context.resources.getDimensionPixelSize(height)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return statusHeight
        }

        /**
         * 获取当前屏幕截图，包含状态栏
         *
         * @param activity
         * @return
         */
        fun snapShotWithStatusBar(activity: Activity): Bitmap? {
            val view = activity.window.decorView
            view.isDrawingCacheEnabled = true
            view.buildDrawingCache()
            val bmp = view.drawingCache
            val width = getScreenWidth(activity)
            val height = getScreenHeight(activity)
            var bp: Bitmap? = null
            bp = Bitmap.createBitmap(bmp, 0, 0, width, height)
            view.destroyDrawingCache()
            return bp

        }

        /**
         * 获取当前屏幕截图，不包含状态栏
         *
         * @param activity
         * @return
         */
        fun snapShotWithoutStatusBar(activity: Activity): Bitmap? {
            val view = activity.window.decorView
            view.isDrawingCacheEnabled = true
            view.buildDrawingCache()
            val bmp = view.drawingCache
            val frame = Rect()
            activity.window.decorView.getWindowVisibleDisplayFrame(frame)
            val statusBarHeight = frame.top

            val width = getScreenWidth(activity)
            val height = getScreenHeight(activity)
            var bp: Bitmap? = null
            bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight)
            view.destroyDrawingCache()
            return bp

        }

        /**
         * 设置控件宽高
         */
        fun setParams(context: Context, v: View, widthx: Double, heightx: Double) {
            val screenWidth = getScreenWidth(context)
            val params = v.layoutParams
            params.width = (screenWidth / widthx).toInt()
            params.height = (screenWidth / heightx).toInt()
            v.layoutParams = params
        }

        /**
         * 设置控件宽高
         * 以屏幕宽度为准，根据比例设置图片
         * w期望的宽
         * h期望的高
         * 如720*320
         * 就输入w720 h320
         */
        fun setCustomParams(context: Context, v: View, w: Double, h: Double) {
            val screenWidth = getScreenWidth(context)
            val scale = w / h
            val params = v.layoutParams
            params.width = screenWidth
            params.height = (screenWidth / scale).toInt()
            v.layoutParams = params
        }

        /**
         * 获得控件
         *
         * @param child
         */
        fun measureView(child: View) {
            var params: LayoutParams? = child.layoutParams
            if (params == null) {
                params = LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT)
            }
            val childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, params.width)
            val lpHeight = params.height
            val childHeightSpec: Int
            if (lpHeight > 0) {
                childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY)
            } else {
                childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            }
            child.measure(childWidthSpec, childHeightSpec)
        }

        /**
         * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
         */
        fun dip2px(context: Context, dpValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }

        /**
         * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
         */
        fun px2dip(context: Context, pxValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (pxValue / scale + 0.5f).toInt()
        }
    }
}
