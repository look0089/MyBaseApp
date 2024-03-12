package org.jzs.mybaseapp.section.otherdemo.waterpic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import org.jzs.mybaseapp.R;
import org.jzs.mybaseapp.common.utils.AppLog;
import org.jzs.mybaseapp.common.utils.MediaScannerUtils;
import org.jzs.mybaseapp.common.utils.ToastUtils;
import org.jzs.mybaseapp.common.utils.glide.GlideEngine;
import org.jzs.mybaseapp.section.otherdemo.waterpic.photoinfodialog.PhotoDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 图片添加水印
 *
 * @author Jzs created 2017/8/2
 */

public class PhotoActivity extends AppCompatActivity {
    public static final String TAG = "PhotoActivity";

    private ImageView mSourImage;
    private ImageView mWartermarkImage;
    private List<Uri> mSelected;
    private Bitmap textBitmap;
    private PhotoDialog dialog;
    private List<PhotoEntity> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        initView();
    }

    private void initView() {
        mSourImage = (ImageView) findViewById(R.id.sour_pic);
        mWartermarkImage = (ImageView) findViewById(R.id.wartermark_pic);

        Bitmap sourBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sour_pic);
        mSourImage.setImageBitmap(sourBitmap);

//        Bitmap waterBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.weixin);

//        Bitmap watermarkBitmap = WaterImageUtil.createWaterMaskCenter(sourBitmap, waterBitmap);
//        watermarkBitmap = WaterImageUtil.createWaterMaskLeftBottom(this, watermarkBitmap, waterBitmap, 0, 0);
//        watermarkBitmap = WaterImageUtil.createWaterMaskRightBottom(this, watermarkBitmap, waterBitmap, 0, 0);
//        watermarkBitmap = WaterImageUtil.createWaterMaskLeftTop(this, watermarkBitmap, waterBitmap, 0, 0);
//        watermarkBitmap = WaterImageUtil.createWaterMaskRightTop(this, watermarkBitmap, waterBitmap, 0, 0);

        Bitmap textBitmap = WaterImageUtil.drawTextToLeftTop(this, sourBitmap, "左上角", 16, Color.RED, 0, 0);
        textBitmap = WaterImageUtil.drawTextToRightBottom(this, textBitmap, "右下角", 16, Color.RED, 0, 0);
        textBitmap = WaterImageUtil.drawTextToRightTop(this, textBitmap, "右上角", 16, Color.RED, 0, 0);
        textBitmap = WaterImageUtil.drawTextToLeftBottom(this, textBitmap, "左下角", 16, Color.RED, 0, 0);
        textBitmap = WaterImageUtil.drawTextToCenter(this, textBitmap, "中间", 16, Color.RED);

        mWartermarkImage.setImageBitmap(textBitmap);

        dialog = new PhotoDialog();
        dialog.initDialog(this);
        dialog.setCallBack(new PhotoDialog.DialogCallBack() {
            @Override
            public void sure() {
                mList = dialog.getList();
            }

            @Override
            public void cancel() {

            }
        });
    }

    public void set(View v) {
        dialog.show();
    }

    public void open(View v) {
        if (mList.size() == 0) {
            for (int i = 0; i < 5; i++) {
                PhotoEntity photoEntity = new PhotoEntity();
                photoEntity.title = "项目" + i;
                photoEntity.content = "内容" + i;
                mList.add(photoEntity);
            }
        }
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .selectionMode(PictureConfig.SINGLE)
                .isCompress(true)
                .isGif(false)
                .imageEngine(GlideEngine.createGlideEngine())
                .isAndroidQTransform(false)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        AppLog.e("imagePath", result.get(0).getCompressPath());
                        File f = new File(result.get(0).getCompressPath().replace("sdcard://", ""));
                        Glide.with(PhotoActivity.this).load(f).override(600, 600).into(mSourImage);
                        try {
                            Uri uri = Uri.fromFile(f);
                            Bitmap bitmapFormUri = WaterImageUtil.getBitmapFormUri(PhotoActivity.this, uri, false);
                            textBitmap = WaterImageUtil.drawProjectText(PhotoActivity.this, bitmapFormUri, mList, 50, Color.GREEN, 0, 0);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            textBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                            byte[] bytes = baos.toByteArray();

                            Glide.with(PhotoActivity.this).load(bytes).override(600, 600).into(mWartermarkImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }

    public void save(View v) {
        if (textBitmap != null) {
            String fileName = System.currentTimeMillis() + "";
            String picPath = MediaStore.Images.Media.insertImage(getContentResolver(), textBitmap, fileName, "description");
            MediaScannerUtils.mediaScan(this, picPath);
            ToastUtils.showToast("图片已保存到相册");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
//            mSelected = Matisse.obtainResult(data);
            Uri uri = mSelected.get(0);
//            Glide.with(PhotoActivity.this).load(uri).override(600, 600).into(mSourImage);
//            mSourImage.setImageURI(uri);
            try {
                Bitmap bitmapFormUri = WaterImageUtil.getBitmapFormUri(this, uri, false);
                textBitmap = WaterImageUtil.drawProjectText(this, bitmapFormUri, mList, 16, Color.RED, 0, 0);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                textBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] bytes = baos.toByteArray();

                Glide.with(PhotoActivity.this).load(bytes).into(mWartermarkImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.d(TAG, "mSelected: " + mSelected);
        }
    }
}
