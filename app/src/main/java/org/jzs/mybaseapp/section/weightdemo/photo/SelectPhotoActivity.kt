package org.jzs.mybaseapp.section.weightdemo.photo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import org.jzs.mybaseapp.R
import org.jzs.mybaseapp.common.utils.GlideUtils
import org.jzs.mybaseapp.section.weightdemo.permission.PermissionListener
import org.jzs.mybaseapp.section.weightdemo.permission.PermissionUtils
import org.jzs.mybaseapp.common.utils.glide.GlideEngine
import org.jzs.mybaseapp.databinding.ActivitySelectPhotoBinding


/**
 * 图片选择器
 */
class SelectPhotoActivity : AppCompatActivity() {
    lateinit var mBinding: ActivitySelectPhotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_select_photo)
        initView()
    }

    fun initView() {
        mBinding.ivHead.setOnClickListener {
            PermissionUtils.requestCameraAndStorage(this, PermissionListener {
                DialogiOS(this).apply {
                    setTitles(arrayListOf("拍照", "从相册选择"))
                    this.setOnItemClickListener { position, str ->
                        //打开相册或相机
                        PictureSelector.create(this@SelectPhotoActivity).run {
                            if (position == 0) {
                                openCamera(PictureMimeType.ofImage())
                            } else {
                                openGallery(PictureMimeType.ofImage())
                            }
                        }.imageEngine(GlideEngine.createGlideEngine())
                                .selectionMode(PictureConfig.SINGLE)
                                .isCompress(true)
                                .isAndroidQTransform(false)
                                .forResult(object : OnResultCallbackListener<LocalMedia> {

                                    override fun onResult(result: MutableList<LocalMedia>) {
                                        //获取图片路径
                                        val imagePath = result[0].compressPath
                                        GlideUtils.setImage(imagePath, mBinding.ivHead)
                                        mBinding.tvPath.text = imagePath
                                    }

                                    override fun onCancel() {
                                    }
                                })
                    }
                    show()
                }
            })
        }
    }

}
