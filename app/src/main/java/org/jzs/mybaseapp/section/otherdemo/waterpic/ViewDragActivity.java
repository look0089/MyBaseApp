package org.jzs.mybaseapp.section.otherdemo.waterpic;

import android.Manifest;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.tbruyelle.rxpermissions2.RxPermissions;

import org.jzs.mybaseapp.R;
import org.jzs.mybaseapp.common.utils.ToastUtils;
import org.jzs.mybaseapp.databinding.ActivityViewDragBinding;
import org.jzs.mybaseapp.section.otherdemo.waterpic.photoinfodialog.PhotoDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 可以动的view
 *
 * @author Jzs created 2017/11/1 0001
 */
public class ViewDragActivity extends AppCompatActivity {

    private ActivityViewDragBinding mDataBinding;
    private PhotoDialog dialog;
    private List<PhotoEntity> mList = new ArrayList<>();
    private RxPermissions rxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_view_drag);
        rxPermissions = new RxPermissions(this);
        dialog = new PhotoDialog();
        dialog.initDialog(this);
        dialog.setCallBack(new PhotoDialog.DialogCallBack() {
            @Override
            public void sure() {
                mList = dialog.getList();
                mDataBinding.myDv.setList(mList);
            }

            @Override
            public void cancel() {


            }
        });
        mDataBinding.setOpenDialog(v -> dialog.show());
        mDataBinding.setOpenCamera(v -> {
            rxPermissions
                    .request(Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) {
                            Log.e("sk", "onNext:111 ");
                            startActivity(new Intent(ViewDragActivity.this, CameraActivity.class));
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage("您未授权相机权限,将无法拍照,请在权限管理中开启相机权限")
                                    .setTitle("提示")
                                    .setPositiveButton("确认", (dialog12, which) -> {
                                        Uri packageURI = Uri.parse("package:" + getPackageName());
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                                        startActivity(intent);
                                    })
                                    .setNegativeButton("取消", (dialog1, which) -> dialog1.dismiss());
                            builder.create().show();
                        }
                    });
        });
        mDataBinding.myDv.setOnClickListener(v ->
                ToastUtils.showToast("Top:" + v.getTop() + ",Left" + v.getLeft())
        );
    }

}
