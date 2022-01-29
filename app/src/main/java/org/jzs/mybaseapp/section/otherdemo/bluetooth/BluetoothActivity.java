package org.jzs.mybaseapp.section.otherdemo.bluetooth;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ListView;

import org.jzs.mybaseapp.R;

/**
 * @author Jzs created 2017/8/2
 */

public class BluetoothActivity extends AppCompatActivity {
    private Context context = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        setTitle("蓝牙打印");
        setContentView(R.layout.activity_bluetooth);
        this.initListener();
    }

    private void initListener() {
        ListView unbondDevices = (ListView) this.findViewById(R.id.unbondDevices);
        ListView bondDevices = (ListView) this.findViewById(R.id.bondDevices);
        Button switchBT = (Button) this.findViewById(R.id.openBluetooth_tb);
        Button searchDevices = (Button) this.findViewById(R.id.searchDevices);

        BluetoothAction bluetoothAction = new BluetoothAction(this.context,
                unbondDevices,
                bondDevices,
                switchBT,
                searchDevices,
                BluetoothActivity.this);
        Button returnButton = (Button) this.findViewById(R.id.return_Bluetooth_btn);

        bluetoothAction.setSearchDevices(searchDevices);
        bluetoothAction.initView();

        switchBT.setOnClickListener(bluetoothAction);
        searchDevices.setOnClickListener(bluetoothAction);
        returnButton.setOnClickListener(bluetoothAction);
    }
}
