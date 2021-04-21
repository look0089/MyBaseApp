package org.jzs.mybaseapp.section.otherdemo.eiface

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.MutableLiveData
import org.jzs.mybaseapp.common.base.BaseViewModel
import com.wf.wffrdualcamapp
import com.wf.wffrjni
import kotlinx.coroutines.delay

/**
 * Created by Jzs on 2020/10/26.
 */
class FaceViewModel : BaseViewModel() {
    val speakStr = MutableLiveData<String>()

    //人脸坐标 存放坐标,摄像头距离,画笔颜色
    val facePosition = MutableLiveData<FacePositionInfo>()

    //相识度
    val confidence = MutableLiveData<String>()

    /**
     * 识别人脸数据
     * 开始识别暂停识别线程,识别结束继续
     */
    fun startVerify() {
        launch {
//            log("开始识别挂起")
            EIFaceActivity.isPause = true
//            verifyGuarder()
//            log("结束识别")
            EIFaceActivity.isPause = false
        }
    }

    /**
     * 识别签到
     */
//    private suspend fun verifyGuarder() {
//        val realm = Realm.getDefaultInstance()
//        val nameValuesCamera = wffrdualcamapp.getNames() //用SDK获取检测到人脸数据
//        if (nameValuesCamera.isNotEmpty() && !SystemParams.getIsEnroll()) {
//            //从 wffrdualcamapp 中拿人脸数据
//            if (TextUtils.isEmpty(nameValuesCamera[0])) {
//                return
//            }
//            val valueList = nameValuesCamera[0].split(" ")
//            if (TextUtils.isEmpty(valueList[0])) {
//                return
//            }
//            val staffNo = valueList[0]
//            NLog.e("nameValuesCamera", "获取人脸数据编号:staff_no:$staffNo")
//            //从数据库查找用户,进行分析
//            DB.getUser_byStaff_no(staffNo, realm)?.let {
//                // head_portrait值为空时进行url下载人头像
//                if (it.head_portrait == "" && getCompleteUrl(it.photo)) {
//                    val bitmapPhoto: Bitmap? = Url2Bitmap.getHttpBitmap(it.photo)
//                    dbHandler.UpdUserPhoto(it.id, bitmap2byteArrayStr(bitmapPhoto))
//                }
//                // 如果是临时访客判断 有效开始访问时间
//                if (it.is_visit == 1) {
//                    if (compare_date(it.valid, DateUtil.nowDateTime) == -1) {
//                        //访客失效
//                        errMsg.value = "访客权限已失效，请联系管理员！"
//                        return
//                    }
//                }
//
//                //识别通过
//                facePopImage.value = it.getBitmap()
//                showFacePopLayout.value = true
//                voicePrompt(it.cid_name + when (SimpleDateFormat("HH").format(System.currentTimeMillis()).toInt()) {
//                    in 6..11 -> "、早上好"
//                    in 12..18 -> "、下午好"
//                    else -> "、晚上好"
//                } + if (SystemParams.getIsTemperature() && !TextUtils.isEmpty(takeTemperature)) ", 体温${takeTemperature}" else "")
//                verifyGuarderSuccess()
//
//                //隐藏签到框,重置体温数据
//                showFacePopLayout.value = false
//                temperature.value = ""
//                it.getBitmap()?.recycle()
//                //上传考勤信息
//                NLog.e("体温", takeTemperature)
//                DB.insertUplRecord(it.id, DateUtil.nowDateTime, getUid(), takeTemperature, Realm.getDefaultInstance())
//            }
//            if (!realm.isClosed) {
//                realm.close()
//            }
//        } else {
//            //未识别到人脸
//        }
//    }


