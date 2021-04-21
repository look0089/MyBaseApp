package org.jzs.mybaseapp.section.otherdemo.vpn;

import android.net.VpnService;
import android.os.ParcelFileDescriptor;

import org.jzs.mybaseapp.common.utils.ToastUtils;

/**
 * Created by Jzs on 2018/6/26.
 */
public class MyVpnService extends VpnService {

    @Override
    public void onCreate() {
        super.onCreate();
        initVpn();
        ToastUtils.showToast("MyVpN onCreate");
    }

    private void initVpn() {
        Builder builder = new Builder();
        //网络端口的最大传输单元
        builder.setMtu(1500);
        //IP地址
        builder.addAddress("47.91.152.158", 24);

        //匹配上的IP包，才会被路由到虚拟端口上去。如果是0.0.0.0/0的话，则会将所有的IP包都路由到虚拟端口上去；
        builder.addRoute("0.0.0.0", 24);

        //该端口的DNS服务器地址；
        builder.addDnsServer("47.91.152.158");

//        builder.addSearchDomain(1);
        //VPN连接的名字
        builder.setSession("JZS VPN");
        // builder.setConfigureIntent(1);

        ParcelFileDescriptor interface1 = builder.establish();
        int fd = interface1.getFd();


//        // Packets to be sent are queued in this input stream.
//        FileInputStream in = new FileInputStream(interface1.getFileDescriptor());
//
//        // Packets received need to be written to this output stream.
//        FileOutputStream out = new FileOutputStream(interface1.getFileDescriptor());
//
//        // Allocate the buffer for a single packet.
//        ByteBuffer packet = ByteBuffer.allocate(32767);
//        try {
//            // Read packets sending to this interface
//            int length = in.read(packet.array());
//            // Write response packets back
//            out.write(packet.array(), 0, length);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        System.out.println("hello world");
//        InetAddress ip = null;
//        try {
//            ip = InetAddress.getByName("www.baidu.com");
//            System.out.println("是否可达" + ip.isReachable(80));
//            System.out.println(ip.getHostAddress());
//            InetAddress local = InetAddress.getByAddress(new byte[]{127, 0, 0, 1});
//            System.out.println("是否可达" + local.isReachable(5000));
//            System.out.println(local.getCanonicalHostName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
