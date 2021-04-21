package org.jzs.mybaseapp.common.utils;

import android.content.Context;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

/**
 * Created by Jzs on 2017/12/4 0004.
 * 腾讯定位工具类
 */

public class TencentLocationUtils {

    private static TencentLocationUtils instance;

    private TencentLocationManager mLocationManager;

    private TencentLocationListener tencentLocationListener;

    public static TencentLocationUtils getInstance() {
        if (instance == null) {
            instance = new TencentLocationUtils();
        }
        return instance;
    }

    public void initLocation(Context context, MyLocationListener listener) {
        mLocationManager = TencentLocationManager.getInstance(context);
        // 设置坐标系为 gcj-02, 缺省坐标为 gcj-02, 所以通常不必进行如下调用
        mLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);

        TencentLocationRequest request = TencentLocationRequest.create();
        request.setInterval(5000);//设置定位周期(位置监听器回调周期), 单位为 ms (毫秒)
        /**
         * REQ_LEVEL_GEO	0	包含经纬度
         * REQ_LEVEL_NAME	1	包含经纬度, 位置名称, 位置地址
         * REQ_LEVEL_ADMIN_AREA	3	包含经纬度，位置所处的中国大陆行政区划
         * REQ_LEVEL_POI	4	包含经纬度，位置所处的中国大陆行政区划及周边POI列表
         */
        request.setRequestLevel(1);
        request.isAllowCache();//设置是否允许使用缓存, 连续多次定位时建议允许缓存
//        request.setQQ()

        tencentLocationListener = new TencentLocationListener() {
            @Override
            public void onLocationChanged(TencentLocation location, int error, String reason) {
                /**
                 * 位置回调接口
                 * location	新的位置
                 * error	错误码
                 * reason	错误描述
                 */
                if (error == TencentLocation.ERROR_OK) {
                    listener.onSuccess(location);
                } else {
                    /**
                     * ERROR_NETWORK	1	网络问题引起的定位失败
                     * ERROR_BAD_JSON	2	GPS, Wi-Fi 或基站错误引起的定位失败：
                     1、用户的手机确实采集不到定位凭据，比如偏远地区比如地下车库电梯内等;
                     2、开关跟权限问题，比如用户关闭了位置信息，关闭了Wi-Fi，未授予app定位权限等。
                     * ERROR_WGS84	4	无法将WGS84坐标转换成GCJ-02坐标时的定位失败
                     * ERROR_UNKNOWN	404	未知原因引起的定位失败
                     */
                    listener.onFail(error);
                }
                //定位完成后删除位置监听器
                mLocationManager.removeUpdates(tencentLocationListener);
                tencentLocationListener = null;
            }

            @Override
            public void onStatusUpdate(String name, int status, String desc) {
                /**
                 * 状态回调接口
                 * name	GPS，Wi-Fi等
                 * status	新的状态, 启用或禁用
                 * desc	状态描述
                 */
            }
        };

        int i = mLocationManager.requestLocationUpdates(request, tencentLocationListener);
        switch (i) {
            case 0:
                AppLog.e("TencentLocation", "注册位置监听器成功");
                break;
            case 1:
                listener.onFail(11);
                AppLog.e("TencentLocation", "设备缺少使用腾讯定位SDK需要的基本条件");
                break;
            case 2:
                listener.onFail(22);
                AppLog.e("TencentLocation", "配置的 Key 不正确");
                break;
            case 3:
                listener.onFail(33);
                AppLog.e("TencentLocation", "自动加载libtencentloc.so失败");
                break;

            default:

                break;
        }
    }


    public interface MyLocationListener {

        void onSuccess(TencentLocation location);

        void onFail(int error);
    }
}
