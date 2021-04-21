package org.jzs.mybaseapp.section.weightdemo.permission;

import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;

import org.jzs.mybaseapp.R;
import org.jzs.mybaseapp.common.utils.ToastUtils;
import org.jzs.mybaseapp.databinding.ActivityPermissionBinding;


/**
 * 动态权限
 */
public class PermissionActivity extends AppCompatActivity {

    private ActivityPermissionBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_permission);
        initOnClick();
    }

    private void initOnClick() {
        mBinding.btnCamera.setOnClickListener(v -> {
            PermissionUtils.requestCamera(PermissionActivity.this, () -> ToastUtils.showToast("权限已获得"));
        });
        mBinding.btnPhone.setOnClickListener(v -> {
            PermissionUtils.requestPhone(PermissionActivity.this, () -> ToastUtils.showToast("权限已获得"));
        });
        mBinding.btnLocation.setOnClickListener(v -> {
            PermissionUtils.requestLocation(PermissionActivity.this, () -> ToastUtils.showToast("权限已获得"));
        });
        mBinding.btnMore.setOnClickListener(v -> {
//            PermissionUtils.requestCustom(PermissionActivity.this, () -> ToastUtils.showToast("权限已获得"));
        });
        mBinding.btnSetting.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        });
    }
}
