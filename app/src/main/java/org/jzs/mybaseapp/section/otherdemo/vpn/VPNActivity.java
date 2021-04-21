package org.jzs.mybaseapp.section.otherdemo.vpn;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.net.VpnService;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import org.jzs.mybaseapp.R;
import org.jzs.mybaseapp.databinding.ActivityVpnBinding;

/**
 * 创建VPN
 *
 * @author Jzs created 2018/6/26
 * @email 355392668@qq.coms
 */

public class VPNActivity extends AppCompatActivity {

    private ActivityVpnBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_vpn);
        //判断当前是否有 VPN 连接
        Intent intent = VpnService.prepare(VPNActivity.this);
        if (intent != null) {
            startActivityForResult(intent, 0);
        } else {
            onActivityResult(0, RESULT_OK, null);
        }
        mBinding.btnService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = VpnService.prepare(VPNActivity.this);
                if (intent != null) {
                    startActivityForResult(intent, 0);
                } else {
                    onActivityResult(0, RESULT_OK, null);
                }
            }
        });
    }

    protected void onActivityResult(int request, int result, Intent data) {
        if (result == RESULT_OK) {
            Intent intent = new Intent(this, MyVpnService.class);
            startService(intent);
        }
    }
}
