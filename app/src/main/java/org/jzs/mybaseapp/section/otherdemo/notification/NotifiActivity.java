package org.jzs.mybaseapp.section.otherdemo.notification;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jzs.mybaseapp.R;
import org.jzs.mybaseapp.common.base.EventBusEntity;
import org.jzs.mybaseapp.databinding.ActivityNotifiBinding;

import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

/**
 * 监听通知栏
 * Created by Jzs on 2018/6/4.
 */

public class NotifiActivity extends AppCompatActivity {

    private ActivityNotifiBinding mBinding;
    public static final String TAG = "NotifiActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_notifi);
        EventBus.getDefault().register(this);

        mBinding.buttonAssesc.setOnClickListener(v -> {
            if (!isEnabled()) {
                startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
            } else {
                mBinding.tvPermission.setTextColor(ContextCompat.getColor(this, R.color.red));
                mBinding.tvPermission.setText("监听权限：已打开");
                Toast toast = Toast.makeText(getApplicationContext(), "监控器开关已打开", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        mBinding.buttonStartNofi.setOnClickListener(v -> {
            mBinding.tvService.setText("监听服务：启动中...再次发送一个推送，便可启动服务。如果长时间未启动，请重启手机再次尝试");
            Intent intent = new Intent(this, NotificationCollectorMonitorService.class);
            startService(intent);
        });

        mBinding.btnRebindService.setOnClickListener(v -> {
            toggleNotificationListenerService();
        });

        if (isEnabled()) {
            mBinding.tvPermission.setTextColor(ContextCompat.getColor(this, R.color.red));
            mBinding.tvPermission.setText("监听权限：已打开");
        } else {
            mBinding.tvPermission.setText("监听权限：未打开");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(EventBusEntity entity) {
        if (entity.creat) {
            mBinding.tvService.setTextColor(ContextCompat.getColor(this, R.color.red));
            mBinding.tvService.setText("监听服务：已启动");
            mBinding.buttonStartNofi.setEnabled(false);
            mBinding.btnRebindService.setEnabled(false);
        }


        View inflate = getLayoutInflater().inflate(R.layout.item_notifi, null);
        ImageView ivIcon = inflate.findViewById(R.id.iv_icon);
        TextView tvTitle = inflate.findViewById(R.id.tv_title);
        TextView tvContent = inflate.findViewById(R.id.tv_content);
        if (entity.notifiIcon != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ivIcon.setImageIcon(entity.notifiIcon);
            }
        }
        tvTitle.setText(entity.title);
        tvContent.setText(entity.content);
        mBinding.rootLayout.addView(inflate);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    // 判断是否打开了通知监听权限
    private boolean isEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //重新开启NotificationMonitor
    private void toggleNotificationListenerService() {
        ComponentName thisComponent = new ComponentName(this, org.jzs.mybaseapp.section.otherdemo.notification.NLService.class);
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(thisComponent, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(thisComponent, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    private static boolean isNotificationListenerServiceEnabled(Context context) {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(context);
        if (packageNames.contains(context.getPackageName())) {
            return true;
        }
        return false;
    }
}
