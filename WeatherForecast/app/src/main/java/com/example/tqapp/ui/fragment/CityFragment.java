package com.example.tqapp.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentTransaction;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.tqapp.R;
import com.example.tqapp.common.App;
import com.example.tqapp.common.BaseBindingFragment;
import com.example.tqapp.common.BindAdapter;
import com.example.tqapp.common.bean.History;
import com.example.tqapp.common.database.Database;
import com.example.tqapp.databinding.FragmentTodayBinding;
import com.example.tqapp.databinding.ItemCityBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CityFragment extends BaseBindingFragment<FragmentTodayBinding> {

    private int current = 0;
    private BindAdapter<ItemCityBinding, History> cityAdapter = new BindAdapter<ItemCityBinding, History>() {
        @Override
        public ItemCityBinding createHolder(ViewGroup parent) {
            return ItemCityBinding.inflate(getLayoutInflater(), parent, false);
        }

        @Override
        public void bind(ItemCityBinding itemCityBinding, History s, int position) {
            if (currentHistory != null && position == 0) {
                itemCityBinding.getRoot().setText(s.city + "(当前)");
            } else {
                itemCityBinding.getRoot().setText(s.city);
            }

            if (current == position) {
                itemCityBinding.getRoot().setBackgroundColor(Color.WHITE);
            } else {
                itemCityBinding.getRoot().setBackgroundColor(Color.TRANSPARENT);
            }
            itemCityBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    current = position;
                    cityAdapter.notifyDataSetChanged();
                    changeFragment();
                }
            });
        }
    };

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        viewBinder.rvCity.setAdapter(cityAdapter);
        requestLocation();
    }

    private MyLocationListener myLocationListener;
    private LocationClient mLocationClient;

    private void requestLocation() {
        mLocationClient = new LocationClient(requireActivity().getApplicationContext());
        LocationClientOption locationOption = new LocationClientOption();
        myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locationOption.setCoorType("bd09ll");
        locationOption.setScanSpan(1000);
        locationOption.setIsNeedAddress(true);
        locationOption.setIsNeedLocationDescribe(true);
        locationOption.setNeedDeviceDirect(false);
        locationOption.setLocationNotify(true);
        locationOption.setIgnoreKillProcess(true);
        locationOption.setIsNeedLocationDescribe(true);
        locationOption.setIsNeedLocationPoiList(true);
        locationOption.SetIgnoreCacheException(false);
        locationOption.setOpenGps(true);
        locationOption.setIsNeedAltitude(false);
        locationOption.setOpenAutoNotifyMode();
        locationOption.setOpenAutoNotifyMode(3000, 1, LocationClientOption.LOC_SENSITIVITY_HIGHT);
        mLocationClient.setLocOption(locationOption);
        mLocationClient.start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeCity(History history) {
        currentHistory = history;
        cityAdapter.getData().clear();
        current = 0;
        cityAdapter.getData().add(0, currentHistory);
        cityAdapter.getData().addAll(Database.getDao().getMyHistory(App.user.account, 100));
        cityAdapter.notifyDataSetChanged();
        changeFragment();
    }

    private History currentHistory;

    public class MyLocationListener extends BDAbstractLocationListener {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null) {
                return;
            }
            mLocationClient.stop();
            currentHistory = new History();
            currentHistory.city = location.getCity();
            currentHistory.provinces = location.getProvince();
            current = 0;
            cityAdapter.getData().add(0, currentHistory);
            cityAdapter.notifyDataSetChanged();
            changeFragment();
        }
    }

    @Override
    protected void initListener() {

    }

    private void changeFragment() {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString("city", cityAdapter.getData().get(current).city);
        args.putString("provinces", cityAdapter.getData().get(current).provinces);
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.ll_info, fragment).commitNow();
    }

    @Override
    public void onResume() {
        super.onResume();
        onHiddenChanged(false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            cityAdapter.getData().clear();
            if (currentHistory != null) {
                cityAdapter.getData().add(currentHistory);
            }
            cityAdapter.getData().addAll(Database.getDao().getMyHistory(App.user.account, 100));
            cityAdapter.notifyDataSetChanged();
        }
    }
}
