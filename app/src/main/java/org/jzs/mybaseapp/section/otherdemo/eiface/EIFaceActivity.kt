package org.jzs.mybaseapp.section.otherdemo.eiface

import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.*
import org.jzs.mybaseapp.R
import org.jzs.mybaseapp.common.utils.ToastUtils.showToast
import org.jzs.mybaseapp.databinding.ActivityEifaceBinding
import com.wf.*
import org.jzs.mybaseapp.common.widget.LoadDialog
import java.util.*

/**
 * 主界面
 * @author ZyElite
 */
class EIFaceActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityEifaceBinding

    companion object {
        val camWidth = 640
        val camHeight = 480
        lateinit var backCameraData: ByteArray
        lateinit var frontCameraData: ByteArray

        //暂停人脸识别标识
        var isPause = false

        /**
         * app 在后台时,暂停循环方法
         */

        var is_screenshot = false
    }

    var isWhile = true
    lateinit var speaker: Speaker
    lateinit var videoUtilFront: VideoUtil
    lateinit var videoUtilBack: VideoUtil
    private val faceViewModel: FaceViewModel by lazy { ViewModelProvider(this).get(FaceViewModel::class.java) }
    var closecamera: TimerTask? = null
    var iscameraopen = false
    var data11: ByteArray? = null
    var data22: ByteArray? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_eiface)
        speaker = Speaker(this)
        videoUtilFront = VideoUtil(mBinding.frontView.holder, Camera.CameraInfo.CAMERA_FACING_FRONT)
        videoUtilBack = VideoUtil(mBinding.backView.holder, Camera.CameraInfo.CAMERA_FACING_BACK)
        mBinding.btnEnroll.setOnClickListener {
            is_screenshot = true
        }
        initViewModel()
        initFaceCheck()
    }

    override fun onDestroy() {
        super.onDestroy()
        isWhile = false
        videoUtilFront.stopPreview()
        videoUtilBack.stopPreview()
    }

    private fun initViewModel() {
        mBinding.viewModel = faceViewModel
        mBinding.lifecycleOwner = this
        faceViewModel.apply {
            showDialog.observe(this@EIFaceActivity, Observer { LoadDialog.showOrDismiss(this@EIFaceActivity, it) })
            errMsg.observe(this@EIFaceActivity, Observer { showToast(it) })
            speakStr.observe(this@EIFaceActivity, Observer { speaker.speak(it) })
            facePosition.observe(this@EIFaceActivity, Observer {
                //根据坐标画人脸框
                mBinding.rectView.drawFaceRect(
                        android.graphics.Rect(
                                (it.faceCoordinates[0][0].toFloat() / 480 * 720 * mBinding.rectView.width / 720).toInt(),
                                ((it.faceCoordinates[0][1].toFloat() + 106) / 480 * 720 * mBinding.rectView.height / 1280).toInt(),
                                ((it.faceCoordinates[0][0] + it.faceCoordinates[0][2]).toFloat() / 480 * 720 * mBinding.rectView.width / 720).toInt(),
                                (((it.faceCoordinates[0][1] + it.faceCoordinates[0][3]).toFloat() + 106) / 480 * 720 * mBinding.rectView.height / 1280).toInt()
                        ), "", "", it.drawColor
                )
                val nameValuesCamera = wffrdualcamapp.getNames() //用SDK获取检测到人脸数据
                var name = ""
                if (nameValuesCamera.isNotEmpty()) {
                    val valueList = nameValuesCamera[0].split(" ")
                    name = valueList[0]
                    Log.e("nameValuesCamera", "获取人脸数据编号:staff_no:$name")
                }

                mBinding.tvFaceInfo.text = "摄像头距离:" + it.faceCoordinates[0][2].toFloat() / 480 + "\n" +
                        "识别人脸：" + name + "\n" +
                        "相识度：" + it.confidence[0]
            })
        }
    }

    fun initFaceCheck() {
        /**
         * 启动人脸识别任务线程
         */
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                var now: Long
                var pas: Long = 0
                var fps = 0f
                while (isWhile) {
                    //延迟500ms检测一次,太快会内存溢出
                    delay(1000)
                    openCamera()
                    if (!videoUtilFront.isSyncFlag) {
                        //摄像头未初始化完毕
                        continue
                    }
                    //显示帧时间
//                    now = System.currentTimeMillis()
//                    fps = 1000 / (now - pas).toFloat()
//                    pas = System.currentTimeMillis()
                    if (!iscameraopen) {
                        delay(500)
                        Log.d("zheng", "sleeping")
                        continue
                    }
                    //控制暂停识别标识
                    if (isPause) {
                        continue
                    }
                    //获取摄像头数据，传入 wffr 中识别
                    if (videoUtilBack.getData() != null) {
                        data11 = wffrjni.rotateImage(videoUtilBack.getData(), 640, 480, 1, 90)
                        data22 = wffrjni.rotateImageAndMirror(videoUtilBack.getData(), 640, 480, 1, 90)
                    } else {
                        continue
                    }

                    //识别到后会将人脸信息坐标写入到 wffrdualcamapp.faceCoordinates 中
                    wffrdualcamapp.setState(1)
                    wffrdualcamapp.startExecution(data11, data22, 480, 640, "", "", false)
                    //检查 data 中的人脸数据
                    if (!faceViewModel.checkFaceData()) {
                        mBinding.rectView.clearRect()
                        continue
                    }

                    //目前识别只有2个模式,录入和签到
                    if (is_screenshot) {
                        //手动进行人脸录入,截取当前摄像头数据做录入用
                        backCameraData = data11!!
                        frontCameraData = data22!!
                        isPause = true
                        faceViewModel.addRecord(System.currentTimeMillis().toString(), System.currentTimeMillis().toString())
                        continue
                    }
                }
            }
        }
    }

    /**
     * 打开摄像头
     */
    fun openCamera() {
        if (videoUtilBack.mCamera == null || videoUtilFront.mCamera == null) {
            Log.e("openCamera", "openCamera return")
            return
        }
        synchronized(this) {
            if (!iscameraopen) {
                Log.e("IS_RADAR", "摄像头没开启")
                videoUtilBack.mCamera!!.setPreviewCallback(videoUtilBack)
                videoUtilFront.mCamera!!.setPreviewCallback(videoUtilFront)
                videoUtilBack.mCamera!!.startPreview()
                videoUtilFront.mCamera!!.startPreview()
                iscameraopen = true
            }
            if (closecamera != null) {
                closecamera!!.cancel()
            }
        }
    }
}
