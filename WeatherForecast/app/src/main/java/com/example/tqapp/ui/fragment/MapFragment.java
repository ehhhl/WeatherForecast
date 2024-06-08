package com.example.tqapp.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.tqapp.common.BaseBindingFragment;
import com.example.tqapp.databinding.FragmentMapBinding;
import com.llw.goodweather.MainActivity;

public class MapFragment extends BaseBindingFragment<FragmentMapBinding> implements OnGetGeoCoderResultListener {

    @Override
    protected void initData() {

        mapView = viewBinder.mapView;
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        requestLocation();
    }

    private GeoCoder mGeoCoder;

    /**
     * /**
     * 逆地理编码请求
     *
     * @param latLng
     */
    private void reverseRequest(LatLng latLng) {
        if (null == latLng) {
            return;
        }

        ReverseGeoCodeOption reverseGeoCodeOption = new ReverseGeoCodeOption().location(latLng)
                .newVersion(1)
                .radius(10)
                .pageNum(1);

        if (null == mGeoCoder) {
            mGeoCoder = GeoCoder.newInstance();
        }

        mGeoCoder.setOnGetGeoCodeResultListener(this);
        mGeoCoder.reverseGeoCode(reverseGeoCodeOption);
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (null == reverseGeoCodeResult) {
            return;
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                String province = reverseGeoCodeResult.getAddressDetail().district;
                String city = reverseGeoCodeResult.getAddressDetail().city;
                Log.d("BAIDUMMMAP", "onGetReverseGeoCodeResult: " + province + "-" + city);
                Intent intent = new Intent(requireActivity(), MainActivity.class);
                intent.putExtra("district", province);
                intent.putExtra("city", city);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initListener() {
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                reverseRequest(latLng);
            }

            @Override
            public void onMapPoiClick(MapPoi mapPoi) {

            }
        });
    }

    private MapView mapView;
    private BaiduMap baiduMap;
    private MyLocationListener myLocationListener;
    private LocationClient mLocationClient;

    private void requestLocation() {
//定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        mLocationClient = new LocationClient(requireActivity().getApplicationContext());
//声明LocationClient类实例并配置定位参数
        LocationClientOption locationOption = new LocationClientOption();
        myLocationListener = new MyLocationListener();
//注册监听函数
        mLocationClient.registerLocationListener(myLocationListener);
//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        locationOption.setCoorType("bd09ll");
//可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        locationOption.setScanSpan(1000);
//可选，设置是否需要地址信息，默认不需要
        locationOption.setIsNeedAddress(true);
//可选，设置是否需要地址描述
        locationOption.setIsNeedLocationDescribe(true);
//可选，设置是否需要设备方向结果
        locationOption.setNeedDeviceDirect(false);
//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        locationOption.setLocationNotify(true);
//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationOption.setIgnoreKillProcess(true);
//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        locationOption.setIsNeedLocationDescribe(true);
//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        locationOption.setIsNeedLocationPoiList(true);
//可选，默认false，设置是否收集CRASH信息，默认收集
        locationOption.SetIgnoreCacheException(false);
//可选，默认false，设置是否开启Gps定位
        locationOption.setOpenGps(true);
//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        locationOption.setIsNeedAltitude(false);
//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        locationOption.setOpenAutoNotifyMode();
//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        locationOption.setOpenAutoNotifyMode(3000, 1, LocationClientOption.LOC_SENSITIVITY_HIGHT);
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        mLocationClient.setLocOption(locationOption);
//开始定位
        mLocationClient.start();
    }

    private boolean first = true;

    public class MyLocationListener extends BDAbstractLocationListener {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mapView == null) {
                return;
            }

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            Log.d("TAG", "onReceiveLocation: " + location.getCity());
            Log.d("TAG", "onReceiveLocation: latitude " + latitude + "---longitude" + longitude + "===" + location.getAddrStr());
            mLocationClient.stop();
            MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(latitude).longitude(longitude).build();
            LatLng ll = new LatLng(latitude, longitude);
            if (first) {
                first = false;
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
                baiduMap.animateMapStatus(update);
            }
            baiduMap.setMyLocationData(locData);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView = null;

    }
}
