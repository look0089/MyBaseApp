package org.jzs.mybaseapp.section.demo.map;

import android.os.Bundle;
import android.view.View;
import android.widget.PopupWindow;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.Circle;
import com.tencent.mapsdk.raster.model.CircleOptions;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;

import org.jzs.mybaseapp.R;
import org.jzs.mybaseapp.common.base.EventBusEntity;
import org.jzs.mybaseapp.common.utils.TencentLocationUtils;
import org.jzs.mybaseapp.common.utils.ToastUtils;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Jzs created 2017/8/2
 */

public class MapActivity extends AppCompatActivity {

    private MapView mapview = null;
    private TencentMap mTencentMap;

    private Marker mLocationMarker;
    private Circle mAccuracyCircle;

    private Marker showMarker;
    private PopupWindow mBottomSheetPop;
    private View mapSheetView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapview = (MapView) findViewById(R.id.mapview);
        mTencentMap = mapview.getMap();
        initMap();
    }


    @Override
    protected void onDestroy() {
        mapview.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mapview.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mapview.onResume();
        startLocation();
        super.onResume();
    }

    @Override
    protected void onStop() {
        mapview.onStop();
        super.onStop();
    }

    private void initMap() {
        //移除腾讯地图 logo
        mapview.removeViewAt(2);

        //添加 marker 点击事件
        mTencentMap.setOnMarkerClickListener(marker -> {
            showMarker = marker;
            showMarker.showInfoWindow();
            return true;
        });

        //添加 marker 弹出的 infowindow 点击事件
        mTencentMap.setOnInfoWindowClickListener(marker -> {
            ToastUtils.showToast(((EventBusEntity) marker.getTag()).info);
        });

        //点击空白处隐藏 marker
        mTencentMap.setOnMapClickListener(latLng -> {
            if (showMarker != null) {
                showMarker.hideInfoWindow();
                showMarker = null;
            }
        });

    }

    public void location(View v) {
        startLocation();
    }

    public void openMap(View v) {
        openMapApp();
    }

    /**
     * 百度坐标转GCJ02
     *
     * @param lat
     * @param lng
     * @return
     */
    public LatLng Convert_BD09_To_GCJ02(double lat, double lng) {
        double x_pi = Math.PI * 3000.0 / 180.0;
        double x = lng - 0.0065, y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        lng = z * Math.cos(theta);
        lat = z * Math.sin(theta);
        return new LatLng(lat, lng);
    }

    private void startLocation() {
        TencentLocationUtils.getInstance()
                .initLocation(this, new TencentLocationUtils.MyLocationListener() {
                    @Override
                    public void onSuccess(TencentLocation location) {
                        // 定位成功
                        String mRequestParams = "";
                        StringBuilder sb = new StringBuilder();
                        sb.append("定位参数=").append(mRequestParams).append("\n");
                        sb.append("(纬度=").append(location.getLatitude()).append(",经度=")
                                .append(location.getLongitude()).append(",精度=")
                                .append(location.getAccuracy()).append("), 来源=")
                                .append(location.getProvider()).append(", 地址=")
                                .append(location.getAddress());
                        ToastUtils.showToast(sb.toString());

                        LatLng latLngLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        mTencentMap.setCenter(latLngLocation);
                        mTencentMap.setZoom(13);

                        EventBusEntity eventBusEntity = new EventBusEntity();

                        // 更新 location Marker
                        if (mLocationMarker == null) {
                            mLocationMarker = mTencentMap.addMarker(new MarkerOptions().
                                    position(latLngLocation).
                                    title(location.getAddress()).
                                    tag(eventBusEntity).
                                    icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                        } else {
                            mLocationMarker.setPosition(latLngLocation);
                        }

                        //紫色范围圈
                        if (mAccuracyCircle == null) {
                            mAccuracyCircle = mTencentMap.addCircle(new CircleOptions().
                                    center(latLngLocation).
                                    radius(location.getAccuracy()).
                                    fillColor(0x884433ff).
                                    strokeColor(0xaa1122ee).
                                    strokeWidth(1));
                        } else {
                            mAccuracyCircle.setCenter(latLngLocation);
                            mAccuracyCircle.setRadius(location.getAccuracy());
                        }
                    }

                    @Override
                    public void onFail(int error) {
                        ToastUtils.showToast("定位失败，错误码：" + error);
                    }
                });
    }

    private void openMapApp() {
        BottomPopDialog bottomPopDialog = new BottomPopDialog(this);
        bottomPopDialog.show();

    }
}
