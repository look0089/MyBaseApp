package org.jzs.mybaseapp.section.otherdemo.bluetooth;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.jzs.mybaseapp.R;

public class PrintDataAction implements View.OnClickListener {
    private Context context = null;
    private TextView deviceName = null;
    private TextView connectState = null;
    private EditText printData = null;
    private String deviceAddress = null;
    private PrintDataService printDataService = null;

    public PrintDataAction(Context context, String deviceAddress,
                           TextView deviceName, TextView connectState) {
        super();
        this.context = context;
        this.deviceAddress = deviceAddress;
        this.deviceName = deviceName;
        this.connectState = connectState;
        this.printDataService = new PrintDataService(this.context, this.deviceAddress);
        this.initView();

    }

    private void initView() {
        // 设置当前设备名称
        this.deviceName.setText(this.printDataService.getDeviceName());
        // 一上来就先连接蓝牙设备
        boolean flag = this.printDataService.connect();
        if (flag == false) {
            // 连接失败
            this.connectState.setText("连接失败！");
        } else {
            // 连接成功
            this.connectState.setText("连接成功！");

        }
    }

    public void setPrintData(EditText printData) {
        this.printData = printData;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send) {
            String sendData = this.printData.getText().toString();
            this.printDataService.send(sendData + "\n");

            PrintUtils.selectCommand(PrintUtils.RESET);
            PrintUtils.selectCommand(PrintUtils.LINE_SPACING_DEFAULT);
            PrintUtils.selectCommand(PrintUtils.ALIGN_CENTER);
            PrintUtils.printText("美食餐厅\n\n");
            PrintUtils.selectCommand(PrintUtils.DOUBLE_HEIGHT_WIDTH);
            PrintUtils.printText("桌号：1号桌\n\n");
            PrintUtils.selectCommand(PrintUtils.NORMAL);
            PrintUtils.selectCommand(PrintUtils.ALIGN_LEFT);
            PrintUtils.printText(PrintUtils.printTwoData("订单编号", "201507161515\n"));
            PrintUtils.printText(PrintUtils.printTwoData("点菜时间", "2016-02-16 10:46\n"));
            PrintUtils.printText(PrintUtils.printTwoData("上菜时间", "2016-02-16 11:46\n"));
            PrintUtils.printText(PrintUtils.printTwoData("人数：2人", "收银员：张三\n"));

            PrintUtils.printText("--------------------------------\n");
            PrintUtils.selectCommand(PrintUtils.BOLD);
            PrintUtils.printText(PrintUtils.printThreeData("项目", "数量", "金额\n"));
            PrintUtils.printText("--------------------------------\n");
            PrintUtils.selectCommand(PrintUtils.BOLD_CANCEL);
            PrintUtils.printText(PrintUtils.printThreeData("面", "1", "0.00\n"));
            PrintUtils.printText(PrintUtils.printThreeData("米饭", "1", "6.00\n"));
            PrintUtils.printText(PrintUtils.printThreeData("铁板烧", "1", "26.00\n"));
            PrintUtils.printText(PrintUtils.printThreeData("一个测试", "1", "226.00\n"));
            PrintUtils.printText(PrintUtils.printThreeData("牛肉面啊啊", "1", "2226.00\n"));
            PrintUtils.printText(PrintUtils.printThreeData("牛肉面啊啊啊牛肉面啊啊啊", "888", "98886.00\n"));

            PrintUtils.printText("--------------------------------\n");
            PrintUtils.printText(PrintUtils.printTwoData("合计", "53.50\n"));
            PrintUtils.printText(PrintUtils.printTwoData("抹零", "3.50\n"));
            PrintUtils.printText("--------------------------------\n");
            PrintUtils.printText(PrintUtils.printTwoData("应收", "50.00\n"));
            PrintUtils.printText("--------------------------------\n");

            PrintUtils.selectCommand(PrintUtils.ALIGN_LEFT);
            PrintUtils.printText("备注：不要辣、不要香菜");
            PrintUtils.printText("\n\n\n\n\n");
        } else if (v.getId() == R.id.command) {
            this.printDataService.selectCommand();

        }
    }
}