    /**
     * 检查摄像头获得的人脸信息
     * return false 人脸信息异常,继续检测下一帧的信息
     * return true 人脸信息正确
     */
    var faceInfo: FacePositionInfo = FacePositionInfo()
    var centrefailed = 0 //不在镜头中间提示
    fun checkFaceData(): Boolean {
        //获取face 坐标
        val faceCoordinates = wffrdualcamapp.getFaceCoordinates()
        //返回所注册人脸的自信度值。 如果自信度为-1，则尚未通过活体检测，如果自信度为0，则通过活体检测并正在录入该人脸.
        val confidenceValuesCamera = wffrdualcamapp.getConfidence()
        val faceAngles = wffrjni.getFaceAngles()
        if (faceCoordinates != null && confidenceValuesCamera != null && confidenceValuesCamera.isNotEmpty() && faceCoordinates.isNotEmpty()) {
            //检测到人脸 分析人脸数据
            faceInfo.faceCoordinates = faceCoordinates
            faceInfo.confidence = confidenceValuesCamera
            Log.e("checkFaceData", "摄像头距离:" + faceCoordinates[0][2].toFloat() / 480)

            if (faceCoordinates[0][0].toFloat() <= 10.0 ||
                    faceCoordinates[0][0].toFloat() + faceCoordinates[0][2].toFloat() >= 470.0 ||
                    faceCoordinates[0][2].toFloat() >= 430.0 ||
                    faceCoordinates[0][1].toFloat() <= 5.0 ||
                    faceCoordinates[0][1].toFloat() + faceCoordinates[0][3].toFloat() >= 635.0) {
                centrefailed++
                if (centrefailed > 2) {
                    voicePrompt("请靠中间识别")
                }
            } else {
                centrefailed = 0
            }

            if (faceCoordinates[0][2].toFloat() / 480 > 0.65) {
                voicePrompt("请稍微站远一点")
            }

            val faceType = wffrjni.faceType()
            if (faceType.size > 0) {
                Log.d("EIWFFEJNI", "face_type: ${faceType[0]}")
                if (faceType[0] == 0) {
                    //提示请正对屏幕
                    voicePrompt("请正视屏幕")
                } else if (faceType[0] == 2) {
                    voicePrompt("请摘掉口罩")
                }
            }

            //识别到人脸,不符合识别要求,显示红框
            if (faceCoordinates[0][2].toFloat() / 480 > 0.65) {
                faceInfo.drawColor = Color.RED
                facePosition.postValue(faceInfo)
                return false
            }
            //人脸距离正常,显示黄框
            if (faceCoordinates[0][2].toFloat() / 480 < 0.65) {
                faceInfo.drawColor = Color.YELLOW
                facePosition.postValue(faceInfo)
                return true
            }
            //通过检测的人脸,显示绿框
//            if (confidenceValuesCamera[0] > 0f) {
//                faceInfo.drawColor = Color.GREEN
//                facePosition.postValue(faceInfo)
//                return true
//            }
        } else {
            Log.e("checkFaceData", "未检测到人脸")
            //未检测到人脸
            if (EIFaceActivity.is_screenshot) {
                EIFaceActivity.is_screenshot = false
                voicePrompt("未检测到人脸")
            }
            return false
        }
        return false
    }

    fun addRecord(no: String, name: String) {
        launch {
            showDialog.value = true
            Log.e("uploadFaceInfo", "录入名字:first:" + no + ",lastname:" + name)
            //删除wffrdb和 wffrdbExtract mul
//            FileUtil.deleteDire(File(Property.wffrdbPath + "/"))
//            FileUtil.deleteDire(File(Property.wffrEXPath + "/"))

            //录入人脸到数据库
            wffrdualcamapp.setState(2)
            val s = wffrdualcamapp.startExecution(
                    EIFaceActivity.backCameraData,
                    EIFaceActivity.frontCameraData, 480, 640, no, name, false)
            delay(500)
            Log.e("uploadFaceInfo", "录入状态:" + WffrStatus.getName(s))

            //获取人脸数据db上传云端
            wffrdualcamapp.get_people_db(no, name)
            var getEx = wffrjni.ExtractPersonByName(no, name)
            Log.e("uploadFaceInfo", "生成pid0:" + WffrStatus.getName(getEx))
            if (getEx != WffrStatus.Success.value) {
                errMsg.value = "人脸数据生成失败,请正对屏幕后重试"
                speakStr.postValue("请正对屏幕后重试")
                delay(2000)
                showDialog.value = false
                EIFaceActivity.is_screenshot = false
                EIFaceActivity.isPause = false
                return@launch
            }

            voicePrompt("人脸数据录入成功")
            errMsg.value = "人脸数据录入成功,编号" + no
            showDialog.value = false
            delay(4000)
            EIFaceActivity.isPause = false
            EIFaceActivity.is_screenshot = false
        }
    }

    var nowTime = 0L
    fun voicePrompt(str: String) {
        if (nowTime + 4000 > System.currentTimeMillis()) {
            return
        }
        nowTime = System.currentTimeMillis()
        speakStr.postValue(str)
    }
}

