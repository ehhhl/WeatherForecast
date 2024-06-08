package com.example.tqapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tqapp.common.BaseBindingActivity;
import com.example.tqapp.databinding.ActivityHomeBinding;
import com.example.tqapp.ui.fragment.CalendarFragment;
import com.example.tqapp.ui.fragment.CityFragment;
import com.example.tqapp.ui.fragment.MapFragment;
import com.example.tqapp.ui.fragment.MusicFragment;
import com.example.tqapp.ui.fragment.SettingFragment;
import com.example.tqapp.ui.fragment.WeatherFragment;
import com.google.android.material.navigation.NavigationBarView;
import com.llw.goodweather.WeatherFragmentNew;
import com.llw.goodweather.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends BaseBindingActivity<ActivityHomeBinding> {

    @Override
    protected void initListener() {
        requestPermission();
        changePage(fragments.get(0));
        viewBinder.bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.map:
                        changePage(fragments.get(0));
                        break;
                    case R.id.weather:
                        changePage(fragments.get(1));
                        break;
                    case R.id.record:
                        changePage(fragments.get(2));
                        break;
                    case R.id.music:
                        changePage(fragments.get(3));
                        break;
                    case R.id.setting:
                        changePage(fragments.get(4));
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void initData() {
        StatusBarUtil.transparencyBar(this);
        fragments.add(new MapFragment());
        fragments.add(new WeatherFragmentNew());
        fragments.add(new CalendarFragment());
        fragments.add(new MusicFragment());
        fragments.add(new SettingFragment());

    }

    private List<Fragment> fragments = new ArrayList<>();

    private void changePage(Fragment fragment) {

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        for (Fragment item : fragments) {
            fragmentTransaction.hide(item);
        }
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.fcv, fragment).show(fragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void requestPermission() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[0]);
            Log.d("TAG", "onCreate: " + Arrays.toString(permissions));
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }
}