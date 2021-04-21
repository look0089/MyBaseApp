package org.jzs.mybaseapp.section.otherdemo.eiface

import android.graphics.ImageFormat
import android.hardware.Camera
import android.util.Log
import android.view.SurfaceHolder

/**
 * created by LiGuang
 * on 2018/11/12
 */
class VideoUtil(private val surfaceHolder: SurfaceHolder, private val cameraId: Int) :
        SurfaceHolder.Callback,
        Camera.PreviewCallback, Camera.ErrorCallback {
    var mCamera: Camera? = null
    private var mData = ByteArray(460800)
    var isSyncFlag = false

    init {
        surfaceHolder.addCallback(this)
    }

    override fun onPreviewFrame(data: ByteArray, camera: Camera) {
        if (mCamera != null) {
            synchronized(this) {
                System.arraycopy(data, 0, mData, 0, mData.size)
                isSyncFlag = true
            }
            camera.addCallbackBuffer(data)
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        startPreview()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }


    override fun surfaceDestroyed(holder: SurfaceHolder) {

    }


    /**
     * 开始预览
     */
    private fun startPreview() {
        try {
            //SurfaceView初始化完成，开始相机预览
            Log.e("startPreview", "startPreview")
            mCamera = Camera.open(cameraId)
            val parameters = mCamera!!.parameters
//            parameters.pictureFormat = ImageFormat.NV21;
            parameters.previewFormat = ImageFormat.NV21;
            parameters.setPreviewSize(640, 480)
            if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mCamera!!.setDisplayOrientation(90)
            }
            if (cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                mCamera!!.setDisplayOrientation(90)//顺时针旋转
            }

            mCamera!!.parameters = parameters
            mCamera!!.setPreviewDisplay(surfaceHolder)

            mCamera!!.setPreviewCallback(this)
            mCamera!!.setErrorCallback(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun restartPreview() {
        mCamera!!.startPreview()
    }

    /**
     * 停止预览
     */
    fun stopPreview() {
        if (mCamera != null) {
            mCamera!!.stopPreview()
            mCamera!!.setPreviewCallback(null)
            mCamera!!.release()
            isSyncFlag = false
            mCamera = null
        }
    }


    fun getData(): ByteArray? {
        return mData
    }

    override fun onError(error: Int, camera: Camera) {
        Log.d("zheng", "camera error:$error")
    }
}
